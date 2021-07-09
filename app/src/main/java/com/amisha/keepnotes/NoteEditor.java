package com.amisha.keepnotes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Locale;
import java.util.Queue;

public class NoteEditor extends AppCompatActivity {
    EditText file_name,text;
    Button save;
    ImageButton read,write;
    String FileName="",FileText="",data="";
    int changed=0;
    TextToSpeech ts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);
        file_name=findViewById(R.id.file_name);
        text=findViewById(R.id.text);
        save=findViewById(R.id.save);
        read=findViewById(R.id.listen_button);
        write=findViewById(R.id.speak_button);

        Bundle b= getIntent().getExtras();
        FileName=b.getString("SavedFileName_key");
        if(!FileName.equals("none"))
        {
            FileName=b.getString("SavedFileName_key");
            file_name.setText(FileName);
            try{
                int val=0;
                FileInputStream fsaved= openFileInput(FileName);
                while ((val=fsaved.read())!=-1)
                    data+=(char)val;
                text.setText(data);
                changed=1;
            }
            catch (Exception e)
            {
                Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            file_name.setText("");
            text.setText("");
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteEditor.this,MainActivity.class);
                FileName = file_name.getText().toString();
                FileText = text.getText().toString();
                if (FileName.isEmpty()) {
                    file_name.setText("Untitled");
                    FileName = "Untitled";
                }
                if (!FileText.isEmpty()&&!FileText.equals(data)) {
                    try {
                        FileOutputStream fout = openFileOutput(FileName, Context.MODE_PRIVATE);
                        fout.write(FileText.getBytes());
                        MainActivity.arrayList.remove(FileName);
                        MainActivity.arrayList.add(FileName);
                        intent.putExtra("File_name_key", FileName);
                        setResult(1, intent);
                        if(changed==0)
                            Toast.makeText(NoteEditor.this, "File Saved....", Toast.LENGTH_LONG).show();
                        else if(changed==1)
                            Toast.makeText(NoteEditor.this, "File Updated And Saved ", Toast.LENGTH_LONG).show();

                    } catch (Exception e) { }
                    finish();
                }
                else {
                    Toast.makeText(NoteEditor.this, "File Saved With No Changes...", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Start Speaking...");
               startActivityForResult(i,0);
            }
        });
        ts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status!= TextToSpeech.ERROR)
                    ts.setLanguage(Locale.getDefault());
            }
        });
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileText = text.getText().toString();
                ts.speak(FileText,TextToSpeech.QUEUE_FLUSH,null);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == 0 && resultCode == RESULT_OK && data!=null)
        {
            List<String> list = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String value = list.get(0);
            text.append(value);
        }
        else
            Toast.makeText(this, "Could'nt recognize the speech", Toast.LENGTH_SHORT).show();

        super.onActivityResult(requestCode, resultCode, data);
    }
}