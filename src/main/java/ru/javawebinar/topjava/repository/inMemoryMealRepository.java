package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class inMemoryMealRepository implements MealRepository{
    private ConcurrentHashMap<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

     public inMemoryMealRepository() {
         List<Meal> meals = MealsUtil.getTestMeals();
         for (Meal meal : meals) {
             meal.setId(getNewId());
             repository.put(meal.getId(), meal);
         }

     }

    @Override
    public Meal save(Meal meal) {
        Integer id = meal.getId();
        if (id == null) {
            id = getNewId();
            meal.setId(id);
        }
        return repository.put(id, meal);
    }

    @Override
    public boolean delete(int id) {
        repository.remove(id);
         return true;
    }

    @Override
    public Meal get(int id) {
        return repository.get(id);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(repository.values());
    }

    private int getNewId() {
         return counter.getAndIncrement();
    }

}
