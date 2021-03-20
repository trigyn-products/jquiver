package com.trigyn.jws.resourcebundle.dao;

public final class QueryStore {

	public static final String		JPA_QUERY_TO_GET_MESSAGE_DETAILS_BY_RESOURCE_KEY	= (" SELECT new com.trigyn.jws.resourcebundle.vo.ResourceBundleVO ")
			+ (" (rb.id.languageId AS languageId, rb.id.resourceKey AS resourceKey, rb.text AS text) ")
			+ (" FROM ResourceBundle AS rb WHERE rb.id.resourceKey =:resourceBundleKey ");

	public static final String		JPA_QUERY_TO_GET_ALL_LANGUAGES						= (" SELECT new com.trigyn.jws.resourcebundle.vo.LanguageVO ")
			+ (" (lg.languageId AS languageId, lg.languageName AS languageName, lg.languageCode AS localeId) ")
			+ (" FROM Language AS lg WHERE lg.isDeleted = :isDeleted ");

	public static final String		JPA_QUERY_TO_GET_MESSAGE_BY_LANGUAGE_CODE			= " SELECT COALESCE(rb.text, "
			+ " (SELECT rbd.text AS defaultText" + " FROM ResourceBundle AS rbd "
			+ " INNER JOIN rbd.language AS lgd ON lgd.languageCode = :defaultLocaleCode AND lgd.isDeleted = :isDeleted "
			+ " WHERE rbd.id.resourceKey = :resourceBundleKey)) AS text " + " FROM Language AS lg "
			+ " LEFT OUTER JOIN lg.resourceBundles AS rb ON rb.id.resourceKey = :resourceBundleKey "
			+ " WHERE lg.languageCode = :localeCode AND lg.isDeleted = :isDeleted ";

	public static final String		JPA_QUERY_TO_CHECK_RESOURCE_KEY_EXIST				= ""
			+ " SELECT rb.id.resourceKey FROM ResourceBundle AS rb " + " WHERE rb.id.resourceKey = :resourceBundleKey "
			+ " GROUP BY rb.id.resourceKey ";

	protected static final String	SQL_QUERY_TO_GET_MESSAGE_DETAILS_BY_RESOURCE_KEY	= new StringBuilder(
			" SELECT language_id AS languageId, text AS text, resource_Key AS resourceKey ")
					.append(" FROM jq_resource_bundle WHERE resource_Key =:resourceKey ").toString();

	protected static final String	SQL_QUERY_TO_GET_MESSAGE_DETAILS					= new StringBuilder(
			" SELECT language_id AS languageId, resource_key AS resourceKey, text ")
					.append(" FROM jq_resource_bundle WHERE language_id=? AND resource_key=?").toString();

	protected static final String	SQL_QUERY_TO_DELETE_RESOURCE_BUNDLE					= new StringBuilder(
			" DELETE FROM jq_resource_bundle WHERE text=? AND language_id=? AND resource_key=?").toString();

	protected static final String	SQL_QUERY_TO_INSERT_RESOURCE_BUNDLE					= new StringBuilder(
			" INSERT INTO jq_resource_bundle(language_id,resource_key,text) VALUES (?,?,?)").toString();

	protected static final String	SQL_QUERY_TO_UPDATE_RESOURCE_BUNDLE					= new StringBuilder(
			"UPDATE jq_resource_bundle SET text=? WHERE resource_key=? AND language_id=?").toString();

	protected static final String	SQL_QUERY_TO_GET_LANGAUGE_ID_BY_NAME				= new StringBuilder(
			" SELECT language_id AS languageId FROM jq_language WHERE language_name = ? ").toString();

	protected static final String	SQL_QUERY_TO_GET_LANGAUGE_DETAILS					= new StringBuilder(
			" SELECT language_name AS languageName, language_id AS languageId FROM jq_language ").toString();

}