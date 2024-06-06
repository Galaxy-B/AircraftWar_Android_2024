package com.example.aircraftwar2024.Dao;

import java.util.Date;
import java.io.*;

public class PlayerScore implements Serializable
{
    private static final long serialVersionUID = 1L;

    // 玩家昵称
    private String player_name;

    // 得分
    private int score;

    // 日期
    private Date record_date;

    public PlayerScore(String name, int score, Date date)
    {
        this.player_name = name;
        this.score = score;
        this.record_date = date;
    }

    public String get_player_name()
    {
        return player_name;
    }

    public int get_score()
    {
        return score;
    }

    public Date get_record_date()
    {
        return record_date;
    }

    public void set_player_name(String name)
    {
        this.player_name = name;
    }

    public void set_score(int score)
    {
        this.score = score;
    }

    public void set_record_date(Date date)
    {
        this.record_date = date;
    }
}
