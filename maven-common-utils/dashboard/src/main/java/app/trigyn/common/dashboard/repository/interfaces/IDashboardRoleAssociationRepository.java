package app.trigyn.common.dashboard.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.trigyn.common.dashboard.entities.DashboardRoleAssociation;
import app.trigyn.common.dashboard.entities.DashboardRoleAssociationPK;

@Repository
public interface IDashboardRoleAssociationRepository extends JpaRepository<DashboardRoleAssociation, DashboardRoleAssociationPK>{

}
