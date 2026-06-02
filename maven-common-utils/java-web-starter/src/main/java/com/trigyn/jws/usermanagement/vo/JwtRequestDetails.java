package com.trigyn.jws.usermanagement.vo;

public class JwtRequestDetails {
	private String token;
    private String requestId;

    public JwtRequestDetails(String token, String requestId) {
        this.token = token;
        this.requestId = requestId;
    }

    public String getToken() {
        return token;
    }

    public String getRequestId() {
        return requestId;
    }
}
