package com.goke.media.ui.call;

import android.content.Context;
import android.media.MediaPlayer;

import com.goke.media.R;

// use MediaPlayer or SoundPool
public class Sound {
    private static MediaPlayer mediaPlayer = null;

    public static void Play(Context ctx, CallDir dir){

        int fileId = R.raw.outgoing;
        if(dir== CallDir.INCOMING){
            fileId = R.raw.incoming;
        }

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {return;}
        mediaPlayer = MediaPlayer.create(ctx, fileId);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (mediaPlayer == null) {return;}
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
            }
        });
    }

    public static void StopPlay() {
        if (null == mediaPlayer) return;
        if(mediaPlayer.isPlaying())
            mediaPlayer.stop();

        mediaPlayer.release();
        mediaPlayer = null;
    }
}

enum CallDir {
    OUTGOING,
    INCOMING
}
