package org.example;


import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.WebcamUtils;
import com.github.sarxos.webcam.util.ImageUtils;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * 相机测试
 * @author hqqich
 * @version 1.0
 */
public class TestWebCam {

  public static void main(String[] args) {

    Webcam webcam = Webcam.getDefault();
    webcam.setViewSize(WebcamResolution.VGA.getSize());

    WebcamPanel panel = new WebcamPanel(webcam);
    panel.setFPSDisplayed(true);
    panel.setDisplayDebugInfo(true);
    panel.setImageSizeDisplayed(true);
    //panel.setMirrored(true);  //设置镜像 true 为镜像显示

    JFrame window = new JFrame("调用摄像头");
    window.add(panel);
    window.setResizable(true);
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.pack();
    window.setVisible(true);

    final JButton button = new JButton("识别");
    window.add(panel, BorderLayout.CENTER);
    window.add(button, BorderLayout.SOUTH);
    window.setResizable(true);
    window.pack();
    window.setVisible(true);
    button.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {

        button.setEnabled(false);  //设置按钮不可点击

        //实现拍照保存-------start
        String fileName = "../img/" + System.currentTimeMillis();       //保存路径即图片名称（不用加后缀）

        String resultStr = "";


        try {
          ByteBuffer imageByteBuffer = WebcamUtils.getImageByteBuffer(webcam, ImageUtils.FORMAT_PNG);

          LuminanceSource source = new BufferedImageLuminanceSource(readByteBufferToBufferedImage(imageByteBuffer));
          Binarizer binarizer = new HybridBinarizer(source);
          BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
          Map<DecodeHintType,Object> hints = new HashMap<DecodeHintType, Object>();
          hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
          Result result = new MultiFormatReader().decode(binaryBitmap, hints);//解码
          System.out.println("图片中内容：  ");
          resultStr = result.getText();
          System.out.println("content： " + result.getText());
          System.out.println(result.getText());
        } catch (NotFoundException ex) {
          System.out.println("ERROR");
        }

        WebcamUtils.capture(webcam, fileName, ImageUtils.FORMAT_PNG);
        String finalResultStr = resultStr;
        SwingUtilities.invokeLater(new Runnable() {

          @Override
          public void run() {
            JOptionPane.showMessageDialog(null, finalResultStr);
            button.setEnabled(true);    //设置按钮可点击

            return;
          }
        });
        //实现拍照保存-------end

      }
    });

  }

  public static BufferedImage readByteBufferToBufferedImage(ByteBuffer byteBuffer) {

    try {
      if(null==byteBuffer)
        return null;

      byte [] b = byteBuffer.array();
      ByteArrayInputStream in = new ByteArrayInputStream(b);

      BufferedImage image = ImageIO.read(in);

      return image;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
