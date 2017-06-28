<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String pathHeader = request.getContextPath();
	String basePathHeader = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ pathHeader + "/";
%>
<link rel="stylesheet" href="<%=basePathHeader%>css/header.css" type="text/css">
<div class="header">
	<div class="header-left">
		<div style="float: left;">
			<img src="<%=basePathHeader%>images/icon_mic.png" />
		</div>
		<div class="header-left-title-container">
			<div class="title-company">IBM RESEARCH-CHINA</div>
			<div class="title-project-name">Speech Emotion Analysis</div>
		</div>
		<div class="header-right-container">
<%-- 			<img src="<%=basePathHeader%>images/icon_refresh.png" /> --%>
<%-- 			<img src="<%=basePathHeader%>images/icon_edit.png" /> --%>
		</div>
		<div class="clearfix"></div>
	</div>
</div>