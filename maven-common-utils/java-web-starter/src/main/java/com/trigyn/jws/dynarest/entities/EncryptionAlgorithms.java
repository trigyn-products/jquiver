package com.trigyn.jws.dynarest.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "jq_encryption_algorithms_lookup")
public class EncryptionAlgorithms {

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
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
