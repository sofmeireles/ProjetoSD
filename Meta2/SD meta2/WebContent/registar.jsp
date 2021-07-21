<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<html>
<head>
    <title>Registar</title>
</head>
<body>
<s:form action="register" method="post">
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
    <s:text name="Password:" />
    <s:textfield name="password" /><br>
    <s:text name="Departamento:" />
    <s:textfield name="departamento" /><br>
    <s:text name="Nº de telefone:" />
    <s:textfield name="telefone" /><br>
    <s:text name="Morada:" />
    <s:textfield name="morada" /><br>
    <s:text name="Nº CC:" />
    <s:textfield name="numCC" /><br>
    <s:text name="Validade do CC (AAAA-MM-DD):" />
    <s:textfield name="valCC" /><br>
    <s:submit />
</s:form>
</body>
</html>
