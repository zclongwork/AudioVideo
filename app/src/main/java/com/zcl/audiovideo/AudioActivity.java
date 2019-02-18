package com.zcl.audiovideo;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * PCM:未经任何编码和压缩
 * wav：无损的音频文件格式
 *
 *
 *
 */
public class AudioActivity extends Activity implements View.OnClickListener {
    
    private String[] mArrays;
    private Button mRecordButton;
    private Button mPlayButton;
    private AudioRecord mAudioRecord;
    
    
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
       
        mRecordButton = findViewById(R.id.bt_record);
        mPlayButton = findViewById(R.id.bt_play);
        
        createRecord();
    }
    
    private void createRecord() {
        bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
        
        mAudioRecord = new AudioRecord(audioSource, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes);
        
    }
    
    
    @Override
    public void onClick(View v) {
    
    
    }
}

