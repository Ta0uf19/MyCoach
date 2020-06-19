package io.mycoach.model;

import androidx.databinding.BaseObservable;

import com.google.firebase.firestore.Exclude;
import com.stfalcon.chatkit.commons.models.IUser;

import java.io.Serializable;

public class User extends BaseObservable implements IUser, Serializable {

    private String id;
    private String email;
    private String name;
    private String password;
    private String avatar;

    @Exclude
    private boolean isAuthenticated;
    @Exclude
    private boolean isNew, isCreated;

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.avatar = email;
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

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public boolean isCreated() {
        return isCreated;
    }

    public void setCreated(boolean created) {
        isCreated = created;
    }

}
