package com.example.yoshi1125hisa.roastbeefapp;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PostActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference refMsg;
    EditText telNumText;
    Button mPostButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // タイトルバーを隠す場合
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // ステータスバーを隠す場合
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_post);

        refMsg = database.getReference("tel");
        mPostButton = findViewById(R.id.post);
        telNumText = findViewById(R.id.telNumText);
        mPostButton.setOnClickListener(this);
    }

        @Override
        public void onClick(View v) {
            int id = v.getId();

            switch (id) {
                case R.id.post:
                    String telNum = telNumText.getText().toString();
                    final Post post = new Post(telNum);


                    //childを見に行く。
                    //そして次のActivityに送る。

                    refMsg.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //Log.d("ondatachange", dataSnapshot.getRef().push().getKey().toString());
                            }
                        @Override
                            public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    refMsg.push().setValue(post);
                    break;
            }
    }
}
