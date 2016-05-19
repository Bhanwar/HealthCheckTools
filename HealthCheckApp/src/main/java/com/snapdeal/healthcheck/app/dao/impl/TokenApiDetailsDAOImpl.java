package com.snapdeal.healthcheck.app.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.snapdeal.healthcheck.app.dao.AbstractDao;
import com.snapdeal.healthcheck.app.dao.TokenApiDetailsDAO;
import com.snapdeal.healthcheck.app.model.TokenApiDetails;

@Repository("tokenApiDetailsDao")
public class TokenApiDetailsDAOImpl extends AbstractDao implements TokenApiDetailsDAO{

	@Override
	public TokenApiDetails getTokenApiDetails(String compName) {
		Criteria criteria = getSession().createCriteria(TokenApiDetails.class);
		criteria.add(Restrictions.eq("componentName", compName));
		return (TokenApiDetails) criteria.uniqueResult();
	}

	@Override
	public void updateTokenApiDetails(TokenApiDetails tokenApiDetail) {
		getSession().update(tokenApiDetail);
	}

	@Override
	public void saveTokenApiDetails(TokenApiDetails tokenApiDetail) {
		persist(tokenApiDetail);
	}
}
