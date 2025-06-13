package com.trigyn.jws.formio.storages;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.trigyn.jws.formio.utils.FormioFileToFileStorageEntityAdapter;

public class FormioBase64FileStorage implements FileStorage {

	@Override
	public void store(FileStorageEntity fileStorageEntity) {
	}

	@Override
	public FileStorageEntity retrieve(String id) {
		return new FormioFileToFileStorageEntityAdapter(id, JsonNodeFactory.instance.objectNode());
	}

	@Override
	public String getDownloadUrlPrefix() {
		return "";
	}

}
