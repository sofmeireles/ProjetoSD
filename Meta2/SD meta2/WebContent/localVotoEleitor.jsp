<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<html>
<head>
    <title>Local voto</title>
</head>
<body>
<s:iterator  value="info" status="infor">
    <fieldset>
        <s:property value="titulo"/><br/>
        <s:form action="eleitores" method="post">
            <s:hidden name="num" value="%{#infor.index}" />
            <s:submit value="Eleitores"/>
        </s:form>
    </fieldset>
</s:iterator>
</body>
</html>