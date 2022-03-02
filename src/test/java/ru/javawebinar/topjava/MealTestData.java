package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {

    public static final int MEAL1_ID = AbstractBaseEntity.START_SEQ + 3;
    public static final int MEAL2_ID = AbstractBaseEntity.START_SEQ + 4;
    public static final int MEAL3_ID = AbstractBaseEntity.START_SEQ + 5;
    public static final LocalDate startDate = LocalDate.of(2022, 3, 2);
    public static final LocalDate endDate = LocalDate.of(2022, 3, 3);

    public static Meal userMeal1 = new Meal(MEAL1_ID, LocalDateTime.of(2022,3,2,10,0), "user meal 1", 1000);
    public static Meal userMeal2 = new Meal(MEAL2_ID, LocalDateTime.of(2022,3,2,15,0), "user meal 2", 1200);
    public static Meal userMeal3 = new Meal(MEAL3_ID, LocalDateTime.of(2022,3,1,12,0), "user meal 3", 1500);

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }

    public static Meal getUpdatedMeal() {
        return new Meal(MEAL1_ID, LocalDateTime.of(2035,5,5,10,0), "user meal 1 updated", 2000);
    }

    public static Meal getNewMeal() {
        return new Meal(LocalDateTime.of(2022, 1, 1, 1, 1), "new meal", 10000);
    }
}
