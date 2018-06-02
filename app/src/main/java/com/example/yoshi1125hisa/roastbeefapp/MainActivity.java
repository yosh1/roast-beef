package com.example.yoshi1125hisa.roastbeefapp;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private TextView timerText;
    EditText numberText;
    private SimpleDateFormat dataFormat = new SimpleDateFormat("mm:ss.SSS", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // タイトルバーを隠す場合
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // ステータスバーを隠す場合
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        // sm　これをFirebaseでとってきたい。
        long countNumber = 180000;
        // インターバル
        long interval = 10;

        Button postButton = findViewById(R.id.mPostButton);
        Button startButton = findViewById(R.id.start_button);
        Button stopButton = findViewById(R.id.stop_button);

        postButton = findViewById(R.id.post);


        timerText = findViewById(R.id.timer);
        timerText.setText(dataFormat.format(0));

        //CountDownTimer(long millisInFuture, long countDownInterval);
        final CountDown countDown = new CountDown(countNumber, interval);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 開始
                countDown.start();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 中止
                countDown.cancel();
                timerText.setText(dataFormat.format(0));
               // Intent intent = new Intent(MainActivity.this,PostActivity.class);
               // startActivity(intent);
            }
        });

        postButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        }
    );
    }

    class CountDown extends CountDownTimer {

        CountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            // 完了
            timerText.setText(dataFormat.format(0));
        }

        // インターバルで呼ばれる
        @Override
        public void onTick(long millisUntilFinished) {
            // 残り時間を分、秒、ミリ秒に分割
            long mm = millisUntilFinished / 1000 / 60;
            long ss = millisUntilFinished / 1000 % 60;
           long ms = millisUntilFinished - ss * 1000 - mm * 1000 * 60;
            timerText.setText(String.format("%1$02d:%2$02d.%3$03d", mm, ss, ms));
            timerText.setText(dataFormat.format(millisUntilFinished));

        }

    }

    public void look(View v){

 Intent intent = new Intent(this,TelListActivity.class);
        startActivity(intent);
    }
}
