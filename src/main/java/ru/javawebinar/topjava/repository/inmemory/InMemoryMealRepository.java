package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    //    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal.getUserId(), meal));
    }

    @Override
    public Meal save(int userId, Meal meal) {

        repository.computeIfAbsent(userId, (id) -> new ConcurrentHashMap<>());
        Map<Integer, Meal> mealMap = repository.get(userId);
        Meal savedMeal;

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            mealMap.put(meal.getId(), meal);
            savedMeal = meal;
        } else {
            if (!meal.getUserId().equals(userId)) {
                return null;
            }
            // handle case: update, but not present in storage
            savedMeal = mealMap.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        }

        if (savedMeal != null) {
            repository.put(userId, mealMap);
        }

        return savedMeal;
    }

    @Override
    public boolean delete(int userId, int id) {
        Map<Integer, Meal> mealMap = repository.get(userId);
        if (mealMap == null || mealMap.get(id) == null || !mealMap.get(id).getUserId().equals(userId) || mealMap.remove(id) == null) {
            return false;
        }

        repository.put(userId, mealMap);
        return true;
    }

    @Override
    public Meal get(int userId, int id) {

        Map<Integer, Meal> mealMap = repository.get(userId);

        if (mealMap == null) {
            return null;
        }

        return mealMap.get(id);
    }

    @Override
    public List<Meal> getAll(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, int userId) {

        Map<Integer, Meal> mapMeal = repository.get(userId);

        if (mapMeal == null) {
            return new ArrayList<>();
        }

        List<Meal> meals = mapMeal.values()
                .stream()
                .filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDate(), startDate, endDate))
                .filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime))
                .sorted(Comparator.comparing(Meal::getDateTime)).collect(Collectors.toList());
        return meals;
    }
}

