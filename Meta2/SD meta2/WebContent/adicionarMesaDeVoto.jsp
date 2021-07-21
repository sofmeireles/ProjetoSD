<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<html>
<head>
    <title>Adicionar mesa de voto</title>
</head>
<body>
<s:text name="Eleicao ja possui mesas nos departamentos:" /><br/>
<td>
    ${mesas}
</td>
<br/><br/>

<s:form action="adicionarMesaDeVoto" method="post">
    <s:hidden name="num" value="%{myindex}" />
    <s:text name="Em qual departamento pretende inserir uma mesa de voto:" /><br/>
    <s:textfield name="dep" /><br>
    <s:submit />
</s:form>
<a href= "Menu.jsp">voltar menu</a>

</body>
</html>
