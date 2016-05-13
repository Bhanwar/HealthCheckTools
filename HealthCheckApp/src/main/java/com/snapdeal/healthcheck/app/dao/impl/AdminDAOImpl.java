package com.snapdeal.healthcheck.app.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.snapdeal.healthcheck.app.dao.AbstractDao;
import com.snapdeal.healthcheck.app.dao.AdminDAO;
import com.snapdeal.healthcheck.app.model.Administrator;

@Repository("adminDao")
public class AdminDAOImpl extends AbstractDao implements AdminDAO{

	@Override
	public Administrator getAdmin() {
		Criteria criteria = getSession().createCriteria(Administrator.class);
		criteria.add(Restrictions.eq("name", "Admin"));
		return (Administrator) criteria.uniqueResult();
	}

}
