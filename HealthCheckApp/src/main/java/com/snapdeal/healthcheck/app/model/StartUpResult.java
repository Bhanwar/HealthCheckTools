package com.snapdeal.healthcheck.app.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="startUpResults")
public class StartUpResult {

	@Id
	private String id;
	
	private String componentName;
	private boolean serverUp;
	
	public String getComponentName() {
		return componentName;
	}
	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}
	public boolean isServerUp() {
		return serverUp;
	}
	public void setServerUp(boolean serverUp) {
		this.serverUp = serverUp;
	}
	@Override
	public String toString() {
		return "StartUpResult [componentName=" + componentName + ", serverUp=" + serverUp + "]";
	}
	
}