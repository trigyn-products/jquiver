package com.trigyn.jws.dbutils.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.trigyn.jws.sciptlibrary.entities.ScriptLibraryConnection;

public interface ScriptLibraryConnRepository extends JpaRepository<ScriptLibraryConnection, String> {
	
	@Query(" FROM ScriptLibraryConnection WHERE entityId=:entityId AND moduletypeId=:moduleId GROUP BY scriptLibId")
	List<ScriptLibraryConnection> getScriptLibraryConnectionIds(@Param("entityId") String entityId, @Param("moduleId") String moduleId);
	
	@Query("FROM ScriptLibraryConnection " +
		       "WHERE entityId IN (:entityIds) AND moduletypeId = :moduleId " +
		       "GROUP BY scriptLibId")
		List<ScriptLibraryConnection> getScriptLibraryConnIds(@Param("entityIds") List<String> entityIds,
		                                                           @Param("moduleId") String moduleId);

}

