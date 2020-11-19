// +build !ignore_autogenerated

/*
Copyright (c) SiteWhere, LLC. All rights reserved. http://www.sitewhere.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

// Code generated by controller-gen. DO NOT EDIT.

package v1alpha4

import (
	runtime "k8s.io/apimachinery/pkg/runtime"
)

// DeepCopyInto is an autogenerated deepcopy function, copying the receiver, writing into out. in must be non-nil.
func (in *SiteWhereScriptCategory) DeepCopyInto(out *SiteWhereScriptCategory) {
	*out = *in
	out.TypeMeta = in.TypeMeta
	in.ObjectMeta.DeepCopyInto(&out.ObjectMeta)
	out.Spec = in.Spec
	out.Status = in.Status
}

// DeepCopy is an autogenerated deepcopy function, copying the receiver, creating a new SiteWhereScriptCategory.
func (in *SiteWhereScriptCategory) DeepCopy() *SiteWhereScriptCategory {
	if in == nil {
		return nil
	}
	out := new(SiteWhereScriptCategory)
	in.DeepCopyInto(out)
	return out
}

// DeepCopyObject is an autogenerated deepcopy function, copying the receiver, creating a new runtime.Object.
func (in *SiteWhereScriptCategory) DeepCopyObject() runtime.Object {
	if c := in.DeepCopy(); c != nil {
		return c
	}
	return nil
}

// DeepCopyInto is an autogenerated deepcopy function, copying the receiver, writing into out. in must be non-nil.
func (in *SiteWhereScriptCategoryList) DeepCopyInto(out *SiteWhereScriptCategoryList) {
	*out = *in
	out.TypeMeta = in.TypeMeta
	in.ListMeta.DeepCopyInto(&out.ListMeta)
	if in.Items != nil {
		in, out := &in.Items, &out.Items
		*out = make([]SiteWhereScriptCategory, len(*in))
		for i := range *in {
			(*in)[i].DeepCopyInto(&(*out)[i])
		}
	}
}

// DeepCopy is an autogenerated deepcopy function, copying the receiver, creating a new SiteWhereScriptCategoryList.
func (in *SiteWhereScriptCategoryList) DeepCopy() *SiteWhereScriptCategoryList {
	if in == nil {
		return nil
	}
	out := new(SiteWhereScriptCategoryList)
	in.DeepCopyInto(out)
	return out
}

// DeepCopyObject is an autogenerated deepcopy function, copying the receiver, creating a new runtime.Object.
func (in *SiteWhereScriptCategoryList) DeepCopyObject() runtime.Object {
	if c := in.DeepCopy(); c != nil {
		return c
	}
	return nil
}

// DeepCopyInto is an autogenerated deepcopy function, copying the receiver, writing into out. in must be non-nil.
func (in *SiteWhereScriptCategorySpec) DeepCopyInto(out *SiteWhereScriptCategorySpec) {
	*out = *in
}

// DeepCopy is an autogenerated deepcopy function, copying the receiver, creating a new SiteWhereScriptCategorySpec.
func (in *SiteWhereScriptCategorySpec) DeepCopy() *SiteWhereScriptCategorySpec {
	if in == nil {
		return nil
	}
	out := new(SiteWhereScriptCategorySpec)
	in.DeepCopyInto(out)
	return out
}

// DeepCopyInto is an autogenerated deepcopy function, copying the receiver, writing into out. in must be non-nil.
func (in *SiteWhereScriptCategoryStatus) DeepCopyInto(out *SiteWhereScriptCategoryStatus) {
	*out = *in
}

// DeepCopy is an autogenerated deepcopy function, copying the receiver, creating a new SiteWhereScriptCategoryStatus.
func (in *SiteWhereScriptCategoryStatus) DeepCopy() *SiteWhereScriptCategoryStatus {
	if in == nil {
		return nil
	}
	out := new(SiteWhereScriptCategoryStatus)
	in.DeepCopyInto(out)
	return out
}

// DeepCopyInto is an autogenerated deepcopy function, copying the receiver, writing into out. in must be non-nil.
func (in *SiteWhereScriptTemplate) DeepCopyInto(out *SiteWhereScriptTemplate) {
	*out = *in
	out.TypeMeta = in.TypeMeta
	in.ObjectMeta.DeepCopyInto(&out.ObjectMeta)
	out.Spec = in.Spec
	out.Status = in.Status
}

// DeepCopy is an autogenerated deepcopy function, copying the receiver, creating a new SiteWhereScriptTemplate.
func (in *SiteWhereScriptTemplate) DeepCopy() *SiteWhereScriptTemplate {
	if in == nil {
		return nil
	}
	out := new(SiteWhereScriptTemplate)
	in.DeepCopyInto(out)
	return out
}

// DeepCopyObject is an autogenerated deepcopy function, copying the receiver, creating a new runtime.Object.
func (in *SiteWhereScriptTemplate) DeepCopyObject() runtime.Object {
	if c := in.DeepCopy(); c != nil {
		return c
	}
	return nil
}

// DeepCopyInto is an autogenerated deepcopy function, copying the receiver, writing into out. in must be non-nil.
func (in *SiteWhereScriptTemplateList) DeepCopyInto(out *SiteWhereScriptTemplateList) {
	*out = *in
	out.TypeMeta = in.TypeMeta
	in.ListMeta.DeepCopyInto(&out.ListMeta)
	if in.Items != nil {
		in, out := &in.Items, &out.Items
		*out = make([]SiteWhereScriptTemplate, len(*in))
		for i := range *in {
			(*in)[i].DeepCopyInto(&(*out)[i])
		}
	}
}

// DeepCopy is an autogenerated deepcopy function, copying the receiver, creating a new SiteWhereScriptTemplateList.
func (in *SiteWhereScriptTemplateList) DeepCopy() *SiteWhereScriptTemplateList {
	if in == nil {
		return nil
	}
	out := new(SiteWhereScriptTemplateList)
	in.DeepCopyInto(out)
	return out
}

// DeepCopyObject is an autogenerated deepcopy function, copying the receiver, creating a new runtime.Object.
func (in *SiteWhereScriptTemplateList) DeepCopyObject() runtime.Object {
	if c := in.DeepCopy(); c != nil {
		return c
	}
	return nil
}

// DeepCopyInto is an autogenerated deepcopy function, copying the receiver, writing into out. in must be non-nil.
func (in *SiteWhereScriptTemplateSpec) DeepCopyInto(out *SiteWhereScriptTemplateSpec) {
	*out = *in
}

// DeepCopy is an autogenerated deepcopy function, copying the receiver, creating a new SiteWhereScriptTemplateSpec.
func (in *SiteWhereScriptTemplateSpec) DeepCopy() *SiteWhereScriptTemplateSpec {
	if in == nil {
		return nil
	}
	out := new(SiteWhereScriptTemplateSpec)
	in.DeepCopyInto(out)
	return out
}

// DeepCopyInto is an autogenerated deepcopy function, copying the receiver, writing into out. in must be non-nil.
func (in *SiteWhereScriptTemplateStatus) DeepCopyInto(out *SiteWhereScriptTemplateStatus) {
	*out = *in
}

// DeepCopy is an autogenerated deepcopy function, copying the receiver, creating a new SiteWhereScriptTemplateStatus.
func (in *SiteWhereScriptTemplateStatus) DeepCopy() *SiteWhereScriptTemplateStatus {
	if in == nil {
		return nil
	}
	out := new(SiteWhereScriptTemplateStatus)
	in.DeepCopyInto(out)
	return out
}
