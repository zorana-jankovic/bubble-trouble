package models;

public class User {
    private String name;
    private long scores;
    private long level;

    public User(String name, long scores){
        this.name = name;
        this.scores = scores;
        this.level = 0;
    }

    public User(String name, long scores, long level){
        this.name = name;
        this.scores = scores;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getScores() {
        return scores;
    }

    public void setScores(long scores) {
        this.scores = scores;
    }

    public long getLevel() {
        return level;
    }

    public void setLevel(long level) {
        this.level = level;
    }
}
