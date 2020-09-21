package app.trigyn.core.webstarter.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.trigyn.core.webstarter.entities.MasterModule;

@Repository
public interface IMasterModuleDAO  extends  JpaRepository<MasterModule,String>{

	
	
}
