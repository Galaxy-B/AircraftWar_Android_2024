package com.example.aircraftwar2024.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.aircraftwar2024.Dao.PlayerScore;
import com.example.aircraftwar2024.Dao.PlayerScoreDao;
import com.example.aircraftwar2024.Dao.ScoreDaoImpl;
import com.example.aircraftwar2024.R;
import com.example.aircraftwar2024.activityManager.ActivityManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class RecordActivity extends AppCompatActivity implements View.OnClickListener
{
    private String gameType;
    private int score;
    private List<PlayerScore> records;

    private String file;
    private PlayerScoreDao playerScoreDao;
    private ListView list;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        ActivityManager.getActivityManager().addActivity(this);

        intent = new Intent(RecordActivity.this, MainActivity.class);

        Button jump_back = findViewById(R.id.button);
        jump_back.setOnClickListener(this);

        // 获取游戏难度
        gameType = getIntent().getStringExtra("mode");
        // 获取本次游戏的分数
        score = Integer.parseInt(getIntent().getStringExtra("score"));
        TextView degree = findViewById(R.id.pattern);

        switch(gameType)
        {
            case "A":
                file = "easy_game.dat";
                degree.setText("简单模式");
                break;
            case "B":
                file = "medium_game.dat";
                degree.setText("普通模式");
                break;
            case "C":
                file = "hard_game.dat";
                degree.setText("困难模式");
                break;
        }

        //获得Layout里面的ListView
        list = (ListView) findViewById(R.id.record);

        // 实例化DAO 此过程已经完成文件的读取
        playerScoreDao = new ScoreDaoImpl(file, this);

        // 将本次游戏的得分写入记录
        playerScoreDao.add_record("test", score);

        // 利用从文件中读取的数据更新列表
        records = playerScoreDao.get_all_record();

        // 更新排行榜
        update_adapter_view();

        // 将新记录写入文件
        playerScoreDao.save_record();

        //添加单击监听
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
            {
                AlertDialog builder = new AlertDialog.Builder(RecordActivity.this)
                        //设置对话框的按钮文本及按钮监听器
                        .setMessage("是否确定删除？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                // 删除对应位置的记录
                                playerScoreDao.delete_record(position);
                                // 更新排行榜
                                update_adapter_view();
                                // 将新纪录写入文件
                                playerScoreDao.save_record();
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
            }
        });
    }

    private void update_adapter_view()
    {
        ArrayList<Map<String, Object>> list_item = new ArrayList<Map<String, Object>>();

        // 对输出日期格式化
        DateFormat df = new SimpleDateFormat("MM-dd HH:mm");

        int rank = 1;
        for (PlayerScore record : records)
        {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("rank", rank++);
            map.put("user", record.get_player_name());
            map.put("score", record.get_score());
            map.put("time", df.format(record.get_record_date()));
            list_item.add(map);
        }

        //生成适配器的Item和动态数组对应的元素
        SimpleAdapter listItemAdapter = new SimpleAdapter(
                this,
                list_item,
                R.layout.activity_item,
                new String[]{"rank","user","score","time"},
                new int[]{R.id.rank,R.id.user,R.id.score,R.id.time}
        );
        //添加并显示
        list.setAdapter(listItemAdapter);
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.button)
        {
            ActivityManager.getActivityManager().finishAllActivity();
            startActivity(intent);
        }
    }
}