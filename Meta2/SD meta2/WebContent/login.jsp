<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<html>
<head>
    <title>Login</title>
</head>
<body>
<s:form action="login" method="post">
    <s:text name="N�mero cart�o de cidad�o:" />
    <s:textfield name="numCC" /><br>
    <s:text name="Password:" />
    <s:textfield name="password" /><br>
    <s:submit />
</s:form>
<p><a href="${url}">Login Facebook</a></p><br>

</body>
</html>
