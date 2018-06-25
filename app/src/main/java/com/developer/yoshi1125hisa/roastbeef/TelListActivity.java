package com.developer.yoshi1125hisa.roastbeef;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.developer.yoshi1125hisa.roastbeefapp.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.ArrayList;

public class TelListActivity extends AppCompatActivity {

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tel_list);
        final ListView listView = findViewById(R.id.listView);
        final View emptyView = findViewById(R.id.empty);
        listView.setEmptyView(emptyView);
        final DatabaseReference sendsRef = FirebaseDatabase.getInstance().getReference("tel");

        final Context context = getApplicationContext();


        // リスト項目とListViewを対応付けるArrayAdapterを用意する
        final ArrayAdapter<TelephoneNumber> telArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<TelephoneNumber>());
        listView.setAdapter(telArrayAdapter);


        // リスト項目を長押しクリックした時の処理
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            /**
             * @param parent   ListView
             * @param view     選択した項目
             * @param position 選択した項目の添え字
             * @param id       選択した項目のID
             */

            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final TelephoneNumber item = telArrayAdapter.getItem(position);
                if (item != null && item.key != null) {
                    sendsRef.child(item.key).removeValue();
                }
                return false;
            }


        });



        sendsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                final TelephoneNumber number = dataSnapshot.getValue(TelephoneNumber.class);
                if (number != null) {
                    number.key = dataSnapshot.getKey();
                }
                telArrayAdapter.add(number);
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                // Changed
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                final TelephoneNumber number = dataSnapshot.getValue(TelephoneNumber.class);
                if (number != null) {
                    number.key = dataSnapshot.getKey();
                }
                telArrayAdapter.remove(number);
                StyleableToast.makeText(context, "削除しました。", Toast.LENGTH_SHORT, R.style.mytoast).show();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {
                // Moved

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                StyleableToast.makeText(context, "エラーが発生しました。", Toast.LENGTH_SHORT, R.style.mytoast).show();
                // Error
            }
        });
    }
}

