package com.example.Player;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button playBtn;
    SeekBar positionBar;
    SeekBar volumeBar;
    TextView elapsedTimeLabel;
    TextView remainingTimeLabel;
    MediaPlayer mp;
    int TotalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playBtn = (Button) findViewById(R.id.playBtn);
        elapsedTimeLabel = (TextView) findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel = (TextView) findViewById(R.id.remainingTimeLabel);

        //Media Player

        mp = MediaPlayer.create(this, R.raw.m1);
        mp.setLooping(true);
        mp.seekTo(0);
        mp.setVolume(0.5f, 0.5f);
        TotalTime = mp.getDuration();

        //Position Bar

        positionBar = (SeekBar) findViewById(R.id.positionBar);
        positionBar.setMax(TotalTime);
        positionBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if(fromUser){
                            mp.seekTo(progress);
                            positionBar.setProgress(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );



        //Volume bar
        volumeBar = (SeekBar) findViewById(R.id.volumeBar);
        volumeBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        float volumeNum = progress/100f;
                        mp.setVolume(volumeNum, volumeNum);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        // thread (Update possitionBar & timeLabel)

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mp != null){
                    try {
                        Message msg = new Message();
                        msg.what = mp.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    }catch (InterruptedException e){

                    }
                }
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.lista, menu);
        return true;
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;
            //Update positionBar
            positionBar.setProgress(currentPosition);

            //Update Labels
            String elapsedTime = createTimeLabel(currentPosition);
            elapsedTimeLabel.setText(elapsedTime);

            String remainingTime = createTimeLabel(TotalTime-currentPosition);
            remainingTimeLabel.setText(" - "+remainingTime);

        }
    };

    public String createTimeLabel(int time){
        String timeLabel = "";
        int min = time/1000/60;
        int sec = time/1000%60;

        timeLabel = min + ":";
        if (sec < 10)timeLabel += "0";
        timeLabel += sec;

        return timeLabel;

    }

    public void playbtnClick(View view) {

        if (!mp.isPlaying()){
            //Stopping
            mp.start();
            playBtn.setBackgroundResource(R.drawable.stop);

        }else{

            //play
            mp.pause();
            playBtn.setBackgroundResource(R.drawable.play);
        }
    }
    public void btn_lista(MenuItem item) {
        Intent intent = new   Intent(this, lista.class);
        startActivity(intent);
    }
}
