package com.example.aircraftwar2024.music;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

import com.example.aircraftwar2024.R;

import java.util.HashMap;

public class MySoundPool {

    private boolean is_music_on = false;
    private SoundPool mysp;
    private AudioAttributes audioAttributes = null;
    private HashMap<Integer, Integer> spMap;

    public MySoundPool(Context context, boolean music_state){
        is_music_on = music_state;

        audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        mysp = new SoundPool.Builder()
                .setMaxStreams(10)
                .setAudioAttributes(audioAttributes)
                .build();

        spMap = new HashMap<Integer, Integer>();
        spMap.put(1, mysp.load(context, R.raw.bullet_hit, 1));
        spMap.put(2, mysp.load(context, R.raw.bomb_explosion, 1));
        spMap.put(3, mysp.load(context, R.raw.get_supply, 1));
        spMap.put(4, mysp.load(context, R.raw.game_over, 1));
    }

    public void playMusic(int index){
        if(is_music_on){
            mysp.play(spMap.get(index), 1, 1, 0, 0, 1.2f);
        }
    }

}
