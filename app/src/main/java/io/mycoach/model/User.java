package io.mycoach.model;

import androidx.databinding.BaseObservable;
import com.stfalcon.chatkit.commons.models.IUser;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class User extends BaseObservable implements IUser, Serializable {

    private String id;
    private String email;
    private String name;
    private String password;
    private String avatar;
    private String type_workout;
    private String[] weekly_days;
    private String weekly_workout;
    private Map<String, String> workouts;
    private boolean isNew;

    public User() {
        this.id = UUID.randomUUID().toString().replace("-", "");
    }
    public User(String id) {
        this.id = id;
        this.workouts = new HashMap<String, String>();
    }
    public User(String email, String password) {
        this(UUID.randomUUID().toString());
        this.password = password;
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getType_workout() {
        return type_workout;
    }

    public void setType_workout(String type_workout) {
        this.type_workout = type_workout;
    }

    public String[] getWeekly_days() {
        return weekly_days;
    }

    public void setWeekly_days(String[] weekly_days) {
        this.weekly_days = weekly_days;
    }

    public String getWeekly_workout() {
        return weekly_workout;
    }

    public void setWeekly_workout(String weekly_workout) {
        this.weekly_workout = weekly_workout;
    }

    public Map<String, String> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(Map<String, String> workouts) {
        this.workouts = workouts;
    }


}
