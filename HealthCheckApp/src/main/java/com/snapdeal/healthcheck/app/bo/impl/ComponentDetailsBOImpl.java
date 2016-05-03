package com.snapdeal.healthcheck.app.bo.impl;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snapdeal.healthcheck.app.bo.ComponentDetailsBO;
import com.snapdeal.healthcheck.app.dao.ComponentDetailsDAO;
import com.snapdeal.healthcheck.app.model.ComponentDetails;

@Service("endpointDetailsService")
@Transactional
public class ComponentDetailsBOImpl implements ComponentDetailsBO{

	@Autowired
	ComponentDetailsDAO dao;
	
	@Override
	public List<ComponentDetails> getAllComponentDetails() {
		return dao.getAllComponentDetails();
	}

	@Override
	public ComponentDetails getComponentDetails(String compName) {
		return dao.getComponentDetails(compName);
	}


}
