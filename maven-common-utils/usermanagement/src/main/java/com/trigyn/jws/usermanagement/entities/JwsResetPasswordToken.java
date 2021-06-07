package com.trigyn.jws.usermanagement.entities;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "jq_reset_password_token")
public class JwsResetPasswordToken {

	@Id
	@Column(name = "token_id")
	private String		tokenId				= null;

	@Column(name = "password_reset_url")
	private String		resetPasswordUrl	= null;

	@Column(name = "password_reset_gen_time", nullable = false)
	private Calendar	passwordResetTime;

	@Column(name = "is_reset_url_expired")
	private Boolean		isResetUrlExpired;

	@Column(name = "user_id")
	private String		userId				= null;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false, insertable = false, updatable = false)
	private JwsUser		user				= null;

	public JwsResetPasswordToken(String tokenId, String resetPasswordUrl, String userId, JwsUser user, Calendar passwordResetTime,
			Boolean isResetUrlExpired) {
		super();
		this.tokenId			= tokenId;
		this.resetPasswordUrl	= resetPasswordUrl;
		this.passwordResetTime	= passwordResetTime;
		this.userId				= userId;
		this.user				= user;
		this.isResetUrlExpired	= isResetUrlExpired;
	}

	public JwsResetPasswordToken() {
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getResetPasswordUrl() {
		return resetPasswordUrl;
	}

	public void setResetPasswordUrl(String resetPasswordUrl) {
		this.resetPasswordUrl = resetPasswordUrl;
	}

	public Calendar getPasswordResetTime() {
		return passwordResetTime;
	}

	public void setPasswordResetTime(Calendar passwordResetTime) {
		this.passwordResetTime = passwordResetTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public JwsUser getUserRegistration() {
		return user;
	}

	public void setUserRegistration(JwsUser user) {
		this.user = user;
	}

	public Boolean getIsResetUrlExpired() {
		return isResetUrlExpired;
	}

	public void setIsResetUrlExpired(Boolean isResetUrlExpired) {
		this.isResetUrlExpired = isResetUrlExpired;
	}

}
