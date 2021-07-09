package com.amisha.keepnotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    ExtendedFloatingActionButton add_notes;
    static ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;
    String saved_file;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.listview);
        add_notes=findViewById(R.id.extended_fab);
        arrayList = new ArrayList<String>();
        arrayAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);
        intent= new Intent(MainActivity.this,NoteEditor.class);
        if (arrayList.isEmpty())
            arrayList.add("No recent file to show");
        arrayAdapter.notifyDataSetChanged();
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String check = arrayList.get(position);
                if(!check.equals("No recent file to show"))
                {
                    saved_file =arrayList.get(position);
                    intent.putExtra("SavedFileName_key",saved_file);
                    startActivityForResult(intent,1);
                }
            }
        });

        add_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saved_file="none";
                intent.putExtra("SavedFileName_key",saved_file);
                startActivityForResult(intent,1);
            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        {
            arrayList.remove("No recent files");
            arrayAdapter.notifyDataSetChanged();
            listView.setAdapter(arrayAdapter);
        }
    }
}