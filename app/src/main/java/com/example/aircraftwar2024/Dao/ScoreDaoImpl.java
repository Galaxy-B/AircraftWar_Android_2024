package com.example.aircraftwar2024.Dao;

import android.content.Context;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class ScoreDaoImpl implements PlayerScoreDao {
    private List<PlayerScore> scoreInfos;
    private Context context;
    private String file;

    public ScoreDaoImpl(Context context, String file) throws IOException, ClassNotFoundException {
        this.context = context;
        this.file = file;

        File dataFile = new File(context.getFilesDir(), file);
        if(dataFile.exists()) {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile));
            this.scoreInfos = (List<PlayerScore>) ois.readObject();
        } else {
            this.scoreInfos = new ArrayList<>();
        }

    }

    public void deleteByTime(String[][] str, File scoreFile) {
        OutputStream fOut = null;
        OutputStreamWriter writer = null;

        try {
            // 构建FileOutputStream对象,文件不存在会自动新建
            fOut = new FileOutputStream(scoreFile);

            // 构建OutputStreamWriter对象,参数可以指定编码,默认为操作系统默认编码
            writer = new OutputStreamWriter(fOut, StandardCharsets.UTF_8);

            for (int i = 0; i < str.length - 1; i++) {
                if (!Objects.equals(str[i][3], "delete")) {
                    for (int j = 1; j < str[i].length; j++) {
                        writer.append(str[i][j]).append("\t");
                    }
                    writer.append("\n");
                }
            }

            // 刷新缓冲区
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                //关闭写入流,同时会把缓冲区内容写入文件
                assert writer != null;
                writer.close();

                //关闭输出流,释放系统资源
                assert fOut != null;
                fOut.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
//
//    @Override
//    public void doAdd(PlayerScore playerScore,int level){
//        playerScoreList.add(playerScore);
//    }

    public void addItem(PlayerScore scoreInfo, File scoreFile) {
        OutputStream fOut = null;
        OutputStreamWriter writer = null;

        try {
            // 构建FileOutputStream对象,文件不存在会自动新建
            fOut = new FileOutputStream(scoreFile, true);

            // 构建OutputStreamWriter对象,参数可以指定编码,默认为操作系统默认编码
            writer = new OutputStreamWriter(fOut, "UTF-8");

            // 写入到缓冲区
            writer.append(scoreInfo.getPlayerName()).append("\t");
            writer.append(String.valueOf(scoreInfo.getPlayerScore())).append("\t");
            writer.append(scoreInfo.getTime()).append("\n");

            // 刷新缓冲区
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                //关闭写入流,同时会把缓冲区内容写入文件
                writer.close();

                //关闭输出流,释放系统资源
                fOut.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void getAllItems(File scoreFile) {
        BufferedReader reader = null;
        FileReader fileReader = null;

        try {
            fileReader = new FileReader(scoreFile);
            reader = new BufferedReader(fileReader);
            String line = "";
            while ((line = reader.readLine()) != null) {
                // 按 \t 分割，存在parts里
                String[] parts = line.split("\t");

                // 写入数据对象
                PlayerScore new_data = new PlayerScore();
                new_data.setPlayerName(parts[0]);
                new_data.setScore(Integer.parseInt(parts[1]));
                new_data.setTime(parts[2]);
                scoreInfos.add(new_data);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (fileReader != null) {
                    fileReader.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void sortByScore() {
        scoreInfos.sort((b, a) -> {
            return a.getPlayerScore() - b.getPlayerScore();
        });
    }

    public String[][] outPutItems() {
        int cnt = 0;
        String[][] str = new String[scoreInfos.size()][4];
        for (PlayerScore s : scoreInfos) {
            str[cnt][0] = String.valueOf(cnt + 1);
            str[cnt][1] = s.getPlayerName();
            str[cnt][2] = String.valueOf(s.getPlayerScore());
            str[cnt][3] = s.getTime();
            cnt++;
        }
        return str;
    }
}


