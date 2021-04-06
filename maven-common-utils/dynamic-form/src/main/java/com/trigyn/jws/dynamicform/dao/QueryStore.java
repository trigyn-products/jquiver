package com.trigyn.jws.dynamicform.dao;

public final class QueryStore {

	private QueryStore() {

	}

	public static final String	JPA_QUERY_TO_GET_FILE_DETAILS_ID_BY_FILE_UPLOAD_ID			= "SELECT fu FROM FileUpload AS fu WHERE fu.fileUploadId = :fileUploadId";

	public static final String	JPA_QUERY_TO_GET_ALL_FILE_DETAILS_BY_FILE_UPLOAD_ID			= "SELECT fu FROM FileUpload AS fu WHERE fu.fileUploadId IN ( :fileUploadIdList ) ORDER BY fu.lastUpdatedTs DESC, fu.originalFileName ASC ";

	public static final String	JPA_QUERY_TO_GET_FILE_DETAILS_BY_FILE_BIN_ID_AND_ASSOC_ID	= "SELECT fu FROM FileUpload AS fu WHERE fu.fileBinId = :fileBinId AND fu.fileAssociationId = :fileAssociationId ORDER BY fu.lastUpdatedTs DESC, fu.originalFileName ASC";

	public static final String	JPA_QUERY_TO_GET_FILE_DETAILS_BY_FILE_ASSOC_ID				= "SELECT fu FROM FileUpload AS fu WHERE fu.fileAssociationId = :fileAssociationId ORDER BY fu.lastUpdatedTs DESC, fu.originalFileName ASC";

	public static final String	JPA_QUERY_TO_GET_FILE_DETAILS_BY_FILE_BIN_ID				= "SELECT fu FROM FileUpload AS fu WHERE fu.fileBinId = :fileBinId ORDER BY fu.lastUpdatedTs DESC, fu.originalFileName ASC";

	public static final String	JPA_QUERY_TO_GET_MANUAL_ENTRY_DETAILS_BY_MANUAL_TYPE		= "SELECT med FROM ManualEntryDetails AS med WHERE med.manualType = :manualType";

}
