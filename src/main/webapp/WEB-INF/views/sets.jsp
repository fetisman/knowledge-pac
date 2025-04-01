<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<head>
  <link rel="stylesheet" href="https://cdn.dhtmlx.com/suite/7.0/suite.min.css">
  <script src="https://cdn.dhtmlx.com/suite/7.0/suite.min.js"></script>
</head>

<h2>K-PAC Sets</h2>

<form id="setForm" method="post">
  <label for="title">Title:</label>
  <input type="text" id="title" name="title" required>

  <label for="kpacs">Select K-PACs:</label>
  <select id="kpacs" name="kpacIds" multiple required>
<%--    <c:forEach var="kpac" items="${kpacs}">--%>
<%--      <option value="${kpac.id}">${kpac.title}</option>--%>
<%--    </c:forEach>--%>
  </select>

  <button type="submit">Add K-PAC Set</button>
</form>

<div id="setGrid"></div>

<script>
  const setData = [
    <c:forEach var="set" items="${sets}" varStatus="status">
    {
      id: '${set.id}',
      title: '${set.title}'
    }<c:if test="${!status.last}">, </c:if>
    </c:forEach>
  ];

  const grid = new dhx.Grid("setGrid", {
    columns: [
      {id: "id", header: [{text: "ID"}], width: 50},
      {id: "title", header: [{text: "Title"}], width: 150},
      {
        id: "delete", header: [{text: "Delete"}], width: 100, align: "center",
        template: () => '❌'
      }
    ],
    data: setData,
    autoWidth: true,
    resizable: true,
    sortable: true
  });

  grid.events.on("cellClick", (row, column) => {
    if (column.id === "delete" && row.id) {
      deleteSet(row.id);
    } else {
      window.location.href = "/kpac-app/sets/" + row.id;
    }
  });

  async function deleteSet(id) {
    if (confirm("Delete K-PAC Set with id " + id + " ?")) {
      await fetch(`/kpac-app/sets/` + id, {method: 'DELETE'});
      await loadKpacSets();
    }
  }

  document.getElementById('setForm').addEventListener('submit', async function(event) {
    event.preventDefault();

    const formData = new FormData(this);
    try {
      const response = await fetch('/kpac-app/sets', {
        method: 'POST',
        headers: {
          'Accept': 'application/json'
        },
        body: new URLSearchParams(formData)
      });

      if (response.ok) {
        // Якщо додавання пройшло успішно — оновлюємо таблицю
        await loadKpacSets();
        this.reset(); // Очищення форми kpacForm після успіху
      } else {
        const errors = await response.json();
        console.error('Validation errors:', JSON.stringify(errors));
        alert('Validation error: ' + JSON.stringify(errors));
      }
    } catch (error) {
      console.error("Error adding K-PAC:", error);
    }

  });

  async function loadKpacSets() {
    const response = await fetch('/kpac-app/sets/json', {
      headers: {
        'Accept': 'application/json'
      }
    });

    if (response.ok) {
      const data = await response.json();
      grid.data.parse([]); // Очищаємо таблицю перед завантаженням
      grid.data.parse(data); // Оновлюємо дані у таблиці DHTMLX
    } else {
      console.log('Failed to load K-PAC Sets');
      alert('Failed to load K-PAC Sets');
    }
  }

  async function loadKpacs() {
    const response = await fetch('/kpac-app/kpacs/json');
    const data = await response.json();
    const select = document.getElementById("kpacs");
    select.innerHTML = ""; // Очищення списку
    data.forEach(kpac => {
      const option = document.createElement("option");
      option.value = kpac.id;
      option.textContent = kpac.title;
      select.appendChild(option);
    });
  }

  // Викликати при завантаженні сторінки
  document.addEventListener("DOMContentLoaded", () => {
    loadKpacSets();
    loadKpacs();
  });
</script>

</html>
