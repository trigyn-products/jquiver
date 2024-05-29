package com.trigyn.jws.dynamicform.dao;

public final class QueryStore {

	private QueryStore() {

	}

	public static final String JPA_QUERY_TO_GET_MANUAL_ENTRY_DETAILS_BY_MANUAL_TYPE = "SELECT med FROM ManualEntryDetails AS med WHERE med.manualId = :manualType";

	public static final String JPA_QUERY_TO_GET_HELP_MANUAL_ENTRY_DETAILS = "SELECT med FROM ManualEntryDetails AS med";

	public static final String JPA_QUERY_TO_GET_HELP_MANUAL_ENTRY_PARENT_NODES = "SELECT med FROM ManualEntryDetails AS med where med.parentId is null And med.manualId=:manualId ORDER BY sortIndex";

	public static final String JPA_QUERY_TO_GET_HELP_MANUAL_ENTRY_DETAILS_BY_NODEID = "SELECT med FROM ManualEntryDetails AS med where med.parentId =:nodeId And med.manualId=:manualId ORDER BY sortIndex";

	public static final String JPA_QUERY_TO_GET_COUNT = "SELECT count(*) FROM ManualEntryDetails AS med where med.parentId =:nodeId";

	public static final String JPA_QUERY_TO_GET_CONTENT = "SELECT entryContent FROM ManualEntryDetails AS med where med.manualEntryId =:nodeId";

	public static final String JPA_QUERY_TO_GET_FILTERED_DATA = "SELECT med FROM ManualEntryDetails AS med where (med.entryContent LIKE %:searchText% OR  med.entryName LIKE %:searchText%) AND med.manualId =:manualId";

}
