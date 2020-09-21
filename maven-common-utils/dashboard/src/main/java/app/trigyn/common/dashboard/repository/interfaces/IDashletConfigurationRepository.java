package app.trigyn.common.dashboard.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.trigyn.common.dashboard.entities.DashletConfiguration;
import app.trigyn.common.dashboard.entities.DashletConfigurationPK;

@Repository
public interface IDashletConfigurationRepository extends JpaRepository<DashletConfiguration, DashletConfigurationPK>{

}
