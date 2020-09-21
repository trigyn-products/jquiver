package app.trigyn.common.dbutils.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PropertyMasterPK  implements Serializable {

	private static final long serialVersionUID = 430763775787389240L;

	@Column(name = "owner_type")
	private String ownerType =  null;
	
	@Column(name = "owner_id")
	private String ownerId =  null;
	
	@Column(name = "property_name")
	private String propertyName =  null;

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public PropertyMasterPK(String ownerType, String ownerId, String propertyName) {
		super();
		this.ownerType = ownerType;
		this.ownerId = ownerId;
		this.propertyName = propertyName;
	}

	public PropertyMasterPK() {
	}
	
	
}
