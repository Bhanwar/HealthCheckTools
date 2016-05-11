package com.snapdeal.healthcheck.app.bo.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.snapdeal.healthcheck.app.bo.AdminBO;
import com.snapdeal.healthcheck.app.dao.AdminDAO;
import com.snapdeal.healthcheck.app.model.Administrator;

@Service("adminService")
@Transactional
public class AdminBOImpl implements AdminBO{
	
	@Autowired
	AdminDAO dao;

	@Override
	public Administrator getAdmin() {
		return dao.getAdmin();
	}

}
