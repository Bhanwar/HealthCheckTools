<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<link
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css"
	rel="stylesheet" />
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

<link
	href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-validator/0.5.3/css/bootstrapValidator.css"
	rel="stylesheet" />
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-validator/0.5.3/js/bootstrapValidator.js"></script>
<spring:url value="/css/home.css" var="webAppCss" />
<spring:url value="/css/tooltipster.css" var="toolTipsterCss" />
<link href="${webAppCss}" rel="stylesheet" />
<link href="${toolTipsterCss}" rel="stylesheet" />
<spring:url value="/javascript/home.js" var="webAppJs" />
<spring:url value="/javascript/jquery.tooltipster.js"
	var="toolTipsterJs" />
<script src="${webAppJs}"></script>
<script src="${toolTipsterJs}"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Health Check App</title>
<link rel=icon type=image/ico
	href="http://i3.sdlcdn.com/img/icons/favicon.ico" />
</head>
<body>
	<div id="header" class="pointer"></div>
	<div id="main-content">
		<div id="summary">
			<h3>Total Components: ${total}</h3>
			<h3>Date: ${dateTime}</h3>
			<div id="tableHdr-anchor"></div>
			<table id="cloneTable" class="table dashTable"></table>
			<table id="dashDataTable" class="table dashTable">
				<thead>
					<tr>
						<th rowspan="2" class="tableHdr dashTable"
							style="text-align: center; width: 15%;"><i>Component</i></th>
						<th class="tableHdr dashTable" style="text-align: center;"><i>${dateStr}</i></th>
					</tr>
					<tr>
						<th bgcolor="#FFFFFF" class="tableHdr dashTable">
							<div style="width: 4.17%; float: left; text-align: left">0</div>
							<div style="width: 4.16%; float: left; text-align: left">1</div>
							<div style="width: 4.17%; float: left; text-align: left">2</div>
							<div style="width: 4.17%; float: left; text-align: left">3</div>
							<div style="width: 4.16%; float: left; text-align: left">4</div>
							<div style="width: 4.17%; float: left; text-align: left">5</div>
							<div style="width: 4.17%; float: left; text-align: left">6</div>
							<div style="width: 4.16%; float: left; text-align: left">7</div>
							<div style="width: 4.17%; float: left; text-align: left">8</div>
							<div style="width: 4.17%; float: left; text-align: left">9</div>
							<div style="width: 4.16%; float: left; text-align: left">10</div>
							<div style="width: 4.17%; float: left; text-align: left">11</div>
							<div style="width: 4.17%; float: left; text-align: left">12</div>
							<div style="width: 4.16%; float: left; text-align: left">13</div>
							<div style="width: 4.17%; float: left; text-align: left">14</div>
							<div style="width: 4.17%; float: left; text-align: left">15</div>
							<div style="width: 4.16%; float: left; text-align: left">16</div>
							<div style="width: 4.17%; float: left; text-align: left">17</div>
							<div style="width: 4.17%; float: left; text-align: left">18</div>
							<div style="width: 4.16%; float: left; text-align: left">19</div>
							<div style="width: 4.17%; float: left; text-align: left">20</div>
							<div style="width: 4.17%; float: left; text-align: left">21</div>
							<div style="width: 4.16%; float: left; text-align: left">22</div>
							<div style="width: 4.17%; float: left; text-align: left">23</div>
						</th>
					</tr>
				</thead>
				<c:forEach items="${data}" var="entry">
					<tr>
						<td class="compName dashTable">${entry.key}</td>
						<td bgcolor="#FFFFFF" class="active dashTable">
							<div class="graphcontainer">
								<div class="percentgraph"
									style="width: <c:out value="${timePercentage}"/>%;"></div>
								<c:forEach items="${entry.value}" var="val">
									<c:set var="downTimeData"
										value="<html>Server Down Time: ${val.downTime}
									<br>Server Up Time: ${val.upTime}
									<br>Total time server down (mins): ${val.totalTime}</html>" />
									<div title="${downTimeData}" class="redbar pointer toolTipster" onclick="updateReason()"
										style="margin-left: <c:out value="${val.leftMargin}"/>%; width: <c:out value="${val.width}"/>%;"></div>


								</c:forEach>
							</div>
						</td>
					</tr>
				</c:forEach>
			</table>
			<div id="bottom_anchor"></div>
		</div>
	</div>
</body>
</html>