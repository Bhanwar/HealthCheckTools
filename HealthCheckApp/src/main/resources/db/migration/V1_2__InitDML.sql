INSERT INTO `components` (`component_name`,`qm_spoc`,`qa_spoc`,`authkey`,`endpoint`,`health_check_api`,`health_check_api_call_type`,`health_check_api_resp`, `first_get_api`, `first_get_api_call_type`, `first_get_api_req_json`, `first_get_api_resp`, `second_get_api`, `second_get_api_call_type`, `second_get_api_req_json`, `second_get_api_resp`)
VALUES
('CaMS', 'ravinder.singh04@snapdeal.com','mandeep.chawla@snapdeal.com', uuid(), 'http://20.0.2.203:8080', '/service/catalog/healthCheck', 'GET', 'Looks Healthy', '/service/product/getMinPOGContentListByIdList', 'POST', '{"productOfferGroupIds":["2462","2622","2718"],"requestProtocol":"PROTOCOL_JSON","responseProtocol":"PROTOCOL_JSON"}', null, '/service/product/getPOGDetailById', 'POST', '{"productOfferGroupId":"13878","requestProtocol":"PROTOCOL_JSON","responseProtocol":"PROTOCOL_JSON"}', null),
('CART','mohit.bansal@snapdeal.com','ankita.agarwal@snapdeal.com', uuid(),'http://54.169.156.29:10230','/service/cartAPIService/cartHealthCheck?pwd=Snapdeal','GET','Authentication SUCCESS !!! We are UP :)', '/service/cartMwAPIService/getNonFrozenCartUsingEmail', 'POST', '{"email":"qwerty1@gmail.com","nextDayDeliveryEnabled":"true","pincode":"110020","sameDayDeliveryEnabled":"true","config":{"promoApplicable":"true","platform":"WEB"},"responseProtocol":"PROTOCOL_JSON","requestProtocol":"PROTOCOL_JSON","storeFront":{"code":"SDL"}}','"successful":true', '/service/cartMwAPIService/getCartUsingId', 'POST', '{"cartId":"0eab5e34-91b4-4ca9-81c4-2278afc5e61a","nextDayDeliveryEnabled":"true","pincode":"110020","sameDayDeliveryEnabled":"true","config":{"promoApplicable":"true","platform":"WEB","loyaltyPointsApplicable":"false","inventoryCheckApplicable":"true","autoSellerChange":"true","deliveryDetailsUpdate":"true"},"responseProtocol":"PROTOCOL_JSON","basketType":null,"requestProtocol":"PROTOCOL_JSON"}', 'successful":true'),
('ERAS', 'bhanwar.sharma@snapdeal.com',	'chinkit.tyagi@snapdeal.com', uuid(),'http://52.74.52.155:8080', '/service/erp/healthCheck', 'GET', 'Looks Healthy', null, null, null, null, null, null, null, null),
('FilmsUI', 'ravinder.singh04@snapdeal.com','mandeep.chawla@snapdeal.com', uuid(),'http://20.0.2.156:8080', '/service/catalog/healthCheck', 'GET', 'Looks Healthy', '/service/filter/admin/productFilterUpdate', 'POST', '{"responseProtocol":"PROTOCOL_JSON","requestProtocol":"PROTOCOL_JSON","productFilterUpdateDTOList":[{"supc":"SDL999112265","productFilterMap":{"djfilter":"a"},"productAttributeMap":null},{"supc":"SDL999112265","productFilterMap":{"djfilter":"a"},"productAttributeMap":null},{"supc":"SDL999112265","productFilterMap":{"djfilter":"a"},"productAttributeMap":null},{"supc":"SDL999112265","productFilterMap":{"djfilter":"a"},"productAttributeMap":null}]}', null, null, null, null, null),
('IPMS', 'bhanwar.sharma@snapdeal.com',	'tushar.srivastava@snapdeal.com', uuid(),'http://54.251.163.31:9097', '/service/ipms/healthCheck?supc=1002397', 'GET', 'Looks Healthy', '/service/ipms/getSellerInventoryPricingForPDPV6', 'POST', '{"orderedSUPCList":["1136650"],"zone":"NO_ZONE","pinAvailable":"true", "storeFront":"SDL","sellerBusinessType":"ALL","responseProtocol":"PROTOCOL_JSON", "requestProtocol":"PROTOCOL_JSON"}', '"successful":true', '/service/seller/getSellerByPrimaryEmail', 'POST', '{"responseProtocol":"PROTOCOL_JSON","requestProtocol":"PROTOCOL_JSON","primarySellerEmail":"bhanwar.sharma@snapdeal.com"}', '"successful":true'),
('OMS', 'mohit.bansal@snapdeal.com','nitin.gupta@snapdeal.com', uuid(),'http://52.74.88.101:8080', '/omsHealthCheck?pwd=snapdeal', 'GET', 'Authentication SUCCESS! Application and DB server both are up!', '/service/oms/order/getAllOrderParameters', 'POST', '{"responseProtocol":"PROTOCOL_JSON","requestProtocol":"PROTOCOL_JSON"}', '"successful":true','/service/oms/order/getAllOrderCancellationParams', 'POST', '{"responseProtocol":"PROTOCOL_JSON","requestProtocol":"PROTOCOL_JSON"}', '"successful":true'),
('OPMS', 'mohit.bansal@snapdeal.com','nitin.gupta@snapdeal.com', uuid(),'http://52.74.99.41:8080', '/opmsHealthCheck?pwd=snapdeal', 'GET', 'Authentication SUCCESS! Application and DB server both are up!', '/service/opms/payment/getAllPaymentModeCharges', 'POST', '{"responseProtocol":"PROTOCOL_JSON","requestProtocol":"PROTOCOL_JSON"}', '"successful":true', '/service/opms/payment/getAllOpmsProperties', 'POST', '{"responseProtocol":"PROTOCOL_JSON","requestProtocol":"PROTOCOL_JSON"}', '"message":"OpmsPropertySRO list created successfully."'),
('OPS', 'bhanwar.sharma@snapdeal.com',	'chinkit.tyagi@snapdeal.com, amit.chaurasia@snapdeal.com', uuid(),'http://52.74.34.78:9090', '/service/ops/healthCheck', 'GET', 'Looks Healthy', '/service/ops/getRateCardByCategory', 'POST', '{"responseProtocol":"PROTOCOL_JSON","requestProtocol":"PROTOCOL_JSON"}', null, null, null, null, null),
('Promo', 'mohit.bansal@snapdeal.com', 'prachi.agarwal@snapdeal.com', uuid(),'http://54.169.156.29:10220', '/service/promoAPIService/promoHealthCheck', 'GET', null, '/service/promoAPIService/getPromoByCode', 'POST', '{"responseProtocol":"PROTOCOL_JSON","requestProtocol":"PROTOCOL_JSON"}', '"successful":true', '/service/promoAPIService/getOfferStrips', 'POST', '{"pogId":1234567,"brandId":1,"bucketId":0,"responseProtocol":"PROTOCOL_JSON","requestProtocol":"PROTOCOL_JSON"}', '"successful":true'),
('RnR', 'mohit.bansal@snapdeal.com', 'vikram.b@snapdeal.com', uuid(),'http://54.254.153.61:8080', '/reviews-api/api/service/reviews/health', 'GET', '"status":"Up and Running"', '/reviews-api/api/service/reviews/quality/configurations/weightage/defaults', 'GET', null, 'reviewQualityWeightageConfiguration', null, null, null, null),
('ScoreAdmin', 'nishith.tripathi@snapdeal.com', 'priyanka.sharma@snapdeal.com', uuid(),'http://54.251.152.42:10060', '/checkSystemHealth', 'GET', '{score aerospike=true}', null, null, null, null, null, null, null, null),
('Score', 'nishith.tripathi@snapdeal.com', 'priyanka.sharma@snapdeal.com', uuid(),'http://54.251.152.42:10070', '/checkSystemHealth', 'GET', '{score aerospike=true}', null, null, null, null, null, null, null, null),
('Search', 'manish.pandey@snapdeal.com', 'deepak.garg@snapdeal.com, kuldeep.rana@snapdeal.com', uuid(),'http://20.0.0.35:5050', '/service/searchServer/healthCheck', 'GET', 'Losoks Healthy', '/service/searchServer/search', 'POST', '{"responseProtocol":"PROTOCOL_JSON", "requestProtocol":"PROTOCOL_JSON", "keyword":"iphone","q":"","start":0,"number":20,"sortBy":"rlvncy","clickSrc":"submitForViewAll","spellCheckEnabled":true,"partialSearch":true, "version":"v2","userTrackingId":"9899109988","categoryUrl":"ALL","userZone":"Z2","serviceable":"true"}', null, '/service/searchServer/srpFilters', 'POST', '{"responseProtocol":"PROTOCOL_JSON", "requestProtocol":"PROTOCOL_JSON", "q":"", "keyword":"nokia", "partialSearch":"true", "version":"v2","userTrackingId":"9899109988","categoryUrl":"ALL","userZone":"Z2","serviceable":"false"}', null),
('SearchExclusive', 'manish.pandey@snapdeal.com', 'deepak.garg@snapdeal.com, kuldeep.rana@snapdeal.com', uuid(),'http://52.76.174.114:5050', '/service/searchServer/healthCheck', 'GET', '200', '/service/searchServer/search', 'POST', '{"responseProtocol":"PROTOCOL_JSON","requestProtocol":"PROTOCOL_JSON","keyword":"shirt","q":"","start":0,"number":20,"sortBy":"rlvncy","clickSrc":"submitForViewAll","spellCheckEnabled":true,"partialSearch":true,"version":"v2","userTrackingId":"9899109988","categoryUrl":"ALL"}', '{"successful":true,"code":"sc_nonzero_lc","message":null,"validationErrors":[],"protocol":"PROTOCOL_JSON","searchSRO":{"itemIds":[681187358420,682650065825],"noOfResults":2,"resultType":"BIN_PAGE","bins":[],"categories":{},"zonePageUrl":null,"error":false,"partialSearch":false,"itemIdTypeDTO":[],"maxResultsSubCategoryDTO":null,"noOfMatches":2,"partnerSRO":null,"spellSuggestions":[],"spellCheckUsed":false,"personaUsed":false,"code":"sc_nonzero_lc"}}', '/service/searchServer/srpFilters', 'POST', '{"responseProtocol":"PROTOCOL_JSON","requestProtocol":"PROTOCOL_JSON","q":"","keyword":"shirt","partialSearch":"true","version":"v2","userTrackingId":"9899109988","categoryUrl":"ALL"}', '{"code":null,"filterListSRO":null,"message":null,"protocol":"PROTOCOL_JSON","successful":true,"validationErrors":[]}');
