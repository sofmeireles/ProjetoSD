<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Title</title>
</head>
<body>
<s:form action="passaLista" method="post">
    <s:hidden name="num" value="%{myindex}" />
    <s:hidden name="nomeE" value="%{nomeE}" />
    <s:hidden name="funcE" value="%{funcE}" />
    <s:text name="Nome:" />
    <s:textfield name="nome" /><br>
    <s:text name="Funcao:" />
    <label>
        <select name="func">
            <option value="EST">Estudante</option>
            <option value="DOC">Docente</option>
            <option value="FUNC">Funcionario</option>
        </select>
    </label><br>
    <s:submit value="Adicionar elementos"/>
</s:form>


</body>
</html>
