<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
    <title>Conecta FB</title>
</head>
<body>
<s:text name="OLA" />
<s:form action="conectaFacebook" method="post">
    <s:text name="Número cartão de cidadão:" />
    <s:textfield name="CC" /><br>
    <s:submit />
</s:form>
</body>
</html>
