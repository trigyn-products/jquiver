package com.trigyn.jws.dashboard.repository.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dashboard.dao.QueryStore;
import com.trigyn.jws.dashboard.entities.ContextMaster;
import com.trigyn.jws.dashboard.vo.ContextMasterVO;

@Repository
public interface IContextMasterRepository extends JpaRepository<ContextMaster, String> {

	@Query(QueryStore.JPA_QUERY_TO_GET_ALL_CONTEXT)
	List<ContextMasterVO> findAllContext();
}
