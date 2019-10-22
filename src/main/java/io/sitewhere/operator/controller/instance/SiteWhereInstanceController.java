/*
 * Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package io.sitewhere.operator.controller.instance;

import java.time.Clock;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.fabric8.kubernetes.api.model.Event;
import io.fabric8.kubernetes.api.model.EventBuilder;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.informers.SharedIndexInformer;
import io.fabric8.kubernetes.client.informers.SharedInformerFactory;
import io.sitewhere.k8s.crd.ResourceContexts;
import io.sitewhere.k8s.crd.ResourceLabels;
import io.sitewhere.k8s.crd.instance.SiteWhereInstance;
import io.sitewhere.k8s.crd.instance.SiteWhereInstanceList;
import io.sitewhere.k8s.crd.instance.SiteWhereInstanceStatus;
import io.sitewhere.k8s.crd.instance.configuration.InstanceConfigurationTemplate;
import io.sitewhere.operator.controller.ResourceChangeType;
import io.sitewhere.operator.controller.SiteWhereResourceController;

/**
 * Resource controller for SiteWhere instance monitoring.
 */
public class SiteWhereInstanceController extends SiteWhereResourceController<SiteWhereInstance> {

    /** Static logger instance */
    private static Logger LOGGER = LoggerFactory.getLogger(SiteWhereInstanceController.class);

    /** Resync period in milliseconds */
    private static final int RESYNC_PERIOD_MS = 10 * 60 * 1000;

    /** Workers for handling instance resource tasks */
    private ExecutorService instanceWorkers = Executors.newFixedThreadPool(2);

    public SiteWhereInstanceController(KubernetesClient client, SharedInformerFactory informerFactory) {
	super(client, informerFactory);
    }

    /**
     * Create informer.
     */
    public SharedIndexInformer<SiteWhereInstance> createInformer() {
	return getInformerFactory().sharedIndexInformerForCustomResource(ResourceContexts.INSTANCE_CONTEXT,
		SiteWhereInstance.class, SiteWhereInstanceList.class, RESYNC_PERIOD_MS);
    }

    /*
     * @see io.sitewhere.operator.controller.SiteWhereResourceController#
     * reconcileResourceChange(io.sitewhere.operator.controller.ResourceChangeType,
     * io.fabric8.kubernetes.client.CustomResource)
     */
    @Override
    public void reconcileResourceChange(ResourceChangeType type, SiteWhereInstance instance) {
	LOGGER.info(String.format("Detected %s resource change in instance %s.", type.name(),
		instance.getMetadata().getName()));
	if (type == ResourceChangeType.CREATE) {
	    getInstanceWorkers().execute(new InstanceCreationValidator(type, instance));
	}
    }

    /**
     * Ensure that the instance namespace exists and create if not.
     * 
     * @param instance
     * @return
     */
    protected Namespace ensureInstanceNamespace(SiteWhereInstance instance) {
	String ns = instance.getSpec().getInstanceNamespace();
	if (ns == null) {
	    LOGGER.error("No instance namespace specified.");
	    return null;
	} else {
	    LOGGER.info("Checking for existence of instance namespace.");
	    Namespace namespace = getClient().namespaces().withName(instance.getSpec().getInstanceNamespace()).get();
	    if (namespace == null) {
		LOGGER.info("Instance namespace did not exist. Creating.");
		namespace = new NamespaceBuilder().withNewMetadata().withName(instance.getSpec().getInstanceNamespace())
			.addToLabels(ResourceLabels.LABEL_SITEWHERE_INSTANCE, instance.getMetadata().getName())
			.endMetadata().build();
		namespace = getClient().namespaces().create(namespace);
	    } else {
		LOGGER.info("Instance namespace existence confirmed.");
	    }
	    return namespace;
	}
    }

    /**
     * Verify that the instance configuration template referenced by an instance
     * exists.
     * 
     * @param instance
     * @return
     */
    protected InstanceConfigurationTemplate verifyInstanceConfigurationTemplate(SiteWhereInstance instance) {
	InstanceConfigurationTemplate ict = getSitewhereClient().getInstanceConfigurationTemplates()
		.withName(instance.getSpec().getConfigurationTemplate()).get();
	if (ict == null) {
	    String message = String.format("Instance template '%s' was not found.",
		    instance.getSpec().getConfigurationTemplate());
	    LOGGER.warn(message);
	    throw new RuntimeException(message);
	}
	LOGGER.info(String.format("Instance template '%s' verified.", instance.getSpec().getConfigurationTemplate()));
	return ict;
    }

    /**
     * Process instance configuraton template. If instance or web configuration are
     * not set for the instance, they are copied from the template.
     * 
     * @param instance
     * @return
     */
    protected SiteWhereInstance processInstanceConfigurationTemplate(SiteWhereInstance instance) {
	LOGGER.info(String.format("Verifying that instance template '%s' exists.",
		instance.getSpec().getConfigurationTemplate()));

	// Set initial flags based on what user provided in yaml.
	if (instance.getStatus() == null) {
	    instance.setStatus(new SiteWhereInstanceStatus());
	}
	boolean initialInstanceStatus = instance.getStatus().isInstanceConfigured();
	boolean initialWebStatus = instance.getStatus().isWebConfigured();

	// Verify that instance configuration template exists.
	InstanceConfigurationTemplate ict = verifyInstanceConfigurationTemplate(instance);

	// Copy instance configuration from template if not already set.
	boolean hadInstanceConfiguration = instance.getSpec().getInstanceConfiguration() != null;
	if (!hadInstanceConfiguration) {
	    LOGGER.info("Instance configuration not set. Copying from template.");
	    instance.getSpec().setInstanceConfiguration(ict.getSpec().getInstanceConfiguration());
	}

	// Copy web configuration from template if not already set.
	boolean hadWebConfiguration = instance.getSpec().getWebConfiguration() != null;
	if (!hadWebConfiguration) {
	    LOGGER.info("Web configuration not set. Copying from template.");
	    instance.getSpec().setWebConfiguration(ict.getSpec().getWebConfiguration());
	}

	// Update status values based on current
	instance.getStatus().setInstanceConfigured(instance.getSpec().getInstanceConfiguration() != null);
	instance.getStatus().setWebConfigured(instance.getSpec().getWebConfiguration() != null);

	boolean instanceStatusUpdated = initialInstanceStatus != instance.getStatus().isInstanceConfigured();
	boolean webStatusUpdated = initialWebStatus != instance.getStatus().isWebConfigured();

	// Look up instance and make updates.
	if (!hadInstanceConfiguration || !hadWebConfiguration || instanceStatusUpdated || webStatusUpdated) {
	    LOGGER.info("Saving instance specification/status updates.");
	    return getSitewhereClient().getInstances().withName(instance.getMetadata().getName())
		    .createOrReplace(instance);
	} else {
	    LOGGER.info("Leaving existing instance configuration settings.");
	    return instance;
	}
    }

    /**
     * Create event for an instance.
     * 
     * @param instance
     * @param reason
     * @param type
     * @param message
     */
    protected void createEventForInstance(SiteWhereInstance instance, String reason, String type, String message) {
	String name = instance.getMetadata().getName() + "-event-" + System.currentTimeMillis();
	String timestamp = Instant.now(Clock.systemDefaultZone()).toString();
	Event event = new EventBuilder().withNewMetadata().withName(name)
		.withNamespace(instance.getMetadata().getNamespace()).endMetadata().withCount(1).withReason(reason)
		.withMessage(message).withType(type).withNewInvolvedObject()
		.withKind(SiteWhereInstance.class.getSimpleName()).withNamespace(instance.getMetadata().getNamespace())
		.withName(instance.getMetadata().getName()).endInvolvedObject().withFirstTimestamp(timestamp)
		.withLastTimestamp(timestamp).withNewSource().withComponent("sitewhere-operator").endSource().build();
	getClient().events().create(event);
    }

    /**
     * Validates k8s resources associated with new SiteWhere instance.
     */
    protected class InstanceCreationValidator extends InstanceWorkerRunnable {

	public InstanceCreationValidator(ResourceChangeType type, SiteWhereInstance instance) {
	    super(type, instance);
	}

	@Override
	public void run() {
	    LOGGER.info("Validating created SiteWhereInstance.");

	    // Verify that namespace for instance exists.
	    Namespace namespace = ensureInstanceNamespace(getInstance());
	    if (namespace == null) {
		return;
	    }

	    // Process instance configuration template.
	    SiteWhereInstance configured = processInstanceConfigurationTemplate(getInstance());
	    if (configured == null) {
		return;
	    }
	}
    }

    protected ExecutorService getInstanceWorkers() {
	return instanceWorkers;
    }
}
