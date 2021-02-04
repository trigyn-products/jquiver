package com.trigyn.jws.webstarter.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.webstarter.entities.MasterModule;

@Repository
public interface IMasterModuleDAO extends JpaRepository<MasterModule, String> {

}
