package app.trigyn.core.menu.entities;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the module_role_association database table.
 * 
 */
@Entity
@Table(name="module_role_association")
@NamedQuery(name="ModuleRoleAssociation.findAll", query="SELECT m FROM ModuleRoleAssociation m")
public class ModuleRoleAssociation implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ModuleRoleAssociationPK id;

	@ManyToOne
	@JoinColumn(name="module_id", nullable=false, insertable=false, updatable=false)
	private ModuleListing moduleListing;
	
	public ModuleRoleAssociation() {
	}

	public ModuleRoleAssociationPK getId() {
		return this.id;
	}

	public void setId(ModuleRoleAssociationPK id) {
		this.id = id;
	}
	
	public void setModuleListing(ModuleListing moduleListing) {
		this.moduleListing = moduleListing;
	}

}