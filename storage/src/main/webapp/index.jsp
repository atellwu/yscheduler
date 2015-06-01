<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html  
PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"  
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Insert title here</title>
</head>
<body>
</body>
<form enctype="multipart/form-data" action="/upload" method="post">
	<input type="file" name="fileUpload" /> <br />
	<input type="hidden" name="nameSpace" value="task"/> <br />
	<input type="text" name="key"/> <br />
	<input type="text" name="proxyPath" value="http://localhost:8080/static/cross_domain_proxy.html"/> <br />
	<input type="submit" value="上传" />
	<%
	request.getContextPath();
	%>
</form>
</html>
