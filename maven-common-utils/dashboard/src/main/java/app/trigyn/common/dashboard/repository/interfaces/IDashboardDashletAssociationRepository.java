package app.trigyn.common.dashboard.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.trigyn.common.dashboard.entities.DashboardDashletAssociation;
import app.trigyn.common.dashboard.entities.DashboardDashletAssociationPK;

@Repository
public interface IDashboardDashletAssociationRepository extends JpaRepository<DashboardDashletAssociation, DashboardDashletAssociationPK>{

}
