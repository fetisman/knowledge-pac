<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<head>
    <link rel="stylesheet" href="https://cdn.dhtmlx.com/suite/7.0/suite.min.css">
    <script src="https://cdn.dhtmlx.com/suite/7.0/suite.min.js"></script>
</head>

<h2>K-PAC Set Details: ${set.title}</h2>

<div id="kpacGrid"></div>

<script>
    const kpacData = [
        <c:forEach var="kpac" items="${set.kpacs}" varStatus="status">
        {
            id: '${kpac.id}',
            title: '${kpac.title}',
            description: '${kpac.description}'
        }<c:if test="${!status.last}">, </c:if>
        </c:forEach>
    ];

    const grid = new dhx.Grid("kpacGrid", {
        columns: [
            {id: "id", header: [{text: "K-PAC ID"}], width: 100},
            {id: "title", header: [{text: "Title"}], width: 150},
            {id: "description", header: [{text: "Description"}], width: 300}
        ],
        data: kpacData,
        autoWidth: true,
        resizable: true,
        sortable: true
    });
</script>

</html>
