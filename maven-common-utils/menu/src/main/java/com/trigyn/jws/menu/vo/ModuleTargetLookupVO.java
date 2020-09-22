package com.trigyn.jws.menu.vo;

import java.io.Serializable;
import java.util.Objects;

public class ModuleTargetLookupVO implements Serializable{

	private static final long serialVersionUID = 6698280102493537756L;

	private Integer lookupId						= null;
	private String description						= null;
	
	public ModuleTargetLookupVO() {

	}

	public ModuleTargetLookupVO(Integer lookupId, String description) {
		this.lookupId 		= lookupId;
		this.description 	= description;
	}

	
	public Integer getLookupId() {
		return lookupId;
	}

	
	public void setLookupId(Integer lookupId) {
		this.lookupId = lookupId;
	}

	
	public String getDescription() {
		return description;
	}

	
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, lookupId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ModuleTargetLookupVO other = (ModuleTargetLookupVO) obj;
		return Objects.equals(description, other.description) && Objects.equals(lookupId, other.lookupId);
	}

	@Override
	public String toString() {
		return "ModuleTargetLookupVO [lookupId=" + lookupId + ", description=" + description + "]";
	}
	
}
