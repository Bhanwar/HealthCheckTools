package com.snapdeal.healthcheck.app.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.snapdeal.healthcheck.app.dao.AbstractDao;
import com.snapdeal.healthcheck.app.dao.ComponentDetailsDAO;
import com.snapdeal.healthcheck.app.model.ComponentDetails;

@Repository("endpointDetailsDao")
public class ComponentDetailsDAOImpl extends AbstractDao implements ComponentDetailsDAO{

	@SuppressWarnings("unchecked")
	@Override
	public List<ComponentDetails> getAllEndpointDetails() {
		Criteria criteria = getSession().createCriteria(ComponentDetails.class);
		System.out.println("Inside DAO");
		System.out.println(criteria.list().size());
		return (List<ComponentDetails>) criteria.list();
	}

	@Override
	public ComponentDetails getEndpointDetails(String keyName) {
		Criteria criteria = getSession().createCriteria(ComponentDetails.class);
		criteria.add(Restrictions.eq("keyName", keyName));
		return (ComponentDetails) criteria.uniqueResult();
	}

}
