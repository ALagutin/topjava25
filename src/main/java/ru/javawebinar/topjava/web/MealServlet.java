package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private MealRestController controller;

    @Override
    public void init() {
        ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        controller = appCtx.getBean(MealRestController.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String authId = request.getParameter("authId");

        if (authId != null) {
            SecurityUtil.setUserId(Integer.parseInt(authId));
            response.sendRedirect("meals");
            return;
        }

        String sId = request.getParameter("id");
        Integer id = sId.isEmpty() ? null : Integer.valueOf(sId);
        String userId = request.getParameter("userId");
        Meal meal = new Meal(id,
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")), userId.isEmpty() ? null : Integer.valueOf(userId));
        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        if (meal.isNew()) {
            controller.create(meal);
        } else {
            controller.update(meal, id);
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                controller.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000, 0) :
                        controller.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");

                String sStartDate = request.getParameter("startDate");
                String sEndDate = request.getParameter("endDate");
                String sStartTime = request.getParameter("startTime");
                String sEndTime = request.getParameter("endTime");

                List<MealTo> meals = null;

                if ((sStartDate == null || sStartDate == "") && (sEndDate == null || sEndDate == "") && (sStartTime == null || sStartTime == "") && (sEndTime == null || sEndTime == "")) {
                    meals = controller.getAll();
                } else {
                    LocalDate startDate = (sStartDate == null || sStartDate == "") ? LocalDate.MIN : LocalDate.parse(sStartDate);
                    LocalDate endDate = (sEndDate == null || sEndDate == "") ? LocalDate.MAX : LocalDate.parse(sEndDate).plusDays(1);
                    LocalTime startTime = (sStartTime == null || sStartTime == "") ? LocalTime.MIN : LocalTime.parse(sStartTime);
                    LocalTime endTime = (sEndTime == null || sEndTime == "") ? LocalTime.MAX : LocalTime.parse(sEndTime).plusMinutes(1);

                    meals = controller.getAll(startDate, endDate, startTime, endTime);
                }

                request.setAttribute("meals", meals);
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

}
