package app.trigyn.core.menu.reposirtory.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.trigyn.core.menu.entities.ModuleListingI18n;
import app.trigyn.core.menu.entities.ModuleListingI18nPK;

@Repository
public interface IModuleListingI18nRepository extends JpaRepository<ModuleListingI18n, ModuleListingI18nPK>{
	
}
