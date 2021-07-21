<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<html>
<head>
    <title>Eleicoes atuais</title>
</head>
<body>
<s:text name="Eleicoes" /><br/>
<s:iterator  value="info" status="infor">
    <fieldset>
        <s:property value="titulo"/><br/>
        <s:form action="listaListasEleicao" method="post">
            <s:hidden name="numE" value="%{#infor.index}" />
            <s:submit value="Listas eleicao"/>
        </s:form>
    </fieldset>
</s:iterator>
</body>
</html>
