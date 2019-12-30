package com.example.vingtsix;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;

import lyricview.src.main.java.me.zhengken.lyricview.LyricView;

//import com.example.vingtsix.uilibrary.src.main.java.me.zhehua.uilibrary.adapter.LyricAdapter;
//import com.example.vingtsix.uilibrary.src.main.java.me.zhehua.uilibrary.adapter.SimpleLyricAdapter;

public class Player extends AppCompatActivity {

    Bundle songExtraData;
    ArrayList<File> songFileList;
    SeekBar mSeekBar;
    TextView mSongTitle;
    ImageView playBtn;
    ImageView nextBtn;
    ImageView prevBtn;
    static MediaPlayer mMediaPlayer;
    int position;
    TextView currentTime;
    TextView totalTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);



        //step 2
        LyricView mLyricView = (LyricView)findViewById(R.id.custom_lyric_view);

//step 3
        File lyricFile = null;
        mLyricView.setLyricFile(lyricFile);

//step 4, update LyricView every interval
        long progress = 0;
        mLyricView.setCurrentTimeMillis(progress);

//step 5, implement the interface when user drag lyrics and click the play icon
        mLyricView.setOnPlayerClickListener(new LyricView.OnPlayerClickListener() {
            @Override
            public void onPlayerClicked(long progress, String content) {

            }
        });




        //setting resources

        mSeekBar = findViewById(R.id.musicSeekBar);
        mSongTitle = findViewById(R.id.songTitle);
        playBtn = findViewById(R.id.playBtn);
        prevBtn = findViewById(R.id.prevBtn);
        nextBtn = findViewById(R.id.nextbtn);
        currentTime = findViewById(R.id.currentTimer);
        totalTime = findViewById(R.id.totalTimer);


        //verifier si le mediaplayer est null ou pas

        if (mMediaPlayer != null){
            mMediaPlayer.stop();
        }

        Intent songData = getIntent();
        songExtraData = songData.getExtras();

        songFileList = (ArrayList) songExtraData.getParcelableArrayList("songFileList");
        position = songExtraData.getInt("position", 0);

        initMusciPlayer(position);//Start a MediaPlayer

        //parametrer les btn play pause

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //quand les btn play ou pause sont cliquee
                play();
            }
        });


        //parametrage du btn Suivant pour la chanson suivant
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (position<songFileList.size()-1){

                    // verification si la position actuelle du son dans la lsite est inferieur au total des son present dans la liste
                    // augmenter la position de un pour jouer le prochain son dans la liste

                    position++;

                }
                else {

                    //si la position est plus grande ou egal au numero des son dans la liste
                    //parametrer la position a 0

                    position = 0;

                }

                //jouer le son dans la lsite avec la position
                initMusciPlayer(position);

            }
        });


        //parametrage du Btn PRECEdante

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position <= 0){

                    //si la position du son dans la liste est inferieur ou egal a 0
                    position = songFileList.size()-1;

                }else{
                    position--;
                }

                initMusciPlayer(position);

            }
        });







    }

    private void initMusciPlayer(final int position){

        if (mMediaPlayer !=null && mMediaPlayer.isPlaying()){
            mMediaPlayer.reset();
        }

        String name = songFileList.get(position).getName().toString().replace(".mp3","").replace(".wav", "").replace(".mp4a","");
        mSongTitle.setText(name);


        // get song on the sdcard
        Uri songResourseUri = Uri.parse(songFileList.get(position).toString());

        //creation d'un Mediaplayer

        mMediaPlayer = MediaPlayer.create(getApplicationContext(), songResourseUri);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                //parametrage de la duree max du seekBar
                mSeekBar.setMax(mMediaPlayer.getDuration());

                //parametrage du temps max
                String totTime = createTimerLabel(mMediaPlayer.getDuration());
                totalTime.setText(totTime);

                //je commence a joouer la musique
                mMediaPlayer.start();

                //parametrage icone pause

                   playBtn.setImageResource(R.drawable.pause_btn);



            }
        });

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // faire quelque chose quqnd le song est fini d'etre jouer
                //pour le moment je vais juste l'icone pause en play icon

                //playBtn.setImageResource(R.drawable.play_btn);

               // repeter la lecture quand ca fini

                int currentSongPosition = position;

                if (currentSongPosition<songFileList.size()-1){

                    // verification si la position actuelle du son dans la lsite est inferieur au total des son present dans la liste
                    // augmenter la position de un pour jouer le prochain son dans la liste

                    currentSongPosition++;

                }
                else {

                    //si la position est plus grande ou egal au numero des son dans la liste
                    //parametrer la position a 0

                    currentSongPosition = 0;

                }

                //jouer le son dans la lsite avec la position
                initMusciPlayer(currentSongPosition);



            }
        });

        // parameter le seekBar
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //faire quelque chose quand le seekBar est changee

                if (fromUser) {
                    mMediaPlayer.seekTo(progress); // seeker le song
                    mSeekBar.setProgress(progress); // parametrer la progression du seekBar
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        //paramtrage du seekBar pour qu'elle progresse avec le son
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mMediaPlayer != null){
                    try {
                        if (mMediaPlayer.isPlaying()){
                            Message message = new Message();
                            message.what = mMediaPlayer.getCurrentPosition();
                            handler.sendMessage(message);
                            Thread.sleep(1000);
                        }
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    // creeation d'unn gestionnaire pour parameter la progression

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){

            currentTime.setText(createTimerLabel(msg.what));// parametrage du timer

            mSeekBar.setProgress(msg.what);
        }
    };

    private void play(){
        if (mMediaPlayer != null &&mMediaPlayer.isPlaying()){
            mMediaPlayer.pause();
            playBtn.setImageResource(R.drawable.play_btn);
        }
        else {
            mMediaPlayer.start();
            playBtn.setImageResource(R.drawable.pause_btn);
        }

    }

    public String createTimerLabel (int duration){
        String timerLabel = "";
        int min = duration / 1000 / 60;
        int sec = duration / 1000 %60;

        timerLabel +=min +":";

        if (sec<10) timerLabel +="0";
        timerLabel += sec;

        return timerLabel;

    }

}

