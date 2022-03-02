package ru.javawebinar.topjava.service;

import org.assertj.core.condition.Not;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(MEAL1_ID, USER_ID);
        assertMatch(meal, userMeal1);
    }

    @Test
    public void getWrongUserIdOrMealId() {
        Assert.assertThrows(NotFoundException.class, () -> service.get(MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void delete() {
        service.delete(MEAL1_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(MEAL1_ID, USER_ID));
    }

    @Test
    public void deleteWrongUserIdOrMealId() {
        Assert.assertThrows(NotFoundException.class, () -> service.delete(MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> meals = service.getBetweenInclusive(startDate, endDate, USER_ID);
        assertMatch(meals, userMeal2, userMeal1);
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(USER_ID);
        assertMatch(all, userMeal2, userMeal1, userMeal3);
    }

    @Test
    public void update() {
        Meal meal = getUpdatedMeal();
        service.update(meal, USER_ID);
        assertMatch(service.get(meal.getId(), USER_ID), getUpdatedMeal());
    }

    @Test
    public void updateWrongUserIdOrMealId() {
        Meal meal = getUpdatedMeal();
        Assert.assertThrows(NotFoundException.class, () -> service.update(meal, ADMIN_ID));
    }

    @Test
    public void create() {
        Meal createdMeal = service.create(getNewMeal(), USER_ID);
        Meal expectedMeal = getNewMeal();
        int newId = createdMeal.getId();
        expectedMeal.setId(newId);
        assertMatch(createdMeal, expectedMeal);
        assertMatch(service.get(newId, USER_ID), expectedMeal);
    }

    @Test
    public void createDuplicateDateTime() {
        service.create(getNewMeal(), USER_ID);
        Assert.assertThrows(DataAccessException.class, () -> service.create(getNewMeal(), USER_ID));
    }

}