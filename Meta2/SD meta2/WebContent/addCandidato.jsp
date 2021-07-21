<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Adiciona membro</title>
</head>
<body>

<td> Membros = ${frase}</td>

<s:form action="addElemento" method="post">
    <s:hidden name="num" value="%{myindex}" />
    <s:hidden name="nomeE" value="%{nomeE}" />
    <s:hidden name="funcE" value="%{funcE}" />
    <br>
    <s:text name="Numero do CC do elemento a adicionar:" />
    <s:textfield name="elem" /><br>
    <s:submit value="Adicionar Membros"/>
</s:form>

<s:form action="addLista" method="post">
    <s:hidden name="num" value="%{myindex}" />
    <s:hidden name="nomeE" value="%{nomeE}" />
    <s:hidden name="funcE" value="%{funcE}" />
    <s:submit value="Adicionar Lista"/>
</s:form>


<li><a href="Menu.jsp">Menu</a> </li>
</body>
</html>
