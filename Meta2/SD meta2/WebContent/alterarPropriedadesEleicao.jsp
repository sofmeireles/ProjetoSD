<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<html>
<head>
    <title>Alterar propriedades eleicao</title>
</head>
<body>
<s:text name="Detalhes eleicao:" /><br/>

<td>
    ${desc}
</td>
<br>
<br>
<s:text name="Qual propriedade pretende alterar? (Digite o numero da opcao)" /><br/>

<td>
    1 - Titulo 2 - Descricao 3 - Data de inicio(AAAA-MM-DD) 4 - Data de fim(AAAA-MM-DD) 5 - Tipo(1-Estudamte 2-Docente 3-Funcionario 4-Todos-) 6 - Departamento
</td>

<s:form action="alterarPropriedadesEleicao" method="post">
    <s:hidden name="num" value="%{myindex}" />
    <s:text name="Opcao:" />
    <s:textfield name="opcao" /><br>
    <s:text name="Novo:" />
    <s:textfield name="novo" /><br>
    <s:submit />
</s:form>
<a href= "Menu.jsp">voltar menu</a>

</body>
</html>
