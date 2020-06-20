package io.mycoach.model;
import java.io.Serializable;

@SuppressWarnings("serial")
public class Workout implements Serializable {

    private String name;
    private String desc;
    private String picture;
    private int repeats;
    private int duration; // minutes
    private int sets;


    /**
     * Create a workout exercice
     *
     * @param name name of exercice
     * @param picture avatar
     * @param repeats repetition of exercice per set
     * @param sets repetition of exercice
     * @param duration in minutes
     */
    public Workout(String name, String picture, int repeats, int sets, int duration) {
        this.name = name;
        this.picture = picture;
        this.repeats = repeats;
        this.sets = sets;
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
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

    public int getSets() {
        return sets;
    }

    @Override
    public String toString() {
        return "Workout{" +
                "name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", picture='" + picture + '\'' +
                ", repeats=" + repeats +
                ", duration=" + duration +
                ", sets=" + sets +
                '}';
    }
}
