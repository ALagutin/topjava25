package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpringMain {

    public static MealService mealService;

    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));

            System.out.println();
            System.out.println("Search user by email");
            try {
                System.out.println(adminUserController.getByMail("email123@mail.ru"));
            } catch (Exception e) {
                System.out.println(e);
            }

            //test mealRest
            System.out.println();
            System.out.println("mealRestController get all");
            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            mealRestController.getAll(LocalDate.MIN, LocalDate.MAX, LocalTime.MIN, LocalTime.MAX).forEach(System.out::println);

            //test mealService
            System.out.println();
            mealService = appCtx.getBean(MealService.class);

            //get all
            System.out.println("GET all by userId");
            getAll(1).forEach(System.out::println);

            //delete
            System.out.println();
            System.out.println("DELETE by userId & mealId");
            mealService.delete(1, 4);
            getAll(1).forEach(System.out::println);
            System.out.println();
            System.out.println("DELETE by userId & WRONG mealId");
            try {
                mealService.delete(1, 5);
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            } catch (Exception e) {
                System.out.println(e);
            }
            System.out.println();
            System.out.println("DELETE by WRONG userId & mealId");
            try {
                mealService.delete(2, 4);
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            } catch (Exception e) {
                System.out.println(e);
            }

            //create
            System.out.println();
            System.out.println("CREATE by userId & mealId");
            Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "new meal", 1000, 1);
            mealService.create(1, meal);
            getAll(1).forEach(System.out::println);
            System.out.println();
            System.out.println("CREATE by WRONG userId & mealId");
            try {
                mealService.create(2, meal);
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            } catch (Exception e) {
                System.out.println(e);
            }

            //update
            System.out.println();
            System.out.println("UPDATE by userId & mealId");
            Meal updatedMeal = new Meal(meal.getId(), meal.getDateTime(), "updated meal", 2000, 1);
            mealService.update(1, updatedMeal);
            getAll(1).forEach(System.out::println);
            System.out.println();
            System.out.println("UPDATE by userId & WRONG mealId");
            try {
                meal.setId(1000);
                mealService.update(1, meal);
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            } catch (Exception e) {
                System.out.println(e);
            }
            System.out.println();
            System.out.println("UPDATE by WRONG userId & mealId");
            try {
                mealService.create(2, meal);
                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            } catch (Exception e) {
                System.out.println(e);
            }

            //test date&time generic

//            Meal mealForTime = new Meal(LocalDateTime.now(), "meal for time", 100500, 1);
//            System.out.println(DateTimeUtil.isBetweenHalfOpen(LocalTime.now(), LocalTime.MIN, LocalTime.MAX));
//            System.out.println(DateTimeUtil.isBetweenHalfOpen(LocalDate.now(), LocalDate.MIN, LocalDate.MAX));
            System.out.println(DateTimeUtil.isBetweenHalfOpen(LocalDate.parse("2022-02-19"),LocalDate.parse("2022-02-19"), LocalDate.parse("2022-02-19").plusDays(1)));
        }
    }

    private static List<Meal> getAll(int userId) {
        return mealService.getAll(LocalDate.MIN, LocalDate.MAX, LocalTime.MIN, LocalTime.MAX, userId);
    }
}
