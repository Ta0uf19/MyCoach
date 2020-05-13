package io.mycoach.model;

import androidx.databinding.BaseObservable;

import com.stfalcon.chatkit.commons.models.IUser;

public class User extends BaseObservable implements IUser {

    private String name;
    private String password;
    private String id;
    private String avatar;

    public User(String id, String name, String avatar) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
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


    public void setName(String name) {
        this.name = name;
    }
}
