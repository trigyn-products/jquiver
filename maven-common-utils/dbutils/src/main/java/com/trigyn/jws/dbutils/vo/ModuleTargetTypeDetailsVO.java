package com.trigyn.jws.dbutils.vo;

import java.io.Serializable;
import java.util.Objects;

public class ModuleTargetTypeDetailsVO implements Serializable{

	private static final long serialVersionUID 	= 8875101658807300486L;
	private String targetTypeId 				= null;
	private String targetTypeName 				= null;
	
	public ModuleTargetTypeDetailsVO() {

	}

	public ModuleTargetTypeDetailsVO(String targetTypeId, String targetTypeName) {
		this.targetTypeId 		= targetTypeId;
		this.targetTypeName 	= targetTypeName;
	}

	
	public String getTargetTypeId() {
		return targetTypeId;
	}

	
	public void setTargetTypeId(String targetTypeId) {
		this.targetTypeId = targetTypeId;
	}

	
	public String getTargetTypeName() {
		return targetTypeName;
	}

	
	public void setTargetTypeName(String targetTypeName) {
		this.targetTypeName = targetTypeName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(targetTypeId, targetTypeName);
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
		ModuleTargetTypeDetailsVO other = (ModuleTargetTypeDetailsVO) obj;
		return Objects.equals(targetTypeId, other.targetTypeId) && Objects.equals(targetTypeName, other.targetTypeName);
	}

	@Override
	public String toString() {
		return "ModuleTargetTypeDetailsVO [targetTypeId=" + targetTypeId + ", targetTypeName=" + targetTypeName + "]";
	}
	
	
}
