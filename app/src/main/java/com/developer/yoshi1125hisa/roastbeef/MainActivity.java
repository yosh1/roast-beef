package com.developer.yoshi1125hisa.roastbeef;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import info.vividcode.time.iso8601.Iso8601ExtendedOffsetDateTimeFormat;

public class MainActivity extends AppCompatActivity {
    InputMethodManager inputMethodManager;
    RelativeLayout relativeLayout;



    @IgnoreExtraProperties
    public static class FirebaseTimer {
        public String willEndAt;
        public boolean cooking;
        RelativeLayout relativeLayout;


        public FirebaseTimer() {
        }

        public FirebaseTimer(String willEndAt, boolean cooking) {
            this.willEndAt = willEndAt;
            this.cooking = cooking;
        }
    }

    private static final String channelId = "RoastBeefApp";
    TextView timerView;
    TextView phoneView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.developer.yoshi1125hisa.roastbeefapp.R.layout.activity_main);
        timerView = findViewById(com.developer.yoshi1125hisa.roastbeefapp.R.id.timer);
        phoneView = findViewById(com.developer.yoshi1125hisa.roastbeefapp.R.id.telNumText);

        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        relativeLayout = findViewById(com.developer.yoshi1125hisa.roastbeefapp.R.id.relativeLayout);

        final Context context = getApplicationContext();
        final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager == null) {
            throw new RuntimeException("NotificationManager is null");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final NotificationChannel channel = new NotificationChannel(channelId, "肉", NotificationManager.IMPORTANCE_HIGH);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            manager.createNotificationChannel(channel);
        }
        final DatabaseReference timerDatabase = FirebaseDatabase.getInstance().getReference("timer");
        timerDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final FirebaseTimer timer = dataSnapshot.getValue(FirebaseTimer.class);
                if (timer != null) {
                    if (timer.cooking) {
                        onCookingStarted(timer);
                    } else {
                        onCookingNotStarted(timer);
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });



        //EditTextにリスナーをセット
        phoneView.setOnKeyListener(new View.OnKeyListener() {

            //コールバックとしてonKey()メソッドを定義
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //イベントを取得するタイミングには、ボタンが押されてなおかつエンターキーだったときを指定
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    //キーボードを閉じる
                    inputMethodManager.hideSoftInputFromWindow(phoneView.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    final String phoneNumber = phoneView.getText().toString();
                    if (TextUtils.isEmpty(phoneNumber)) {
                        return false;
                    }
                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("tel");
                    final String key = reference.push().getKey();
                    final TelephoneNumber telephoneNumber = new TelephoneNumber();
                    telephoneNumber.telNum = phoneNumber;
                    final Map<String, Object> map = telephoneNumber.toMap();
                    reference.child(key).updateChildren(map);
                    phoneView.getEditableText().clear();
                    StyleableToast.makeText(context, "登録しました。", Toast.LENGTH_SHORT, com.developer.yoshi1125hisa.roastbeefapp.R.style.mytoast).show();


                }

                return false;
            }
        });



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


        findViewById(com.developer.yoshi1125hisa.roastbeefapp.R.id.lookButton).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(MainActivity.this, TelListActivity.class);
                startActivity(intent);
            }
        });



    }



    // 画面タップ時の処理
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // キーボードを隠す
        inputMethodManager.hideSoftInputFromWindow(relativeLayout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        // 背景にフォーカスを移す
        relativeLayout.requestFocus();
        return true;

    }

    private void onCookingNotStarted(@SuppressWarnings("unused") FirebaseTimer timer) {
        timerView.setText(com.developer.yoshi1125hisa.roastbeefapp.R.string.waiting_cooking);
    }


    private void onCookingStarted(FirebaseTimer timer) {
        final DateFormat format = new Iso8601ExtendedOffsetDateTimeFormat();
        try {
            final Date willEndAt = format.parse(timer.willEndAt);
            final long duration = willEndAt.getTime() - System.currentTimeMillis();
            new Countdown(duration, 10).start();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    class Countdown extends CountDownTimer {

        Countdown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            final long secondsDuration = TimeUnit.MILLISECONDS.toSeconds(l);
            final long minutes = TimeUnit.SECONDS.toMinutes(secondsDuration);
            final long seconds = secondsDuration % 60;
            @SuppressLint("DefaultLocale") final String text = String.format("%02d:%02d", minutes, seconds);
            timerView.setText(text);
        }

        @Override
        public void onFinish() {
            final NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager == null) {
                return;
            }
            final Notification notification = new NotificationCompat.Builder(MainActivity.this, channelId)
                    .setContentTitle("ローストビーフ")
                    .setContentText("お肉ができました！！")
                    .setSmallIcon(com.developer.yoshi1125hisa.roastbeefapp.R.mipmap.ic_launcher_round)
                    .build();
            manager.notify(1, notification);
        }
    }

}
