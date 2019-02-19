package com.zcl.audiovideo;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * PCM:未经任何编码和压缩
 * wav：无损的音频文件格式
 *
 *
 *
 */
public class AudioActivity extends Activity implements View.OnClickListener {
    
    private String[] mArrays;
    private Button mRecordStart;
    private Button mRecordStop;
    private Button mPlayButton;
    private AudioRecord mAudioRecord;
    
    private static String TAG = "AudioActivity";
    
    
    // 音频获取源
    private int audioSource = MediaRecorder.AudioSource.MIC;
    // 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
    private static int sampleRateInHz = 44100;
    // 设置音频的录制的声道CHANNEL_IN_STEREO为双声道，CHANNEL_CONFIGURATION_MONO为单声道
    private static int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    // 音频数据格式:PCM 16位每个样本。保证设备支持。PCM 8位每个样本。不一定能得到设备支持。
    private static int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    
    private int bufferSizeInBytes;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
       
        findViewById(R.id.bt_check).setOnClickListener(this);
        mRecordStart = findViewById(R.id.bt_record_start);
        mRecordStop = findViewById(R.id.bt_record_stop);
        mPlayButton = findViewById(R.id.bt_play);
        
        mRecordStart.setOnClickListener(this);
        mRecordStop.setOnClickListener(this);
        mPlayButton.setOnClickListener(this);
        
        createRecord();
    }
    
    private void createRecord() {
        bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
        mAudioRecord = new AudioRecord(audioSource, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes);
    }
    
    
    boolean inRecording = false;
    int readSize;
    
    private String path = "/sdcard/audio_";
    private int count;
    
    private void savePcm() {
        String audioName = path + count;
        byte[] audioData = new byte[bufferSizeInBytes];
        FileOutputStream fos;
        try {
            File file = new File(audioName);
            if (file.exists()) {
                file.delete();
            }
            fos = new FileOutputStream(file);
            while (inRecording) {
                readSize = mAudioRecord.read(audioData, 0, bufferSizeInBytes);
                if (AudioRecord.ERROR_INVALID_OPERATION != readSize) {
                    fos.write(audioData);
                }
            }
            fos.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        count++;
    }
    
    private boolean checkRecordPermission(String permission) {
        int permissionCheck = ContextCompat.checkSelfPermission(this, permission);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, permission + " have permission");
            
        } else {
            Log.w(TAG, permission + " no permission");
            ActivityCompat.requestPermissions(this, new String[]{permission}, 0);
        }
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
        case 0:
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, permissions[0] + "权限获得");
            }
            break;
            
        }
    }
    
    private void startRecord() {
        mAudioRecord.startRecording();
        inRecording = true;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                savePcm();
            }
        }, "record_thread");
    
        thread.start();
    }
    
    private void stopRecord() {
        if (inRecording) {
            mAudioRecord.stop();
            inRecording = false;
        }
    }
    
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.bt_check:
            checkRecordPermission(Manifest.permission.RECORD_AUDIO);
            checkRecordPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            
            break;
        case R.id.bt_record_start:
            Log.d(TAG, "record_start");
            mRecordStart.setClickable(false);
            startRecord();
            break;
        case R.id.bt_record_stop:
            Log.d(TAG, "record_stop");
            stopRecord();
            mRecordStart.setClickable(true);
            break;
        case R.id.bt_change:
//            WaveUtil.makePCMFileToWAVFile("/sdcard/audio_0", , false);
            
            break;
        case R.id.bt_play:
            break;
        default:
            break;
        }
    
    }
}

