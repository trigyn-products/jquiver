package com.trigyn.jws.resourcebundle.dao;

public final class QueryStore {

	public static final String	JPA_QUERY_TO_GET_MESSAGE_DETAILS_BY_RESOURCE_KEY	= (" SELECT new com.trigyn.jws.resourcebundle.vo.ResourceBundleVO ")
			+ (" (rb.id.languageId AS languageId, rb.id.resourceKey AS resourceKey, rb.text AS text,rb.createdBy as createdBy,rb.createdDate as createdDate) ")
			+ (" FROM ResourceBundle AS rb WHERE rb.id.resourceKey =:resourceBundleKey ");

	public static final String	JPA_QUERY_TO_GET_ALL_LANGUAGES						= (" SELECT new com.trigyn.jws.resourcebundle.vo.LanguageVO ")
			+ (" (lg.languageId AS languageId, lg.languageName AS languageName, lg.languageCode AS localeId) ")
			+ (" FROM Language AS lg WHERE lg.isDeleted = :isDeleted ");

	public static final String	JPA_QUERY_TO_GET_MESSAGE_BY_LANGUAGE_CODE			= " SELECT COALESCE(rb.text, "
			+ " (SELECT rbd.text AS defaultText" + " FROM ResourceBundle AS rbd "
			+ " INNER JOIN rbd.language AS lgd ON lgd.languageCode = :defaultLocaleCode AND lgd.isDeleted = :isDeleted "
			+ " WHERE rbd.id.resourceKey = :resourceBundleKey)) AS text " + " FROM Language AS lg "
			+ " LEFT OUTER JOIN lg.resourceBundles AS rb ON rb.id.resourceKey = :resourceBundleKey "
			+ " WHERE lg.languageCode = :localeCode AND lg.isDeleted = :isDeleted ";

	public static final String	JPA_QUERY_TO_CHECK_RESOURCE_KEY_EXIST				= ""
			+ " SELECT rb.id.resourceKey FROM ResourceBundle AS rb " + " WHERE rb.id.resourceKey = :resourceBundleKey "
			+ " GROUP BY rb.id.resourceKey ";

	public static final String	JPA_QUERY_TO_GET_TEXT_BY_KEY_AND_LANGUAGE_ID		= " SELECT COALESCE(rb.text, "
			+ " (SELECT rbd.text AS defaultText" + " FROM ResourceBundle AS rbd "
			+ " INNER JOIN rbd.language AS lgd ON lgd.languageId = :defaultLanguageId AND lgd.isDeleted = :isDeleted "
			+ " WHERE rbd.id.resourceKey = :resourceBundleKey)) AS text " + " FROM Language AS lg "
			+ " LEFT OUTER JOIN lg.resourceBundles AS rb ON rb.id.resourceKey = :resourceBundleKey "
			+ " WHERE lg.languageId = :languageId AND lg.isDeleted = :isDeleted ";

	public static final String	SQL_QUERY_TO_INSERT_RESOURCE_BUNDLE					= " INSERT IGNORE INTO jq_resource_bundle VALUES "
			+ "(:resourceBundleKey, :languageId, :text, :isCustomUpdated)";
	
	public static final String	JPA_QUERY_TO_CHECK_LANGUAGES						= " SELECT count(*) from  Language AS lg WHERE lg.languageCode in (:langcodes) and isDeleted=0";

}