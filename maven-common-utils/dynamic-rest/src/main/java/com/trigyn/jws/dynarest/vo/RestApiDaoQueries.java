package com.trigyn.jws.dynarest.vo;

import java.util.Objects;

public class RestApiDaoQueries {

	private String	jwsDaoQueryTemplate		= null;

	private String	jwsResultVariableName	= null;

	private Integer	jwsQuerySequence		= null;

	private Integer	queryType				= null;

	public RestApiDaoQueries() {
	}

	public RestApiDaoQueries(String jwsDaoQueryTemplate, String jwsResultVariableName, Integer jwsQuerySequence,
			Integer queryType) {
		this.jwsDaoQueryTemplate	= jwsDaoQueryTemplate;
		this.jwsResultVariableName	= jwsResultVariableName;
		this.jwsQuerySequence		= jwsQuerySequence;
		this.queryType				= queryType;
	}

	public String getJwsDaoQueryTemplate() {
		return this.jwsDaoQueryTemplate;
	}

	public void setJwsDaoQueryTemplate(String jwsDaoQueryTemplate) {
		this.jwsDaoQueryTemplate = jwsDaoQueryTemplate;
	}

	public String getJwsResultVariableName() {
		return this.jwsResultVariableName;
	}

	public void setJwsResultVariableName(String jwsResultVariableName) {
		this.jwsResultVariableName = jwsResultVariableName;
	}

	public Integer getJwsQuerySequence() {
		return this.jwsQuerySequence;
	}

	public void setJwsQuerySequence(Integer jwsQuerySequence) {
		this.jwsQuerySequence = jwsQuerySequence;
	}

	public RestApiDaoQueries jwsDaoQueryTemplate(String jwsDaoQueryTemplate) {
		this.jwsDaoQueryTemplate = jwsDaoQueryTemplate;
		return this;
	}

	public RestApiDaoQueries jwsResultVariableName(String jwsResultVariableName) {
		this.jwsResultVariableName = jwsResultVariableName;
		return this;
	}

	public RestApiDaoQueries jwsQuerySequence(Integer jwsQuerySequence) {
		this.jwsQuerySequence = jwsQuerySequence;
		return this;
	}

	public Integer getQueryType() {
		return queryType;
	}

	public void setQueryType(Integer queryType) {
		this.queryType = queryType;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof RestApiDaoQueries)) {
			return false;
		}
		RestApiDaoQueries restApiDaoQueries = (RestApiDaoQueries) o;
		return Objects.equals(jwsDaoQueryTemplate, restApiDaoQueries.jwsDaoQueryTemplate)
				&& Objects.equals(jwsResultVariableName, restApiDaoQueries.jwsResultVariableName)
				&& Objects.equals(jwsQuerySequence, restApiDaoQueries.jwsQuerySequence);
	}

	@Override
	public int hashCode() {
		return Objects.hash(jwsDaoQueryTemplate, jwsResultVariableName, jwsQuerySequence);
	}

	@Override
	public String toString() {
		return "{" + " jwsDaoQueryTemplate='" + getJwsDaoQueryTemplate() + "'" + ", jwsResultVariableName='"
				+ getJwsResultVariableName() + "'" + ", jwsQuerySequence='" + getJwsQuerySequence() + "'" + "}";
	}

}
