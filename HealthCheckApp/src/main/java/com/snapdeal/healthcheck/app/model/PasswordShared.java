package com.snapdeal.healthcheck.app.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "pwdShared")
public class PasswordShared {

	@Id
	private String id;

	private String componentName;
	
	public String getId() {
		return id;
	}
	public String getComponentName() {
		return componentName;
	}
	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}
}
