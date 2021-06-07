package com.trigyn.jws.dynarest.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "jq_encryption_algorithms_lookup")
public class EncryptionAlgorithms {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "custom-identity-generator")
	@GenericGenerator(name = "custom-identity-generator", strategy = "com.trigyn.jws.dbutils.configurations.CustomIdentityGenerator")
	@Column(name = "encryption_algo_id")
	private Integer	encryptionAlgorithmId	= null;

	@Column(name = "encryption_algo_name")
	private String	encryptionAlgorithmName	= null;

	public Integer getEncryptionAlgorithmId() {
		return encryptionAlgorithmId;
	}

	public void setEncryptionAlgorithmId(Integer encryptionAlgorithmId) {
		this.encryptionAlgorithmId = encryptionAlgorithmId;
	}

	public String getEncryptionAlgorithmName() {
		return encryptionAlgorithmName;
	}

	public void setEncryptionAlgorithmName(String encryptionAlgorithmName) {
		this.encryptionAlgorithmName = encryptionAlgorithmName;
	}
}
