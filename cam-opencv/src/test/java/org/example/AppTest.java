package org.example;

import java.io.File;
import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


public class AppTest {

  @Test
  public void toGray_1() {

    // 这个必须要写,不写报java.lang.UnsatisfiedLinkError
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    File imgFile = new File("./image/test.jpg");
    String dest = "./image";

    //方式一
    Mat src = Imgcodecs.imread(imgFile.toString(), Imgcodecs.IMREAD_GRAYSCALE);

    //保存灰度化的图片
    Imgcodecs.imwrite(dest + "/toGray1" + imgFile.getName(), src);
  }

  @Test
  public void toGray_2() {

    // 这个必须要写,不写报java.lang.UnsatisfiedLinkError
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    File imgFile = new File("./image/test.jpg");
    String dest = "./image";

    //方式二
    Mat src = Imgcodecs.imread(imgFile.toString());
    Mat gray = new Mat();
    Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
    src = gray;

    //保存灰度化的图片
    Imgcodecs.imwrite(dest + "/toGray2" + imgFile.getName(), src);
  }



  public void binaryzation(Mat mat) {

    int BLACK = 0;
    int WHITE = 255;
    int ucThre = 0, ucThre_new = 127;
    int nBack_count, nData_count;
    int nBack_sum, nData_sum;
    int nValue;
    int i, j;
    int width = mat.width(), height = mat.height();

    //寻找最佳的阙值
    while (ucThre != ucThre_new) {
      nBack_sum = nData_sum = 0;
      nBack_count = nData_count = 0;

      for (j = 0; j < height; ++j) {
        for (i = 0; i < width; i++) {
          nValue = (int) mat.get(j, i)[0];
          if (nValue > ucThre_new) {
            nBack_sum += nValue;
            nBack_count++;
          } else {
            nData_sum += nValue;
            nData_count++;
          }
        }
      }

      nBack_sum = nBack_sum / nBack_count;
      nData_sum = nData_sum / nData_count;
      ucThre = ucThre_new;
      ucThre_new = (nBack_sum + nData_sum) / 2;
    }

    //二值化处理
    int nBlack = 0;
    int nWhite = 0;
    for (j = 0; j < height; ++j) {
      for (i = 0; i < width; ++i) {
        nValue = (int) mat.get(j, i)[0];
        if (nValue > ucThre_new) {
          mat.put(j, i, WHITE);
          nWhite++;
        } else {
          mat.put(j, i, BLACK);
          nBlack++;
        }
      }
    }

    // 确保白底黑字
    if (nBlack > nWhite) {
      for (j = 0; j < height; ++j) {
        for (i = 0; i < width; ++i) {
          nValue = (int) (mat.get(j, i)[0]);
          if (nValue == 0) {
            mat.put(j, i, WHITE);
          } else {
            mat.put(j, i, BLACK);
          }
        }
      }
    }

  }

  @Test
  public void binaryzation() {

    // 这个必须要写,不写报java.lang.UnsatisfiedLinkError
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    File imgFile = new File("./image/test.jpg");
    String dest = "./image";

    //先经过一步灰度化
    Mat src = Imgcodecs.imread(imgFile.toString());
    Mat gray = new Mat();
    Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
    src = gray;

    //二值化
    binaryzation(src);
    Imgcodecs.imwrite(dest + "/binaryzation" + imgFile.getName(), src);
  }


  /**
   * Opencv自己也提供了二值化的接口，好像没有上面的效果好，这里也把代码放出来
   */
  @Test
  public  void  testOpencvBinary() {

    // 这个必须要写,不写报java.lang.UnsatisfiedLinkError
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    File imgFile = new File("./image/test.jpg");
    String dest = "./image";
    Mat src = Imgcodecs.imread(imgFile.toString(), Imgcodecs.IMREAD_GRAYSCALE);
    Imgcodecs.imwrite(dest + "/AdaptiveThreshold1" + imgFile.getName(), src);

    Mat dst = new Mat();
    Imgproc.adaptiveThreshold(src, dst, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 13, 5);
    Imgcodecs.imwrite(dest + "/AdaptiveThreshold2" + imgFile.getName(), dst);
    Imgproc.adaptiveThreshold(src, dst, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 13, 5);
    Imgcodecs.imwrite(dest + "/AdaptiveThreshold3" + imgFile.getName(), dst);
    Imgproc.adaptiveThreshold(src, dst, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 13, 5);
    Imgcodecs.imwrite(dest + "/AdaptiveThreshold4" + imgFile.getName(), dst);
    Imgproc.adaptiveThreshold(src, dst, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY_INV, 13, 5);
    Imgcodecs.imwrite(dest + "/AdaptiveThreshold5" + imgFile.getName(), dst);
  }

}
