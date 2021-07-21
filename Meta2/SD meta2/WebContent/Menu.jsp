<%--
  Created by IntelliJ IDEA.
  User: sofss
  Date: 18/05/2021
  Time: 14:34
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%><html><html>
<head>
    <title>Menu</title>
</head>
<body>
<s:text name="Menu:" />
<br>
<br>
<c:choose>
    <c:when test="${session.admin == true}">
        <button onclick="window.location.href='criarEleicao.jsp'">CriarEleicao</button>
<br>
        <br>
        <s:form action="listarEleicoes" method="post">
            <s:submit value="Alterar propriedades eleicao"/>
        </s:form>

        <s:form action="listarEleicoesConcluidas" method="post">
            <s:submit value="Lista eleicoes concluidas"/>
        </s:form>

        <s:form action="localVotoEleitor" method="post">
            <s:submit value="Local voto"/>
        </s:form>

        <s:form action="listarEleicoesMesaDeVoto" method="post">
            <s:submit value="Adicionar mesa de voto"/>
        </s:form>

        <s:form action="listarEleicoesNaoComecadas" method="post">
            <s:submit value="Adicionar uma lista candidata"/>
        </s:form>
        <s:form action="logout" method="post">
            <s:submit value="Logout"/>
        </s:form>
    </c:when>
</c:choose>

<c:choose>
    <c:when test="${session.admin == false}">
        <s:form action="listarEleicoesAtuais" method="post">
            <s:submit value="Votar"/>
        </s:form><br><br>
        <a href="https://www.facebook.com/sharer/sharer.php?u=https://127.0.0.1:8443/War/login.action&quote=Alerta CM! Eleicao nova">
            Partilhar a eleicoes
        </a><br>
        <s:form action="logout" method="post">
            <s:submit value="Logout"/>
        </s:form>
    </c:when>

</c:choose>


</body>
</html>
