package com.trigyn.jws.dbutils.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.entities.ModuleListingI18n;
import com.trigyn.jws.dbutils.entities.ModuleListingI18nPK;

@Repository
public interface IModuleListingI18nRepository extends JpaRepository<ModuleListingI18n, ModuleListingI18nPK> {

}
