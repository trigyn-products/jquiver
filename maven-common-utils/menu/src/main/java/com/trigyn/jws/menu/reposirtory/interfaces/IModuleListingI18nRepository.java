package com.trigyn.jws.menu.reposirtory.interfaces;

import com.trigyn.jws.menu.entities.ModuleListingI18n;
import com.trigyn.jws.menu.entities.ModuleListingI18nPK;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IModuleListingI18nRepository extends JpaRepository<ModuleListingI18n, ModuleListingI18nPK>{
	
}
