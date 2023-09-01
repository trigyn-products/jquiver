package com.trigyn.jws.usermanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.usermanagement.entities.JwsEntityRoleAssociation;
import com.trigyn.jws.usermanagement.vo.JwsRoleVO;
import com.trigyn.jws.usermanagement.vo.SiteLayoutVO;

@Repository
public interface JwsEntityRoleAssociationRepository extends JpaRepository<JwsEntityRoleAssociation, String> {

	@Query(" SELECT entityRoleId FROM JwsEntityRoleAssociation WHERE entityId=:entityId AND roleId=:roleId ")
	String getEntityRoleIdByEntityAndRoleId(String entityId, String roleId);

	@Query(" FROM JwsEntityRoleAssociation WHERE entityId=:entityId AND moduleId=:moduleId ")
	List<JwsEntityRoleAssociation> getEntityRoles(String entityId, String moduleId);

	@Query(" SELECT new com.trigyn.jws.usermanagement.vo.JwsRoleVO( r.roleId,r.roleName) FROM JwsEntityRoleAssociation AS jera INNER JOIN jera.role  AS r WHERE jera.entityId=:entityId AND jera.moduleId=:moduleId  AND jera.isActive=:isActive")
	List<JwsRoleVO> getRoles(String entityId, String moduleId, Integer isActive);
	
	/**
	 * Toggles the active state for module entities when authentication is enabled
	 * wrt anonymous access
	 * @author Anomitro.Mukherjee
	 * 
	 * @param isActive the isActive to set
	 * @param entityIds the entityId list
	 * 
	 */
	@Modifying
	@Query("UPDATE JwsEntityRoleAssociation" + 
			" SET isActive = :isActive, isCustomUpdated = 1" + 
			" WHERE roleId = 'b4a0dda1-097f-11eb-9a16-f48e38ab9348'" + 
			" AND entityId IN :entityIds")
	void toggleAnonymousUserAccess(Integer isActive, List<String> entityIds);
	
	@Modifying
	@Query(" UPDATE JwsEntityRoleAssociation SET isActive=:isActive WHERE moduleTypeId =:moduleTypeId  AND roleId=:roleId")
	void updateEntityRelatedToModule(Integer moduleTypeId, String roleId, Integer isActive);

	@Query(" FROM JwsEntityRoleAssociation WHERE moduleTypeId=:commonModuleTypeId  GROUP BY entityId ")
	List<JwsEntityRoleAssociation> findEntityByModuleTypeId(Integer commonModuleTypeId);

	@Query(" FROM JwsEntityRoleAssociation WHERE entityRoleId=:entityRoleID")
	JwsEntityRoleAssociation getJwsEntityRoleAssociation(String entityRoleID);

	@Query(" SELECT DISTINCT jera.entityName FROM JwsEntityRoleAssociation AS jera INNER JOIN JwsMasterModules AS jmm ON jmm.moduleId = jera.moduleId "
			+ " AND jmm.moduleName = :moduleName WHERE jera.entityName = :entityName ")
	String getEntityNameByEntityAndRoleId(String moduleName, String entityName);

	@Query("SELECT new com.trigyn.jws.usermanagement.vo.SiteLayoutVO(ml.moduleUrl, COUNT(jera.roleId))  FROM JwsEntityRoleAssociation jera INNER JOIN ModuleListing ml ON jera.entityId= ml.moduleId "
			+ " INNER JOIN JwsRole jr ON jera.roleId = jr.roleId "
			+ " INNER JOIN ml.moduleListingI18ns AS mlI18n ON mlI18n.id.languageId = 1 "
			+ "  WHERE ml.moduleUrl LIKE %:moduleUrl%   AND jr.roleName IN :roleNames AND jera.isActive=:isActive ")
	List<SiteLayoutVO> hasAccessToSiteLayout(@Param("moduleUrl")String moduleUrl, @Param("roleNames")List<String> roleNames, @Param("isActive")Integer isActive);

	@Query("SELECT new com.trigyn.jws.usermanagement.vo.SiteLayoutVO(mlI18n.moduleName, ml.moduleUrl) FROM ModuleListing ml INNER JOIN ml.moduleListingI18ns AS mlI18n ON mlI18n.id.languageId = 1 "
			+ " WHERE ml.moduleUrl LIKE %:moduleUrl%  ")
	List<SiteLayoutVO> getSiteLayoutNameByUrl(String moduleUrl);

	@Query(" FROM JwsEntityRoleAssociation WHERE roleId=:roleId ")
	List<JwsEntityRoleAssociation> getEntityRolesByRoleId(@Param("roleId")String roleId);

}
