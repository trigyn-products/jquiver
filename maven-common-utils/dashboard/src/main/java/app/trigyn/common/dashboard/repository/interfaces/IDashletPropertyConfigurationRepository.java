package app.trigyn.common.dashboard.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.trigyn.common.dashboard.entities.DashletPropertyConfiguration;
import app.trigyn.common.dashboard.entities.DashletPropertyConfigurationPK;

@Repository
public interface IDashletPropertyConfigurationRepository extends JpaRepository<DashletPropertyConfiguration, DashletPropertyConfigurationPK>{

}
