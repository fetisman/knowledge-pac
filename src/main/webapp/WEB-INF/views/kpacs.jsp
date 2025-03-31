<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<head>
    <link rel="stylesheet" href="<c:url value='/resources/css/suite.css'/>">
    <%--    <script src="<c:url value='/resources/js/suite.js'/>"></script>--%>
    <link rel="stylesheet" href="https://cdn.dhtmlx.com/suite/7.0/suite.min.css">
    <script src="https://cdn.dhtmlx.com/suite/7.0/suite.min.js"></script>
</head>

<div id="kpacGrid"></div>

<%--<form id="kpacForm" action="/kpac-app/kpacs" method="post">--%>
<form id="kpacForm" method="post">
    <label for="title">Title:</label>
    <input type="text" id="title" name="title" required>

    <label for="description">Description:</label>
    <textarea id="description" name="description" required></textarea>

    <label for="creationDate">Creation Date:</label>
    <input type="date" id="creationDate" name="creationDate" required>

    <button type="submit">Add K-PAC</button>
</form>

<c:forEach var="error" items="${errors}">
    <p>${error}</p>
</c:forEach>

<c:if test="${not empty errors}">
    <div class="errors">
        <ul>
            <c:forEach var="error" items="${errors}">
                <li>${error.defaultMessage}</li>
            </c:forEach>
        </ul>
    </div>
</c:if>

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
                    <%--return `<button onclick="deleteKpac('${row.id}')" class="delete-btn">Delete</button>`;--%>
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
            fetch(`/kpac-app/kpacs/` + id, {method: 'DELETE'})
                .then(async response => {
                    if (response.ok) {
                        grid.data.remove(id);
                        await loadKpacs();
                    } else {
                    const text = await response.text();
                    console.error("Failed to delete K-PAC:", text);
                        // return response.text().then(text => {
                        //     console.error("Failed to delete K-PAC:", text);
                        // });
                    }
                })
                .catch(error => console.error("Error deleting K-PAC:", error));
        }
    }

    document.getElementById('kpacForm').addEventListener('submit', async function(event) {
        event.preventDefault(); // Відміняємо стандартну поведінку форми

        const formData = new FormData(this);

    try {
        const response = await fetch('/kpac-app/kpacs', {
            method: 'POST',
            headers: {
                'Accept': 'application/json'
            },
            body: new URLSearchParams(formData)
        });

        if (response.ok) {
            // Якщо додавання пройшло успішно — оновлюємо таблицю
            await loadKpacs();
            // loadKpacs();
            this.reset(); // Очищення форми kpacForm після успіху
            //document.getElementById('kpacForm').reset();
        } else {
            const errors = await response.json();
            console.error('Validation errors:', errors);
            alert('Помилка валідації: ' + JSON.stringify(errors));
        }
    } catch (error) {
        console.error("Error adding K-PAC:", error);
    }
});

    async function loadKpacs() {
        console.log("loadKpacs...");
        // const response = await fetch('/kpac-app/kpacs/json');
        const response = await fetch('/kpac-app/kpacs/json', {
            headers: {
                'Accept': 'application/json'
            }
        });

        if (response.ok) {
            console.log(grid.data);
            const data = await response.json();
            grid.data.parse([]); // Очищаємо таблицю перед завантаженням
            // grid.data.clear(); // Очищаємо таблицю перед завантаженням
            grid.data.parse(data); // Оновлюємо дані у таблиці DHTMLX
        } else {
            alert('Failed to load K-PACs');
        }
    }

    // Завантажуємо дані при першому завантаженні сторінки
    loadKpacs();
</script>