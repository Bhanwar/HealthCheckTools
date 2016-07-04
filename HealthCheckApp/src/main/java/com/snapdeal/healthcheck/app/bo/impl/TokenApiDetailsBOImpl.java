package com.snapdeal.healthcheck.app.bo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snapdeal.healthcheck.app.bo.TokenApiDetailsBO;
import com.snapdeal.healthcheck.app.dao.TokenApiDetailsDAO;
import com.snapdeal.healthcheck.app.model.TokenApiDetails;

@Service("tokenApiDetailsService")
@Transactional
public class TokenApiDetailsBOImpl implements TokenApiDetailsBO {

	@Autowired
	TokenApiDetailsDAO dao;

	@Override
	public TokenApiDetails getTokenApiDetails(String compName) {
		return dao.getTokenApiDetails(compName);
	}

	@Override
	public void updateTokenApiDetails(TokenApiDetails tokenApiDetail) {
		dao.updateTokenApiDetails(tokenApiDetail);
	}

	@Override
	public void saveTokenApiDetails(TokenApiDetails tokenApiDetail) {
		dao.saveTokenApiDetails(tokenApiDetail);
	}

	@Override
	public List<TokenApiDetails> getAllTokenApiDetails() {
		return dao.getAllTokenApiDetails();
	}
}
