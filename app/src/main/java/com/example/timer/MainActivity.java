package com.example.timer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Button setButton;
    Button startButton;
    TextView time;
    EditText min;
    Button resetButton;
    String minutes;
    double timeLeft;
    double timeStart;
    boolean timerOn = false;
    CountDownTimer countDownTimer;
    Vibrator vibe;


    public void setTime(View view) {
        minutes = min.getText().toString();

        if(minutes.length() == 0) {
            Toast.makeText(this, "Please enter a value!", Toast.LENGTH_SHORT).show();
            return;
        }
        //time is in ms now
        double minute = Double.valueOf(minutes)*60*1000;
        if(minute == 0) {
            Toast.makeText(this, "Please enter a value greater than zero!", Toast.LENGTH_SHORT).show();
            return;
        }

        vibe.vibrate(80);
        setTimer(minute);
        min.setText("");
    }

    public void startTime(View view) {
        if(timerOn){
            stopTimer();
        }
        else{
            startTimer();
        }
        vibe.vibrate(80);
    }

    public void resetTime(View view) {
        resetTimer();
        vibe.vibrate(80);
    }

    public void setTimer(double minute) {
        timeStart = minute;
        resetTimer();
        hideKeyboard();
    }

    public void startTimer() {
        countDownTimer = new CountDownTimer((long) timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                updateTimer();

            }

            @Override
            public void onFinish() {
                timerOn = false;
                updateDisplay();
                MediaPlayer horn = MediaPlayer.create(MainActivity.this, R.raw.horn);
                horn.start();
                vibe.vibrate(1000);
            }
        }.start();
        timerOn = true;
        updateDisplay();
    }

    public void stopTimer() {
        countDownTimer.cancel();
        timerOn = false;
        updateDisplay();
    }

    public void resetTimer() {
        timeLeft = timeStart;
        updateTimer();
        updateDisplay();
    }

    public void updateTimer() {
        long hr , min , sec;
        hr = (long) (((timeLeft/1000)/60)/60);
        min = (long) (((timeLeft/1000)/60)%60);
        sec = (long) ((timeLeft/1000)%60);

        String timeSet;
        timeSet = String.format(Locale.getDefault(), "%02d : %02d : %02d", hr, min, sec);

        time.setText(timeSet);
    }

    public void updateDisplay() {
        if(timerOn) {
            min.setAlpha(0f);
            setButton.setAlpha(0f);
            resetButton.setAlpha(0f);
            startButton.setText("STOP");
        }

        else {
            min.setAlpha(1f);
            setButton.setAlpha(1f);
            startButton.setText("START");

            if(timeLeft < 1000)
                startButton.setAlpha(0f);
            else
                startButton.setAlpha(1f);

            if(timeLeft < timeStart)
                resetButton.setAlpha(1f);
            else resetButton.setAlpha(0f);
        }
    }


    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setButton = findViewById(R.id.set);
        startButton = findViewById(R.id.start);
        resetButton = findViewById(R.id.reset);
        time = findViewById(R.id.time);
        min = findViewById(R.id.minutes);

        startButton.setAlpha(0f);
        resetButton.setAlpha(0f);

        vibe = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
    }
}