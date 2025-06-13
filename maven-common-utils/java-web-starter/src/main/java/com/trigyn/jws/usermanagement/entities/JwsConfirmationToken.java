package com.trigyn.jws.usermanagement.entities;

import java.util.Date;
import java.util.UUID;

import com.trigyn.jws.dbutils.configurations.UUIDEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@EntityListeners(value = { UUIDEntityListener.class })
@Table(name = "jq_confirmation_token")
public class JwsConfirmationToken {

	@Id
	@Column(name = "token_id")
	private String	tokenId				= null;

	@Column(name = "confirmation_token")
	private String	confirmationToken	= null;

	@Column(name = "created_date")
	private Date	createdDate			= null;

	@Column(name = "user_id")
	private String	userId				= null;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false, insertable = false, updatable = false)
	private JwsUser	user				= null;

	public JwsConfirmationToken(JwsUser user) {
		userId				= user.getUserId();
		createdDate			= new Date();
		confirmationToken	= UUID.randomUUID().toString();
	}

	public JwsConfirmationToken() {

	}

	public String getConfirmationToken() {
		return confirmationToken;
	}

	public void setConfirmationToken(String confirmationToken) {
		this.confirmationToken = confirmationToken;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public JwsUser getUserRegistration() {
		return user;
	}

	public void setUserRegistration(JwsUser user) {
		this.user = user;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
