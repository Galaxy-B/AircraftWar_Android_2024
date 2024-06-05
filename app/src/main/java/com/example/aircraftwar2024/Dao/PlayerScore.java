package com.example.aircraftwar2024.Dao;

public class PlayerScore {
    private String playerName;
    private int score;
    private String time;
    private String degree;//难度


    public String getPlayerName() { return playerName; }
    public String getDegree(){ return degree; }
    public String getTime(){ return time; }
    public int getPlayerScore(){ return score; }
    public void setPlayerName(String playerName){
        this.playerName = playerName;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public void setTime(String time) {
        this.time = time;
    }
}
