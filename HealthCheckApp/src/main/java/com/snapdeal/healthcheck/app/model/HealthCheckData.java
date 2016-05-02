package com.snapdeal.healthcheck.app.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.snapdeal.healthcheck.app.bo.ComponentDetailsBO;
import com.snapdeal.healthcheck.app.enums.Component;

public class HealthCheckData {

	@Autowired
	private ComponentDetailsBO endpointDetails;
	
	private String camsEndPoint;
	private String cocofsEndPoint;
	private String ipmsEndPoint;
	private String omsEndPoint;
	private String opmsEndPoint;
	private String opsEndPoint;
	private String promoEndPoint;
	private String scoreEndPoint;
	private String erasEndPoint;
	private String mobapiEndPoint;
	private String rnrEndPoint;
	private String searchEndPoint;
	private String umsEndPoint;
	private String cartEndPoint;
	private String spmsEndPoint;
	private String scoreAdminEndPoint;
	private String filmsUIEndPoint;
	
	public String getPromoEndPoint() {
		return promoEndPoint;
	}

	public String getCamsEndPoint() {
		return camsEndPoint;
	}

	public String getCocofsEndPoint() {
		return cocofsEndPoint;
	}

	public String getIpmsEndPoint() {
		return ipmsEndPoint;
	}

	public String getOmsEndPoint() {
		return omsEndPoint;
	}

	public String getOpmsEndPoint() {
		return opmsEndPoint;
	}

	public String getOpsEndPoint() {
		return opsEndPoint;
	}

	public String getScoreEndPoint() {
		return scoreEndPoint;
	}
	
	public String getErasEndPoint() {
		return erasEndPoint;
	}

	public String getMobApiEndPoint() {
		return mobapiEndPoint;
	}

	public String getRNREndPoint() {
		return rnrEndPoint;
	}

	public String getSearchEndPoint() {
		return searchEndPoint;
	}

	public String getUmsEndPoint() {
		return umsEndPoint;
	}

	public String getCartEndPoint() {
		return cartEndPoint;
	}

	public String getSpmsEndPoint() {
		return spmsEndPoint;
	}

	public String getScoreAdminEndPoint() {
		return scoreAdminEndPoint;
	}
	
	public String getFilmsUIEndPoint() {
		return filmsUIEndPoint;
	}

	public void get() {
		List<ComponentDetais> listDetails = endpointDetails.getAllEndpointDetails();
		for(ComponentDetais detail : listDetails) {
			if(detail.getComponentName().equals(Component.CAMS.code()))
				this.camsEndPoint = detail.getEndpoint();
			else if(detail.getComponentName().equals(Component.COCOFS.code()))
				this.cocofsEndPoint = detail.getEndpoint();
			else if(detail.getComponentName().equals(Component.IPMS.code()))
				this.ipmsEndPoint = detail.getEndpoint();
			else if(detail.getComponentName().equals(Component.OMS.code()))
				this.omsEndPoint = detail.getEndpoint();
			else if(detail.getComponentName().equals(Component.OPMS.code()))
				this.opmsEndPoint = detail.getEndpoint();
			else if(detail.getComponentName().equals(Component.OPS.code()))
				this.opsEndPoint = detail.getEndpoint();
			else if(detail.getComponentName().equals(Component.PROMO.code()))
				this.promoEndPoint = detail.getEndpoint();
			else if(detail.getComponentName().equals(Component.SCORE.code()))
				this.scoreEndPoint = detail.getEndpoint();
			else if(detail.getComponentName().equals(Component.ERAS.code()))
				this.erasEndPoint = detail.getEndpoint();
			else if(detail.getComponentName().equals(Component.MOBAPI.code()))
				this.mobapiEndPoint = detail.getEndpoint();
			else if(detail.getComponentName().equals(Component.RNR.code()))
				this.rnrEndPoint = detail.getEndpoint();
			else if(detail.getComponentName().equals(Component.SEARCH.code()))
				this.searchEndPoint = detail.getEndpoint();
			else if(detail.getComponentName().equals(Component.UMS.code()))
				this.umsEndPoint = detail.getEndpoint();
			else if(detail.getComponentName().equals(Component.CART.code()))
				this.cartEndPoint = detail.getEndpoint();
			else if(detail.getComponentName().equals(Component.SPMSPMNT.code()))
				this.spmsEndPoint = detail.getEndpoint();
			else if(detail.getComponentName().equals(Component.SCOREADMIN.code()))
				this.scoreAdminEndPoint = detail.getEndpoint();
			else if(detail.getComponentName().equals(Component.FILMS.code()))
				this.filmsUIEndPoint = detail.getEndpoint();
		}
	}
}