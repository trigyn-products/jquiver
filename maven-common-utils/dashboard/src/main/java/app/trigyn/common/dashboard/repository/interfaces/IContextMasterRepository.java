package app.trigyn.common.dashboard.repository.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.trigyn.common.dashboard.dao.QueryStore;
import app.trigyn.common.dashboard.entities.ContextMaster;
import app.trigyn.common.dashboard.vo.ContextMasterVO;

@Repository
public interface IContextMasterRepository extends JpaRepository<ContextMaster, String>{

	@Query(QueryStore.JPA_QUERY_TO_GET_ALL_CONTEXT)
	List<ContextMasterVO> findAllContext();
}
