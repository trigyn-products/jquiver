package com.trigyn.jws.dynamicform.dao;

public final class QueryStore {

	private QueryStore() {

	}

	public static final String QUERY_TO_GET_FILE_DETAILS = "SELECT fu FROM FileUpload AS fu WHERE fu.fileUploadId IN ( :fileIdList )";
}
