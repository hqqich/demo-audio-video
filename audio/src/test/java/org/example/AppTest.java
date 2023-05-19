package org.example;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import org.junit.Test;


public class AppTest {

  /**
   * 自动播放声音,1秒的声音
   */
  @Test
  public void shouldAnswerWithTrue() throws LineUnavailableException {
    byte[] buf = new byte[1];
    AudioFormat af = new AudioFormat((float) 44100, 8, 1, true, false);
    SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
    sdl.open();
    sdl.start();
    for (int i = 0; i < 1000 * (float) 44100 / 1000; i++) {
      double angle = i / ((float) 44100 / 440) * 2.0 * Math.PI;
      double v = Math.sin(angle) * 100;
      System.out.println(v);
      buf[0] = (byte) (v);
      sdl.write(buf, 0, 1);
    }
    sdl.drain();
    sdl.stop();
  }
}
