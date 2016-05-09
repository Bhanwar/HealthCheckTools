package com.snapdeal.healthcheck.app.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="healtCheckResults")
public class ConnTimedOutComp {

	@Id
	private String id;
	
	private String componentName;
	private Date execDate;

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public Date getExecDate() {
		return execDate;
	}

	public void setExecDate(Date execDate) {
		this.execDate = execDate;
	}
}
