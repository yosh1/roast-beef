package com.example.yoshi1125hisa.roastbeefapp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView timerText;
    private SimpleDateFormat dataFormat = new SimpleDateFormat("mm"+"分"/*:ss.SSS*/, Locale.US);
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference refMsg;
    private EditText telNumText;
    private RelativeLayout relativeLayout;
    private InputMethodManager inputMethodManager;
    Button postButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // タイトルバーを隠す場合
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // ステータスバーを隠す場合
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.INTERNET)== PackageManager.PERMISSION_GRANTED){
            // 許可されている時の処理
        }else{
            //許可されていない時の処理
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)) {
                //拒否された時 Permissionが必要な理由を表示して再度許可を求めたり、機能を無効にしたりします。
            } else {
                //まだ許可を求める前の時、許可を求めるダイアログを表示します。
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 0);

            }
        }

        // sm　これをFirebaseでとってきたい。
        //180 = 3 ... 120 * 60 = 120
        long countNumber = 3 * 60 * 1000;
        // インターバル
        long interval = 10;



     //   Button startButton = findViewById(R.id.start_button);
       // Button stopButton = findViewById(R.id.stop_button);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        refMsg = database.getReference("tel");
        telNumText = findViewById(R.id.telNumText);
        //telNumText.setInputType(InputType.TYPE_CLASS_PHONE);

        postButton = findViewById(R.id.postButton);
        //入力制限、PhoneNumberUtils
        //android.telephony.PhoneNumberFormattingTextWatcher
        //telNumText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        timerText = findViewById(R.id.timer);
        timerText.setText(dataFormat.format(0));

        //CountDownTimer(long millisInFuture, long countDownInterval);
        final CountDown countDown = new CountDown(countNumber, interval);
        //EditTextにリスナーをセット
        telNumText.setOnKeyListener(new View.OnKeyListener() {

            //コールバックとしてonKey()メソッドを定義
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //イベントを取得するタイミングには、ボタンが押されてなおかつエンターキーだったときを指定
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    //キーボードを閉じる
                    inputMethodManager.hideSoftInputFromWindow(telNumText.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);


                    return true;
                }

                return false;
            }
        });
/*
        countDown.start();

        postButton.setOnClickListener(new View.OnClickListener() {
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
*/
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.post:
                String getTelNum = telNumText.getText().toString();
                final Post post = new Post(getTelNum);


                refMsg.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                refMsg.push().setValue(post);
                break;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    // 画面タップ時の処理
    @Override
    public boolean onTouchEvent(MotionEvent event) {

// キーボードを隠す
        inputMethodManager.hideSoftInputFromWindow(relativeLayout.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
// 背景にフォーカスを移す
        relativeLayout.requestFocus();

        return true;

    }

    class CountDown extends CountDownTimer {

        CountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            // 完了
            timerText.setText(dataFormat.format(0));

            //通知
        }

        // インターバルで呼ばれる
        @Override
        public void onTick(long millisUntilFinished) {
            // 残り時間を分、秒、ミリ秒に分割
            long mm = millisUntilFinished / 1000 / 60;
            long ss = millisUntilFinished / 1000 % 60;
            long ms = millisUntilFinished - ss * 1000 - mm * 1000 * 60;
            timerText.setText(String.format("%1$02d"/*:%2$02d.%3$03d*/ ,mm/*, ss, ms*/));
            timerText.setText(dataFormat.format(millisUntilFinished));

        }

    }

    public void look(View v){
        Intent intent = new Intent(this,TelListActivity.class);
        startActivity(intent);
    }

}
