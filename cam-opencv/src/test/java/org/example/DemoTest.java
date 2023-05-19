package org.example;

import static org.opencv.imgcodecs.Imgcodecs.imread;

import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

/**
 * Created by ChenHao on 2022/7/27 is 16:06.
 *
 * @author hqqich <hqqich1314@outlook.com>
 * @version V1.0.0
 * @description
 * @date 2022/7/27
 * @since 1.0
 */

public class DemoTest {


  // 一定要写，不然报错 "java.lang.UnsatisfiedLinkError: org.opencv.core.Mat.n_Mat(IIIDDDD)J"
  static {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
  }


  @Test
  public void test02() {
    System.out.println(Core.getBuildInformation());
  }


  @Test
  public void test01() {
    System.out.println(Core.VERSION);

    //Mat mat = new Mat(5, 10, CvType.CV_8UC1, new Scalar(0));
    Mat mat = imread("./image/face.png");
    System.out.println(mat);

    Mat mat1 = mat.row(1);
    mat1.setTo(new Scalar(1));

    Mat mat5 = mat.col(5);
    mat5.setTo(new Scalar(5));

    System.out.println(mat.dump());

  }

}
