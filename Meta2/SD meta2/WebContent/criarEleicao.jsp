<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%><html>
<head>
    <title>Criar Eleicao</title>
</head>
<body>
<s:form action="criarEleicao" method="post">
    <s:text name="Detalhes eleicao:" /><br/><br/>
    <s:text name="Data de inicio:" />
    <s:textfield name="dataInicio" /><br>
    <s:text name="Data de fim:" />
    <s:textfield name="dataFim" /><br>
    <s:text name="Titulo:" />
    <s:textfield name="titulo" /><br>
    <s:text name="Descricao:" />
    <s:textfield name="descricao" /><br>
    <s:text name="Tipo:" />
    <label>
        <select name="tipo">
            <option value="EST">Estudante</option>
            <option value="DOC">Docente</option>
            <option value="FUNC">Funcionario</option>
            <option value="FUNC">Todos</option>
        </select>
    </label><br>
    <s:text name="Departamento:" />
    <s:textfield name="departamento" /><br>
    <s:submit />
</s:form>
</body>
</html>
