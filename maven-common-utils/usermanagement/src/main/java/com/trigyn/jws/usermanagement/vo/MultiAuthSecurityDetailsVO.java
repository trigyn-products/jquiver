package com.trigyn.jws.usermanagement.vo;

public class MultiAuthSecurityDetailsVO {

	private ConnectionDetailsJSONSpecification	connectionDetailsVO;

	private JwsAuthenticationTypeVO				authenticationTypeVO;

	public ConnectionDetailsJSONSpecification getConnectionDetailsVO() {
		return connectionDetailsVO;
	}

	public void setConnectionDetailsVO(ConnectionDetailsJSONSpecification connectionDetailsVO) {
		this.connectionDetailsVO = connectionDetailsVO;
	}

	public JwsAuthenticationTypeVO getAuthenticationTypeVO() {
		return authenticationTypeVO;
	}

	public void setAuthenticationTypeVO(JwsAuthenticationTypeVO authenticationTypeVO) {
		this.authenticationTypeVO = authenticationTypeVO;
	}

}
