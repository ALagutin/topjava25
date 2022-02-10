package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.inMemoryMealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    public static MealRepository repository;
    private static final int CALORIES_PER_DAY = 2000;
    private static final Logger log = getLogger(MealServlet.class);
    private static final String LIST_MEALS = "meals.jsp";
    private static final String INSERT_OR_EDIT = "editMeal.jsp";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        repository = new inMemoryMealRepository();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("redirect to meals");
        String forward = "";
        String action = req.getParameter("action");
        String id = req.getParameter("id");

        if (action == null) {
            log.debug("list meals");
            forward = LIST_MEALS;
            req.setAttribute("meals", getAll());
        } else if (action.equalsIgnoreCase("delete")) {
            log.debug("delete meal");
            repository.delete(Integer.parseInt(id));
            resp.sendRedirect("meals");
            return;
        } else if (action.equalsIgnoreCase("insert")) {
            log.debug("insert meal");
            forward = INSERT_OR_EDIT;
        } else if (action.equalsIgnoreCase("edit")) {
            log.debug("edit meal");
            forward = INSERT_OR_EDIT;
            req.setAttribute("meal", repository.get(Integer.parseInt(id)));
        }

        req.getRequestDispatcher(forward).forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        log.debug("save meal");
        req.setCharacterEncoding("UTF-8");

        String sId = req.getParameter("id");
        Integer id = null;
        if(!sId.equals("")) {
            id = Integer.parseInt(sId);
        }
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));
        LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("dateTime"));
        Meal meal = new Meal(id, dateTime, description, calories);
        repository.save(meal);

        resp.sendRedirect("meals");
    }

    private List<MealTo> getAll() {
        return MealsUtil.filteredByStreams(repository.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY);
    }
}
