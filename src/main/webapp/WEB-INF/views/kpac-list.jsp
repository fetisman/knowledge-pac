<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<head>
    <link rel="stylesheet" href="<c:url value='/resources/css/suite.css'/>">
    <%--    <script src="<c:url value='/resources/js/suite.js'/>"></script>--%>
    <link rel="stylesheet" href="https://cdn.dhtmlx.com/suite/7.0/suite.min.css">
    <script src="https://cdn.dhtmlx.com/suite/7.0/suite.min.js"></script>
</head>

<div id="kpacGrid"></div>

<script>
    const kpacData = [
        <c:forEach var="kpac" items="${kpacs}" varStatus="status">
        {
            id: '${kpac.id}',
            title: '${kpac.title}',
            description: '${kpac.description}',
            creationDate: '${kpac.creationDate}'
        }<c:if test="${!status.last}">, </c:if>
        </c:forEach>
    ];

    const grid = new dhx.Grid("kpacGrid", {
        columns: [
            {id: "id", header: [{text: "ID"}], width: 50},
            {id: "title", header: [{text: "Title"}], width: 150},
            {id: "description", header: [{text: "Description"}], width: 300},
            {id: "creationDate", header: [{text: "Creation Date"}], width: 150},
            {
                id: "delete", header: [{text: "Delete"}], width: 100, align: "center",
                template: function (value, row) {
                    return 'Delete';
                }
            }
        ],
        data: kpacData,
        autoWidth: true,
        resizable: true,
        sortable: true
    });

    grid.events.on("cellClick", (row, column) => {
        // console.log("Clicked row:", row);
        if (column.id === "delete" && row.id) {
            deleteKpac(row.id);
        }
    });

    function deleteKpac(id) {
        console.log("Deleting K-PAC with id:", id);

        if (confirm("Delete K-PAC ?")) {
            fetch(`/kpac-app/kpacs/delete/` + id, {method: 'DELETE'})
                .then(response => {
                    if (response.ok) {
                        grid.data.remove(id);
                    } else {
                        return response.text().then(text => {
                            console.error("Failed to delete K-PAC:", text);
                        });
                    }
                })
                .catch(error => console.error("Error:", error));
        }
    }

</script>