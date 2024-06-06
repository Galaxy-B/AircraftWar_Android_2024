package com.example.aircraftwar2024.music;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.aircraftwar2024.R;

public class MyMediaPlayer
{
    private MediaPlayer bgmMP;
    private MediaPlayer bsmMP;
    private boolean is_music_on = false;

    public MyMediaPlayer(Context context, boolean music_state)
    {
        bgmMP = MediaPlayer.create(context, R.raw.bgm);
        bsmMP = MediaPlayer.create(context, R.raw.bgm_boss);
        is_music_on = music_state;
    }

    public void startBGM()
    {
        if (is_music_on)
        {
            bgmMP.start();
            bgmMP.setLooping(true);
        }
    }

    public void startBSM()
    {
        if (is_music_on)
        {
            bsmMP.start();
            bsmMP.setLooping(true);
        }
    }

    public void pauseBGM()
    {
        if (is_music_on)
        {
            bgmMP.pause();
        }
    }

    public void stopBSM()
    {
        if (is_music_on)
        {
            bsmMP.stop();
        }
    }

    public void continueBGM()
    {
        if (is_music_on)
        {
            int position = bgmMP.getCurrentPosition();
            bgmMP.seekTo(position);
            bgmMP.start();
        }
    }

    public void stopMusic()
    {
        if (is_music_on)
        {
            bgmMP.stop();
            bsmMP.stop();
        }
        bgmMP.release();
        bsmMP.release();
        bgmMP = null;
        bsmMP = null;
    }
}
