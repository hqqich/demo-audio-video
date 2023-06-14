package org.example;

import com.google.common.primitives.Bytes;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * 静音音频工具类 <br>
 *
 * 以16k采样率，单声道，16bits采样分辨率为例，具体参数原理，下面有说明。 <br>
 *
 * @author hqqich
 * @version 2022/07/26
 */
public class SilenceAudioUtils {

  /**
   * 根据PCM文件构建wav的header字段
   *
   * @param srate   Sample rate - 8000, 16000, etc.
   * @param channel Number of channels - Mono = 1, Stereo = 2, etc..
   * @param format  Number of bits per sample (16 here)
   * @throws IOException
   */
  public static byte[] buildWavHeader(int dataLength, int srate, int channel, int format) throws IOException {
    byte[] header = new byte[44];

    long totalDataLen = dataLength + 36;
    long bitrate = srate * channel * format;

    header[0] = 'R';
    header[1] = 'I';
    header[2] = 'F';
    header[3] = 'F';
    header[4] = (byte) (totalDataLen & 0xff);
    header[5] = (byte) ((totalDataLen >> 8) & 0xff);
    header[6] = (byte) ((totalDataLen >> 16) & 0xff);
    header[7] = (byte) ((totalDataLen >> 24) & 0xff);
    header[8] = 'W';
    header[9] = 'A';
    header[10] = 'V';
    header[11] = 'E';
    header[12] = 'f';
    header[13] = 'm';
    header[14] = 't';
    header[15] = ' ';
    header[16] = (byte) format;
    header[17] = 0;
    header[18] = 0;
    header[19] = 0;
    header[20] = 1;
    header[21] = 0;
    header[22] = (byte) channel;
    header[23] = 0;
    header[24] = (byte) (srate & 0xff);
    header[25] = (byte) ((srate >> 8) & 0xff);
    header[26] = (byte) ((srate >> 16) & 0xff);
    header[27] = (byte) ((srate >> 24) & 0xff);
    header[28] = (byte) ((bitrate / 8) & 0xff);
    header[29] = (byte) (((bitrate / 8) >> 8) & 0xff);
    header[30] = (byte) (((bitrate / 8) >> 16) & 0xff);
    header[31] = (byte) (((bitrate / 8) >> 24) & 0xff);
    header[32] = (byte) ((channel * format) / 8);
    header[33] = 0;
    header[34] = 16;
    header[35] = 0;
    header[36] = 'd';
    header[37] = 'a';
    header[38] = 't';
    header[39] = 'a';
    header[40] = (byte) (dataLength & 0xff);
    header[41] = (byte) ((dataLength >> 8) & 0xff);
    header[42] = (byte) ((dataLength >> 16) & 0xff);
    header[43] = (byte) ((dataLength >> 24) & 0xff);

    return header;
  }

  /**
   * 默认写入的pcm数据是16000采样率，16bit，可以按照需要修改
   *
   * @param filePath
   * @param pcmData
   */
  public static boolean writeToFile(String filePath, byte[] pcmData) {
    BufferedOutputStream bos = null;
    try {
      bos = new BufferedOutputStream(new FileOutputStream(filePath));
      byte[] header = buildWavHeader(pcmData.length, 16000, 1, 16);
      bos.write(header, 0, 44);
      bos.write(pcmData);
      bos.close();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (bos != null) {
        try {
          bos.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    return false;
  }

  /**
   * 生成静音音频
   *
   * @param filePath 输出文件地址
   * @param duration 音频时长
   */
  public static void makeSilenceWav(String filePath, Long duration) {
    List<Byte> oldBytes = new ArrayList<>();
    IntStream.range(0, (int) (duration * 32)).forEach(x -> oldBytes.add((byte) 100));
    writeToFile(filePath, Bytes.toArray(oldBytes));
  }
  /**
   * 生成静音音频
   *
   * @param filePath 输出文件地址
   * @param duration 音频时长
   */
  public static void makeSilenceWavD(String filePath, Long duration) {
    List<Byte> oldBytes = new ArrayList<>();
    //IntStream.range(0, (int) (duration * 32)).forEach(x -> oldBytes.add((byte) 100));

    byte[] buf = new byte[1];
    for (int i = 0; i < 1000 * (float) 44100 / 1000; i++) {
      double angle = i / ((float) 44100 / 440) * 2.0 * Math.PI;
      double v = Math.sin(angle) * 100;
      System.out.println(v);
      buf[0] = (byte) (v);
      oldBytes.add((byte) (v));
    }
    writeToFile(filePath, Bytes.toArray(oldBytes));
  }

  public static void main(String[] args) {
    //makeSilenceWav("./2.wav", 5000L);
    makeSilenceWavD("./3.wav", 5000L);


  }
}