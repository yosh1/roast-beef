package com.example.yoshi1125hisa.roastbeefapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TelListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tel_list);

        // ListViewに表示するリスト項目をArrayListで準備する
        ArrayList dataList = new ArrayList<>();
        // 要素の削除、順番変更のためArrayListを定義


        dataList.add("データが登録されていません");

        // リスト項目とListViewを対応付けるArrayAdapterを用意する
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);

        // ListViewにArrayAdapterを設定する
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        // リスト項目を長押しクリックした時の処理
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            /**
             * @param parent   ListView
             * @param view     選択した項目
             * @param position 選択した項目の添え字
             * @param id       選択した項目のID
             */

            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String deleteItem = (String) ((TextView) view).getText();


                // 選択した項目を削除する
                //   ArrayAdapter.remove(deleteItem);

                return false;
            }


        });
    }
}
