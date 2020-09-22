package com.trigyn.jws.dashboard.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dashboard.entities.DashletConfiguration;
import com.trigyn.jws.dashboard.entities.DashletConfigurationPK;

@Repository
public interface IDashletConfigurationRepository extends JpaRepository<DashletConfiguration, DashletConfigurationPK>{

}
