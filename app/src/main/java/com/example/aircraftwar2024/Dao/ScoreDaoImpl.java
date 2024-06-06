package com.example.aircraftwar2024.Dao;

import android.content.Context;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ScoreDaoImpl implements PlayerScoreDao
{
    private Context context;

    // 从文件中读出的所有记录
    private List<PlayerScore> records;

    // 文件的路径
    private String path;

    // 对日期进行格式化
    DateFormat df = new SimpleDateFormat("MM-dd HH:mm");

    // 在构造函数中执行读文件的操作
    public ScoreDaoImpl(String path, Context context)
    {
        this.path = path;
        this.context = context;
        records = new ArrayList<>();

        try
        {
            // 创建ObjectInputStream对象
            File datafile = new File(context.getFilesDir() ,path);

            if (datafile.exists())
            {
                FileInputStream fileIn = new FileInputStream(datafile);
                ObjectInputStream ois = new ObjectInputStream(fileIn);
                // 反序列化所有记录
                records.addAll((List<PlayerScore>)ois.readObject());
                // 关闭读取流
                ois.close();
                fileIn.close();
            }
            else
            {
                save_record();
            }
        }
        catch (Exception e)
        {
            System.out.println("fail to open the file!");
        }
    }

    @Override
    public List<PlayerScore> get_all_record()
    {
        // 将records按照score降序排序
        Comparator<PlayerScore> scoreComparator = Comparator.comparing(PlayerScore::get_score);
        records.sort(scoreComparator.reversed());

        return records;
    }

    @Override
    public void add_record(String player_name, int score)
    {
        // 获取本次成绩的记录时间
        Date date = new Date();

        PlayerScore record = new PlayerScore(player_name, score, date);
        records.add(record);
    }

    @Override
    public void delete_record(int position)
    {
        records.remove(position);
    }

    @Override
    public void save_record()
    {
        try
        {
            // 创建ObjectOutputStream对象
            FileOutputStream fileOut = new FileOutputStream(new File(context.getFilesDir() ,path));
            ObjectOutputStream oos = new ObjectOutputStream(fileOut);

            // 序列化所有记录
            oos.writeObject(records);

            // 关闭流
            oos.close();
            fileOut.close();
        }
        catch (Exception e)
        {
            System.out.println("fail to open the file!");
        }
    }
}


