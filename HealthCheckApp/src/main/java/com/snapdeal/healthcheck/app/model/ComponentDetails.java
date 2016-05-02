package com.snapdeal.healthcheck.app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "components")
public class ComponentDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "component_name")
	private String componentName;
	
	@Column(name = "qm_spoc")
	private String qmSpoc;
	
	@Column(name = "qa_spoc")
	private String qaSpoc;
	
	@Column(name = "endpoint")
	private String endpoint;
	
	@Column(name = "authkey")
	private String authKey;
	
	@Column(name = "created")
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	@Column(name = "updated")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updated;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAuthKey() {
		return authKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public String getQmSpoc() {
		return qmSpoc;
	}

	public void setQmSpoc(String qmSpoc) {
		this.qmSpoc = qmSpoc;
	}

	public String getQaSpoc() {
		return qaSpoc;
	}

	public void setQaSpoc(String qaSpoc) {
		this.qaSpoc = qaSpoc;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}
}
