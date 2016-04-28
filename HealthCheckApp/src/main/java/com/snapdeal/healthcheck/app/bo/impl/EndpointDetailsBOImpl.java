package com.snapdeal.healthcheck.app.bo.impl;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snapdeal.healthcheck.app.bo.EndpointDetailsBO;
import com.snapdeal.healthcheck.app.dao.EndpointDetailsDAO;
import com.snapdeal.healthcheck.app.model.EndpointDetails;

@Service("endpointDetailsService")
@Transactional
public class EndpointDetailsBOImpl implements EndpointDetailsBO{

	@Autowired
	EndpointDetailsDAO dao;
	
	@Override
	public List<EndpointDetails> getAllEndpointDetails() {
		return dao.getAllEndpointDetails();
	}

	@Override
	public EndpointDetails getEndpointDetails(String keyName) {
		return dao.getEndpointDetails(keyName);
	}


}
