package com.snapdeal.healthcheck.app.configurables;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GetApiConfigValues {

	@Value("${omsadmin_subordercode}")
	private String omsAdminSuborderCode;
	@Value("${poms_subordercode}")
	private String pomsSuborderCode;
	@Value("${sellerst_sellerCode}")
	private String sellerSTSellerCode;
	
	public String getOmsAdminSuborderCode() {
		return omsAdminSuborderCode;
	}
	public String getPomsSuborderCode() {
		return pomsSuborderCode;
	}
	public String getSellerSTSellerCode() {
		return sellerSTSellerCode;
	}
}
