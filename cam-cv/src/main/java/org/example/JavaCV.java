package org.example;

import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.swing.WindowConstants;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;

/**
 * Created by ChenHao on 2022/7/25 is 16:40.
 *
 * @author hqqich <hqqich1314@outlook.com>
 * @version V1.0.0
 * @description
 * @date 2022/7/25
 * @since 1.0
 */

public class JavaCV {

  public BufferedImage FrameToBufferedImage(Frame frame) {
    // 创建BufferedImage对象
    Java2DFrameConverter converter = new Java2DFrameConverter();
    BufferedImage bufferedImage = converter.getBufferedImage(frame);
    return bufferedImage;
  }

  public static void main(String[] args) throws Exception {
    OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
    grabber.start();   //开始获取摄像头数据
    CanvasFrame canvas = new CanvasFrame("摄像头窗口");//新建一个窗口
    canvas.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    canvas.setAlwaysOnTop(true);
    while (true) {

      Thread.sleep(2 * 1000);

      if (!canvas.isDisplayable()) {//窗口是否关闭
        grabber.stop();//停止抓取
        System.exit(-1);//退出
      }

      Frame frame = grabber.grab();
      try {
        BufferedImage bufferedImage = null;
        bufferedImage = new JavaCV().FrameToBufferedImage(frame);
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        Binarizer binarizer = new HybridBinarizer(source);
        BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
        Map<DecodeHintType,Object> hints = new HashMap<DecodeHintType, Object>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
        Result result = new MultiFormatReader().decode(binaryBitmap, hints);//解码
        System.out.println("图片中内容：  ");
        System.out.println("content： " + result.getText());
        System.out.println(result.getText());
      } catch (Exception e) {
        System.out.println("ERROR");
      }

      canvas.showImage(frame);	//获取摄像头图像并放到窗口上显示， 这里的Frame frame=grabber.grab(); frame是一帧视频图像
      Thread.sleep(50);	//50毫秒刷新一次图像
    }
  }

}
