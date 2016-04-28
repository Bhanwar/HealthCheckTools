package com.snapdeal.healthcheck.app.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.snapdeal.healthcheck.app.dao.AbstractDao;
import com.snapdeal.healthcheck.app.dao.EndpointDetailsDAO;
import com.snapdeal.healthcheck.app.model.EndpointDetails;

@Repository("endpointDetailsDao")
public class EndpointDetailsDAOImpl extends AbstractDao implements EndpointDetailsDAO{

	@SuppressWarnings("unchecked")
	@Override
	public List<EndpointDetails> getAllEndpointDetails() {
		Criteria criteria = getSession().createCriteria(EndpointDetails.class);
		System.out.println("Inside DAO");
		System.out.println(criteria.list().size());
		return (List<EndpointDetails>) criteria.list();
	}

	@Override
	public EndpointDetails getEndpointDetails(String keyName) {
		Criteria criteria = getSession().createCriteria(EndpointDetails.class);
		criteria.add(Restrictions.eq("keyName", keyName));
		return (EndpointDetails) criteria.uniqueResult();
	}

}
