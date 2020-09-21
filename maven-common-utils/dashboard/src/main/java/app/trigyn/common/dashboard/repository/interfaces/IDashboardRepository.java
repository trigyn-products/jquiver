package app.trigyn.common.dashboard.repository.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.trigyn.common.dashboard.entities.Dashboard;

@Repository
public interface IDashboardRepository extends JpaRepository<Dashboard, String>{

}
