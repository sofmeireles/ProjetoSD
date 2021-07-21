<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<html>
<head>
    <title>Listas de Eleicao</title>
</head>
<body>
<s:iterator  value="listas" status="infor">
    <fieldset>
        <s:property value="nomeLista"/><br/>
        <s:form action="votar" method="post">
            <s:hidden name="tam" value="%{#infor.index}" />
            <s:hidden name="numE" value="%{numE}" />
            <s:submit value="Votar"/>
        </s:form>
    </fieldset>
</s:iterator>
<fieldset>
    <s:form action="votar" method="post">
        <s:text name="Branco" /><br/>
        <s:hidden name="tam" value="%{tam+2}" />
        <s:hidden name="numE" value="%{numE}" />
        <s:submit value="Votar"/>
    </s:form>
</fieldset>
<fieldset>
    <s:form action="votar" method="post">
        <s:text name="Nulo" /><br/>
        <s:hidden name="tam" value="%{tam+1}" />
        <s:hidden name="numE" value="%{numE}" />
        <s:submit value="Votar"/>
    </s:form>
</fieldset>

</body>
</html>