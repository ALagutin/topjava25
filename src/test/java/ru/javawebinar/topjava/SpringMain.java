package ru.javawebinar.topjava;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.AbstractEnvironment;
import ru.javawebinar.topjava.web.meal.MealRestController;

import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "datajpa, postgres");
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("classpath:spring/spring-app.xml", "classpath:spring/spring-db.xml")) {
            Arrays.stream(appCtx.getBeanDefinitionNames()).forEach(System.out::println);
//            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
//            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));
//            System.out.println();

//            MealRestController mealController = appCtx.getBean(MealRestController.class);
//            List<MealTo> filteredMealsWithExcess =
//                    mealController.getBetween(
//                            LocalDate.of(2020, Month.JANUARY, 30), LocalTime.of(7, 0),
//                            LocalDate.of(2020, Month.JANUARY, 31), LocalTime.of(11, 0));
//            filteredMealsWithExcess.forEach(System.out::println);
//            System.out.println();
//            System.out.println(mealController.getBetween(null, null, null, null));

            Arrays.asList(appCtx.getBeanDefinitionNames()).forEach(System.out::println);
            System.getProperty("spring.profiles.active");
            MealRestController controller = appCtx.getBean(MealRestController.class);
            controller.getAll().forEach(System.out::println);
            System.out.println(controller.get(100003));
        }
    }
}
