package com.trigyn.jws.usermanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.usermanagement.entities.JwsMasterModules;

@Repository
public interface JwsMasterModulesRepository extends JpaRepository<JwsMasterModules, String> {

	JwsMasterModules findBymoduleName(String moduleName);

	@Query(" SELECT jmm FROM JwsMasterModules jmm WHERE isPermSupported=:isPermSupported order by sequence")
	List<JwsMasterModules> findAllModulesForPermission(Integer isPermSupported);

	@Query(" SELECT jmm FROM JwsMasterModules jmm WHERE isEntityPermSupported=:isEntityPermSupported order by moduleName")
	List<JwsMasterModules> findAllModulesForEntityLevelPermission(Integer isEntityPermSupported);

	@Query(" SELECT jmm FROM JwsMasterModules jmm WHERE isImpExpSupported=:isImpExpSupported order by sequence")
	List<JwsMasterModules> findAllModulesForImportExport(Integer isImpExpSupported);

}
