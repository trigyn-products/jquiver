package com.trigyn.jws.usermanagement.entities;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "jws_confirmation_token")
public class JwsConfirmationToken {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
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
