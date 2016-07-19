package com.snapdeal.healthcheck.app.mongo.repositories;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.snapdeal.healthcheck.app.model.ConnIssueComp;
@Repository
public interface ConnIssueRepository extends MongoRepository<ConnIssueComp, String>{

	@Query("{ 'componentName' : ?0, 'status' : 'OPEN', 'issueType' : 'CTO'}")
	public ConnIssueComp getConnectionTimedOutEntry(String compName);
	
	@Query("{ 'componentName' : ?0, 'status' : 'OPEN', 'issueType' : 'NWI'}")
	public ConnIssueComp getNetworkIssueEntry(String compName);
	
	@Query("{ 'status' : 'OPEN'}")
	public List<ConnIssueComp> findAllOpenIssues();
	
	@Query("{ 'execDate' : ?0, 'issueType' : 'CTO'}")
	public List<ConnIssueComp> findAllConnTimedOutForExecDate(String execDate, Sort sort);
	
	@Query("{ 'execDate' : ?0, 'issueType' : 'NWI'}")
	public List<ConnIssueComp> findAllNetworkIssueForExecDate(String execDate, Sort sort);
}
