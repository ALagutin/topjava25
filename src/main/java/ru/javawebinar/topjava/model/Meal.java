package ru.javawebinar.topjava.model;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@NamedQueries({
        @NamedQuery(name = Meal.DELETE, query = "DELETE FROM Meal u WHERE u.id=:id AND u.user.id =:user_id"),
        @NamedQuery(name = Meal.FIND, query = "SELECT u FROM Meal u JOIN FETCH u.user WHERE u.id=:id AND u.user.id =:user_id"),
        @NamedQuery(name = Meal.UPDATE, query = "UPDATE Meal u SET u.dateTime=: dateTime, u.description=: description, u.calories=: calories WHERE u.id =: id AND u.user.id=:user_id"),
        @NamedQuery(name = Meal.ALL_SORTED, query = "SELECT u FROM Meal u JOIN FETCH u.user WHERE u.user.id=:user_id ORDER BY u.dateTime desc"),
        @NamedQuery(name = Meal.ALL_FILTERED_BY_DATE_SORTED, query = "SELECT u FROM Meal u JOIN FETCH u.user WHERE u.user.id=:user_id AND u.dateTime >=:start_date AND u.dateTime <:end_date ORDER BY u.dateTime desc")
})
@Entity
@Table(name = "meals")
public class Meal extends AbstractBaseEntity {

    public static final String DELETE = "Meal.delete";
    public static final String FIND = "Meal.find";
    public static final String ALL_SORTED = "Meal.getAllSorted";
    public static final String ALL_FILTERED_BY_DATE_SORTED = "Meal.getAllFilteredByDateSorted";
    public static final String UPDATE = "Meal.update";

    @Column(name = "date_time", nullable = false, columnDefinition = "timestamp")
//    @NotBlank
    private LocalDateTime dateTime;

    @Column(name = "description", nullable = false)
//    @NotBlank
    private String description;

    @Column(name = "calories", nullable = false)
//    @NotBlank
    @Range(min = 10, max = 10000)
    private int calories;

    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    public Meal() {
    }

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this(null, dateTime, description, calories);
    }

    public Meal(Integer id, LocalDateTime dateTime, String description, int calories) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public Meal(Integer id, LocalDateTime dateTime, String description, int calories, User user) {
        this(id, dateTime, description, calories);
        this.user = user;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", userId=" + (user == null ? null : user.getId()) +
//                ", user=" + user +
                '}';
    }
}
