<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>E-Voting</title>
</head>
<body>

	<li><a href="registar.jsp">Registar</a> </li>
	<s:form action="passaURL" method="post">
		<s:submit value="Login"/>
	</s:form>

</body>
</html>