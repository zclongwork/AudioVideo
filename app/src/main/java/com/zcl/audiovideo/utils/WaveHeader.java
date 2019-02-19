package com.zcl.audiovideo.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Description TODO
 * Author ZhangChenglong
 * Date 19/2/18 18:29
 */
public class WaveHeader {
    
    private final char fileID[] = {'R', 'I', 'F', 'F'};
    public int fileLength;
    private char wavTag[] = {'W', 'A', 'V', 'E'};;
    private char FmtHdrID[] = {'f', 'm', 't', ' '};
    public int FmtHdrLeth;
    public short FormatTag;
    public short Channels;
    public int SamplesPerSec;
    public int AvgBytesPerSec;
    public short BlockAlign;
    public short BitsPerSample;
    private char DataHdrID[] = {'d','a','t','a'};
    public int DataHdrLeth;
    
    public byte[] getHeader() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        WriteChar(bos, fileID);
        //从下个地址开始到文件尾的总字节数
        WriteInt(bos, fileLength);
        WriteChar(bos, wavTag);
        WriteChar(bos, FmtHdrID);
        WriteInt(bos,FmtHdrLeth);
        
        //格式种类
        WriteShort(bos,FormatTag);
        //单声道1 双声道2
        WriteShort(bos,Channels);
        //采样频率
        WriteInt(bos,SamplesPerSec);
        //每秒数据量；其值为通道数×每秒数据位数×每样本的数据位数／8
        WriteInt(bos,AvgBytesPerSec);
        //数据块的调整数（按字节算的），其值为通道数×每样本的数据位值／8
        WriteShort(bos,BlockAlign);
        //每个样本的数据位数 就是位深度
        WriteShort(bos,BitsPerSample);
        
        WriteChar(bos,DataHdrID);
        //采样数据总数
        WriteInt(bos,DataHdrLeth);
        bos.flush();
        byte[] r = bos.toByteArray();
        bos.close();
        return r;
    }
    
    private void WriteShort(ByteArrayOutputStream bos, int s) throws IOException {
        byte[] mybyte = new byte[2];
        mybyte[1] =(byte)( (s << 16) >> 24 );
        mybyte[0] =(byte)( (s << 24) >> 24 );
        bos.write(mybyte);
    }
    
    
    private void WriteInt(ByteArrayOutputStream bos, int n) throws IOException {
        byte[] buf = new byte[4];
        buf[3] =(byte)( n >> 24 );
        buf[2] =(byte)( (n << 8) >> 24 );
        buf[1] =(byte)( (n << 16) >> 24 );
        buf[0] =(byte)( (n << 24) >> 24 );
        bos.write(buf);
    }
    
    private void WriteChar(ByteArrayOutputStream bos, char[] id) {
        for (int i=0; i<id.length; i++) {
            char c = id[i];
            bos.write(c);
        }
    }

}
