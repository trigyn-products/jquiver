package com.trigyn.jws.dynamicform.dao;

public final class QueryStore {

	private QueryStore() {

	}

	public static final String JPA_QUERY_TO_GET_MANUAL_ENTRY_DETAILS_BY_MANUAL_TYPE = " WITH recursive hierarchy AS (" 
			+ "              SELECT p1.*"
			+ "              FROM   jq_manual_entry p1 "
			+ "              WHERE  parent_id IS NULL "
			+ "              UNION ALL "
			+ "              SELECT     c.* "
			+ "              FROM       jq_manual_entry c "
			+ "              INNER JOIN hierarchy p "
			+ "              ON         c.parent_id = p.manual_entry_id )"
            + "SELECT   h.* "
			+ "FROM     hierarchy h "
			+ "WHERE    h.manual_id=:manualType ";
	public static final String JPA_QUERY_TO_GET_HELP_MANUAL_ENTRY_DETAILS = "SELECT med FROM ManualEntryDetails AS med";

	public static final String JPA_QUERY_TO_GET_HELP_MANUAL_ENTRY_PARENT_NODES = "SELECT med FROM ManualEntryDetails AS med where med.parentId is null And med.manualId=:manualId ORDER BY sortIndex";

	public static final String JPA_QUERY_TO_GET_HELP_MANUAL_ENTRY_DETAILS_BY_Parent_AND_MANUAL_ID = "SELECT med FROM ManualEntryDetails AS med where med.parentId =:parentId And med.manualId=:manualId ORDER BY sortIndex";

	public static final String JPA_QUERY_TO_GET_COUNT = "SELECT count(*) FROM ManualEntryDetails AS med where med.parentId =:nodeId";

	public static final String JPA_QUERY_TO_GET_CONTENT = "SELECT entryContent FROM ManualEntryDetails AS med where med.manualEntryId =:nodeId";

	public static final String JPA_QUERY_TO_GET_FILTERED_DATA = "SELECT med FROM ManualEntryDetails AS med where (med.entryContent LIKE %:searchText% OR  med.entryName LIKE %:searchText%) AND med.manualId =:manualId";

}
