package io.mycoach.model;

import java.io.Serializable;
import java.time.Duration;

@SuppressWarnings("serial")
public class Workout implements Serializable {

    private String name;
    private String desc;
    private String picture;
    private int repeats;
    private int sets;


    public Workout(String name, String picture, int repeats, int sets) {
        this.name = name;
        this.picture = picture;
        this.repeats = repeats;
        this.sets = sets;
    }


    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getPicture() {
        return picture;
    }

    public int getRepeats() {
        return repeats;
    }

    public int getSeries() {
        return sets;
    }

    @Override
    public String toString() {
        return "Workout{" +
                "name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", picture='" + picture + '\'' +
                ", repeats=" + repeats +
                ", series=" + sets +
                '}';
    }
}
