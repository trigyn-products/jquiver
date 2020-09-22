package com.trigyn.jws.dashboard.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dashboard.entities.DashletPropertyConfiguration;
import com.trigyn.jws.dashboard.entities.DashletPropertyConfigurationPK;

@Repository
public interface IDashletPropertyConfigurationRepository extends JpaRepository<DashletPropertyConfiguration, DashletPropertyConfigurationPK>{

}
