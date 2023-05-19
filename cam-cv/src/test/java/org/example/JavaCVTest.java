package org.example;

import java.awt.image.BufferedImage;
import javax.swing.WindowConstants;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.junit.Test;

/**
 * Created by ChenHao on 2022/7/25 is 16:40.
 *
 * @author hqqich <hqqich1314@outlook.com>
 * @version V1.0.0
 * @description
 * @date 2022/7/25
 * @since 1.0
 */

public class JavaCVTest {


    public BufferedImage FrameToBufferedImage(Frame frame) {
        // 创建BufferedImage对象
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bufferedImage = converter.getBufferedImage(frame);
        return bufferedImage;
    }

    /**
     * 生成JavaFX 调用摄像头
     * @throws InterruptedException
     * @throws org.bytedeco.javacv.FrameGrabber.Exception
     */
    @Test
    public void testCamera() throws InterruptedException, org.bytedeco.javacv.FrameGrabber.Exception{
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        grabber.start();   //开始获取摄像头数据
        CanvasFrame canvas = new CanvasFrame("摄像头窗口");//新建一个窗口
        canvas.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        canvas.setAlwaysOnTop(true);
        while (true) {
            if (!canvas.isDisplayable()) {//窗口是否关闭
                grabber.stop();//停止抓取
                System.exit(-1);//退出
            }

            Frame frame = grabber.grab();
            canvas.showImage(frame);	//获取摄像头图像并放到窗口上显示， 这里的Frame frame=grabber.grab(); frame是一帧视频图像
            Thread.sleep(50);	//50毫秒刷新一次图像
        }
    }
}
