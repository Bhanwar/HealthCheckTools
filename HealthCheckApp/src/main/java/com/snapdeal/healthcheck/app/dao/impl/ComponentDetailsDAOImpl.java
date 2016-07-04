package com.snapdeal.healthcheck.app.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.snapdeal.healthcheck.app.dao.AbstractDao;
import com.snapdeal.healthcheck.app.dao.ComponentDetailsDAO;
import com.snapdeal.healthcheck.app.model.ComponentDetails;

@Repository("componentDetailsDao")
public class ComponentDetailsDAOImpl extends AbstractDao implements ComponentDetailsDAO{

	@SuppressWarnings("unchecked")
	@Override
	public List<ComponentDetails> getAllEnabledComponentDetails() {
		Criteria criteria = getSession().createCriteria(ComponentDetails.class);
		criteria.add(Restrictions.eq("enabled", true));
		return (List<ComponentDetails>) criteria.list();
	}

	@Override
	public ComponentDetails getComponentDetails(String compName) {
		Criteria criteria = getSession().createCriteria(ComponentDetails.class);
		criteria.add(Restrictions.eq("componentName", compName));
		return (ComponentDetails) criteria.uniqueResult();
	}

	@Override
	public void updateComponentDetails(ComponentDetails compDetail) {
		getSession().update(compDetail);
	}

	@Override
	public void saveComponentDetails(ComponentDetails compDetail) {
		persist(compDetail);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ComponentDetails> getAllComponentDetails() {
		Criteria criteria = getSession().createCriteria(ComponentDetails.class);
		return (List<ComponentDetails>) criteria.list();
	}

}
