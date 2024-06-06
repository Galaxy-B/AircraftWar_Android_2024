package com.example.aircraftwar2024.Dao;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface PlayerScoreDao
{
    // 获取所有记录
    public List<PlayerScore> get_all_record();

    // 添加新的记录
    public void add_record(String player_name, int score);

    // 按给定id删除对应的记录
    public void delete_record(int id);

    // 将records覆写入文件
    public void save_record();
}
