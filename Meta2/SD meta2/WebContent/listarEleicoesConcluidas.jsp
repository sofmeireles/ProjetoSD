<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<html>
<head>
    <title>Listar Eleicoes Concluidas</title>
</head>
<body>
<s:iterator  value="info" status="infor">
    <fieldset>
        <s:property value="titulo"/><br/>
        <s:form action="consultarDetalhesEleicoesPassadas" method="post">
            <s:hidden name="num" value="%{#infor.index}" />
            <s:submit value="Consultar detalhes eleicao"/>
        </s:form>
    </fieldset>
</s:iterator>
<br>
<br>
<a href= "Menu.jsp">Menu</a>
</body>
</html>