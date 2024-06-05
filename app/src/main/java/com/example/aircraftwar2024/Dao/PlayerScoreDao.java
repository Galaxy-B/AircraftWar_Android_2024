package com.example.aircraftwar2024.Dao;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface PlayerScoreDao {
//    List<PlayerScore> getAllScores();
//    String[][] getAllScores_table();
//    void doAdd(PlayerScore playerScore, int level);
//    void doDelete(List<PlayerScore> playerScores, String time,int level);
//    void PrintRank(int level) throws IOException;

    void addItem(PlayerScore scoreInfo, File scoreFile);
    void getAllItems(File scoreFile);
    void sortByScore();
    String[][] outPutItems();
    void deleteByTime(String[][] str, File scoreFile);
}
