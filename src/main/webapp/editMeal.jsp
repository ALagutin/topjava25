<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.MealTo"/>
<html>
<head>
    <title>Meal</title>
    <link href="style.css" rel="stylesheet" type="text/css">
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Edit meal</h2>
</body>
<form action="meals" method="post">
    <label>
        <input name="id" value="<c:out value="${meal.id}"/>" style="display: none">
    </label>
    <dl>
        <dt>DateTime:</dt>
        <dd><label>
            <input type="datetime-local" name="dateTime" value="<c:out value="${meal.dateTime}"/>" required>
        </label></dd>
    </dl>
    <dl>
        <dt>Description:</dt>
        <dd><label>
            <input type="text" name="description" value="<c:out value="${meal.description}"/>" required>
        </label></dd>
    </dl>
    <dl>
        <dt>Calories:</dt>
        <dd><label>
            <input type="number" name="calories" value="<c:out value="${meal.calories}"/>" required>
        </label></dd>
    </dl>
    <button type="submit">Save</button>
    <button onclick="window.history.back()" type="button">Cancel</button>
</form>
</html>
