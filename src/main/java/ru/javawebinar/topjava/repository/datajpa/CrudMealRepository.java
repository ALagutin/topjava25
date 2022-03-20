package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    static List<Meal> getAll(int userId) {
        return null;
    }

    List<Meal> findAllByUserId(Sort sort, int userId);

    Meal findByIdAndUserId(int id, int userId);

    @Transactional
    Meal save(Meal meal);

    @Transactional
    int deleteByIdAndUserId(int id, int userId);

    List<Meal> findAllByUserIdAndDateTimeGreaterThanEqualAndDateTimeLessThan(Sort sort, int userId, LocalDateTime startTime, LocalDateTime endTime);
}
