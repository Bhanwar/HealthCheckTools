package com.snapdeal.healthcheck.app.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.snapdeal.healthcheck.app.dao.AbstractDao;
import com.snapdeal.healthcheck.app.dao.ComponentDetailsDAO;
import com.snapdeal.healthcheck.app.model.ComponentDetais;

@Repository("endpointDetailsDao")
public class ComponentDetailsDAOImpl extends AbstractDao implements ComponentDetailsDAO{

	@SuppressWarnings("unchecked")
	@Override
	public List<ComponentDetais> getAllEndpointDetails() {
		Criteria criteria = getSession().createCriteria(ComponentDetais.class);
		System.out.println("Inside DAO");
		System.out.println(criteria.list().size());
		return (List<ComponentDetais>) criteria.list();
	}

	@Override
	public ComponentDetais getEndpointDetails(String keyName) {
		Criteria criteria = getSession().createCriteria(ComponentDetais.class);
		criteria.add(Restrictions.eq("keyName", keyName));
		return (ComponentDetais) criteria.uniqueResult();
	}

}
