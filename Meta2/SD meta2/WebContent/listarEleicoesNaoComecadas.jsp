<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<html>
<head>
    <title>Listar Eleicoes Nao Comecadas</title>
</head>
<body>
<td> name="Selecione a eleicao que deseja:" <td/>
<s:iterator  value="info" status="infor">
    <fieldset>
        <s:property value="titulo"/><br/>
        <s:form action="passaEleicao" method="post">
            <s:hidden name="num" value="%{#infor.index}" />
            <s:submit value="Selecionar"/>
        </s:form>
    </fieldset>
</s:iterator>

</body>
</html>
