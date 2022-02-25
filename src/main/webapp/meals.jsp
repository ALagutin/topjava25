<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>

<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .excess {
            color: red;
        }

        .dateTimFilter {
            margin-right: 15px;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <hr/>
    <h2>Meals</h2>
    <form action="meals">
    <div style="display: flex">
        <div class="dateTimFilter">
            <p>От даты (включая)</p>
            <input type="date" value="${param.startDate}" name="startDate">
        </div>
        <div class="dateTimFilter">
            <p>До даты (включая)</p>
            <input type="date" value="${param.endDate}" name="endDate">
        </div>
        <div class="dateTimFilter">
            <p>От времени (включая)</p>
            <input type="time" value="${param.startTime}" name="startTime">
        </div>
        <div class="dateTimFilter">
            <p>До времени (исключая)</p>
            <input type="time" value="${param.endTime}" name="endTime">
        </div>
    </div>
    <br>
    <div>
        <button type="reset" onclick="window.location.href = 'meals'">Отменить</button>
        <button type="submit">Фильтровать</button>
    </div>
    <br>
    </form>
    <a href="meals?action=create">Add Meal</a>
    <br><br>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
            <tr class="${meal.excess ? 'excess' : 'normal'}">
                <td>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>