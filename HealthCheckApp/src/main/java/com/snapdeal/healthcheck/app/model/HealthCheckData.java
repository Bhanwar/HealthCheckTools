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
	private String sellerToolsEndPoint;
	private String snsEndPoint;
	private String ucmsTemplateEndPoint;
	private String ucmsProcessorEndPoint;
	private String shipFarEndPoint;
	private String omsAdminEndPoint;
	private String pomsEndPoint;
	private String qnaEndPoint;
	private String sfMobileEndPoint;
	private String kamEndPoint;
	private String apiGatewayEndPoint;

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

	public String getSellerToolsEndPoint() {
		return sellerToolsEndPoint;
	}

	public String getSNSEndPoint() {
		return snsEndPoint;
	}

	public String getUCMSTemplateEndPoint() {
		return ucmsTemplateEndPoint;
	}

	public String getUcmsProcessorEndPoint() {
		return ucmsProcessorEndPoint;
	}

	public String getShipFarEndPoint() {
		return shipFarEndPoint;
	}

	public String getOMSAdminEndPoint() {
		return omsAdminEndPoint;
	}

	public String getPomsEndPoint() {
		return pomsEndPoint;
	}

	public String getQnaEndPoint() {
		return qnaEndPoint;
	}

	public String getSFMobileEndPoint() {
		return sfMobileEndPoint;
	}
	
	public String getKamEndPoint() {
		return kamEndPoint;
	}
	
	public String getApiGatewayEndPoint() {
		return apiGatewayEndPoint;
	}

	public void get() {
		List<ComponentDetails> listDetails = endpointDetails.getAllComponentDetails();
		for(ComponentDetails detail : listDetails) {
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
			else if(detail.getComponentName().equals(Component.SELLERTOOLS.code()))
				this.sellerToolsEndPoint = detail.getEndpoint();
			else if(detail.getComponentName().equals(Component.SNS.code()))
				this.snsEndPoint = detail.getEndpoint();
			else if(detail.getComponentName().equals(Component.UCMSTE.code()))
				this.ucmsTemplateEndPoint = detail.getEndpoint();
			else if(detail.getComponentName().equals(Component.UCMSP.code()))
				this.ucmsProcessorEndPoint = detail.getEndpoint();
			else if(detail.getComponentName().equals(Component.SHIPFAR.code()))
				this.shipFarEndPoint = detail.getEndpoint();
			else if(detail.getComponentName().equals(Component.OMSADMIN.code()))
				this.omsAdminEndPoint = detail.getEndpoint();
			else if(detail.getComponentName().equals(Component.POMS.code()))
				this.pomsEndPoint = detail.getEndpoint();
			else if(detail.getComponentName().equals(Component.QNA.code()))
				this.qnaEndPoint = detail.getEndpoint();
			else if(detail.getComponentName().equals(Component.SFMOBILE.code()))
				this.sfMobileEndPoint = detail.getEndpoint();
			else if(detail.getComponentName().equals(Component.KAM.code()))
				this.kamEndPoint = detail.getEndpoint();
			else if(detail.getComponentName().equals(Component.APIGATEWAY.code()))
				this.apiGatewayEndPoint = detail.getEndpoint();
		}
	}
}