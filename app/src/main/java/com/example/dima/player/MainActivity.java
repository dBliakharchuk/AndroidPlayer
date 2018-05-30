package com.example.dima.player;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    Button playBtn;
    Button nextBtn;
    Button previousBtn;
    SeekBar positionBar;
    SeekBar volumeBar;
    TextView elapsedTimeLabel;
    TextView remainingTimeLabel;
    MediaPlayer mp;
    int totalTime;

    int curPosition = 0;
    int curBarPosition = 0;
    int curSong = R.raw.closer;

    LayoutInflater inflater;

    public static ArrayList<Composition> list_composition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playBtn = (Button) findViewById(R.id.playBtn);
        nextBtn = (Button) findViewById(R.id.nextButton);
        previousBtn = (Button) findViewById(R.id.previousButton);
        elapsedTimeLabel = (TextView) findViewById(R.id.elapsedTimeLabel);
        remainingTimeLabel = (TextView) findViewById(R.id.remainingTimeLabel);

        recyclerView = (RecyclerView) findViewById(R.id.recycleViewID);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        inflater = this.getLayoutInflater();

        if (savedInstanceState == null) {
            list_composition = (ArrayList<Composition>) getListComposition();
        }
        mAdapter = new RecycleViewAdapter(MainActivity.this, list_composition);
        recyclerView.setAdapter(mAdapter);

        createMediaPlayer(curSong, false, 0);
        changePositionBar();
        changeVolumeVar();
        nextBtn();
        previousBtn();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mp != null) {
                    try {
                        Message msg = new Message();
                        msg.what = mp.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(500);
                    } catch (InterruptedException e) {}
                }
            }
        }).start();

    }

    private void nextBtn() {
        nextBtn.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                curBarPosition = mp.getCurrentPosition();
                if ((curBarPosition + 10000) >= totalTime){

                    mp.seekTo(totalTime);
                }
                else {
                    curBarPosition += 10000;
                    mp.seekTo(curBarPosition);
                }
            }
        });
    }

    private void previousBtn() {
        previousBtn.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                curBarPosition = mp.getCurrentPosition();
                if (curBarPosition - 10000 <= 0) {
                    mp.seekTo(0);
                }
                else {
                    curBarPosition -= 10000;
                    mp.seekTo(curBarPosition);

                }
            }
        });
    }



    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;

            // Update positionBar.
            positionBar.setProgress(currentPosition);

            // Update Labels.
            String elapsedTime = createTimeLabel(currentPosition);
            elapsedTimeLabel.setText(elapsedTime);

            String remainingTime = createTimeLabel(totalTime-currentPosition);
            remainingTimeLabel.setText("- " + remainingTime);
        }
    };

    public String createTimeLabel(int time) {
        String timeLabel = "";
        int min = time / 1000 / 60;
        int sec = time / 1000 % 60;

        timeLabel = min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;

        return timeLabel;
    }

    private void changePositionBar(){
        positionBar = (SeekBar) findViewById(R.id.positionBar);
        positionBar.setMax(totalTime);
        positionBar.setOnSeekBarChangeListener(
            new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
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
    }

    private void changeVolumeVar(){
        volumeBar = (SeekBar) findViewById(R.id.volumeBar);
        volumeBar.setOnSeekBarChangeListener(
            new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    float volumeNum = progress / 100f;
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
    }

    private void releaseMD() {
        if (mp != null) {
            try {
                mp.release();
                mp = null;
                playBtn.setBackgroundResource(R.drawable.play);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void createMediaPlayer(int song, boolean tempBool, int curPos){
        releaseMD();
        curSong = song;
        curPosition = curPos;

        mp = MediaPlayer.create(this, song);
        mp.setLooping(true);
        mp.seekTo(0);
        mp.setVolume(0.5f, 0.5f);
        totalTime = mp.getDuration();

        if (tempBool) mp.start();
        if (mp.isPlaying()) playBtn.setBackgroundResource(R.drawable.stop);
        changePositionBar();
        changeVolumeVar();
    }

    public void playBtnClick(View view) {

        if (!mp.isPlaying()) {
            mp.start();
            playBtn.setBackgroundResource(R.drawable.stop);

        } else {
            mp.pause();
            playBtn.setBackgroundResource(R.drawable.play);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        float speed = 1.0f;
        switch (id) {
        case R.id.about:
            alertMenu();
            return true;
        case R.id.plus0_5x:
            speed = 0.5f;
            mp.setPlaybackParams(mp.getPlaybackParams().setSpeed(speed));
            return true;
        case R.id.plusx:
            speed = 1.0f;
            mp.setPlaybackParams(mp.getPlaybackParams().setSpeed(speed));
            return true;
        case R.id.plus2x:
            speed = 2.0f;
            mp.setPlaybackParams(mp.getPlaybackParams().setSpeed(speed));
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void alertMenu() {
        View view = inflater.inflate(R.layout.extra_window, null);
        TextView textView = view.findViewById(R.id.extraText1);
        textView.setText(list_composition.get(curPosition).getTitle());
        TextView tv = view.findViewById(R.id.extraText2);
        tv.setText(list_composition.get(curPosition).getAuthor());

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Dodatkowe informacje");
        alertDialog.setView(view);
        alertDialog.setCancelable(true);

        AlertDialog alert = alertDialog.create();
        alert.show();

    }


    private List<Composition> getListComposition()
    {
        ArrayList<Composition> compositions = new ArrayList<Composition>();
        compositions.add(new Composition("Closer","Chainsmokers", R.drawable.play, R.raw.closer, R.drawable.chaina));
        compositions.add(new Composition("Sugar","Maroon5", R.drawable.play, R.raw.sugar, R.drawable.maroon5a));
        compositions.add(new Composition("Dont wanna know!","Maroon5", R.drawable.play, R.raw.dontwannaknow, R.drawable.maroon5b));
        compositions.add(new Composition("Sick Boy","Chainsmokers", R.drawable.play, R.raw.sickboy, R.drawable.chainb));
        return  compositions;
    }
}
