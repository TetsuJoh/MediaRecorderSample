package com.websarva.wings.android.mediarecordersample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.*;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;

import static java.lang.Boolean.FALSE;

public class MediaRecordActivity extends AppCompatActivity {

    //testComment

    public MediaRecorder _recorder;
    private Button _btStart;
    private Button _btStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_record);

    }

    //録音開始ボタン押下時の処理
    public void onClickRecordStart(View view){

        //MediaRecorderのpermission確認
        if((ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) || (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)){
            String[] permission = {Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(MediaRecordActivity.this, permission, 2000);
            return;
        }

        _btStart = findViewById(R.id.btStart);
        _btStop = findViewById(R.id.btStop);
        _btStart.setText(R.string.bt_Ongoing);
        _btStart.setEnabled(false);
        _btStop.setEnabled(true);

        File filepath = new File(Environment.getExternalStorageDirectory(),"RecorderSample");
        if(filepath.exists() == FALSE){
            filepath.mkdir();
        }
        File mediaFile = new File(filepath, "SampleWav.wav");
        String mediaFilePath = mediaFile.getAbsolutePath();

        try{
            _recorder = new MediaRecorder();
            _recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            _recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            _recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            _recorder.setOutputFile(mediaFilePath);
            _recorder.prepare();
            _recorder.start();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //録音終了ボタン押下時の処理
    public void onClickRecordStop(View view){

        _btStart = findViewById(R.id.btStart);
        _btStop = findViewById(R.id.btStop);
        _btStart.setText(R.string.bt_Start);
        _btStart.setEnabled(true);
        _btStop.setEnabled(false);

        if(_recorder == null){
            Toast.makeText(MediaRecordActivity.this,"mediarecorder == null", Toast.LENGTH_SHORT).show();
        }else {
            try{
                _recorder.stop();
                _recorder.reset();
                Toast.makeText(MediaRecordActivity.this,"録音終了", Toast.LENGTH_SHORT).show();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResult){
        if((requestCode == 2000 && grantResult[0] == PackageManager.PERMISSION_GRANTED) && (requestCode == 2000 && grantResult[1] == PackageManager.PERMISSION_GRANTED)){
            View btStart = findViewById(R.id.btStart);
            onClickRecordStart(btStart);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        _recorder.release();
        _recorder = null;
    }

}
