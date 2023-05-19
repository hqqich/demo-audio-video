package org.example;

import static org.opencv.highgui.HighGui.imshow;
import static org.opencv.highgui.HighGui.waitKey;
import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgcodecs.Imgcodecs.imwrite;
import static org.opencv.imgproc.Imgproc.COLOR_RGB2GRAY;
import static org.opencv.imgproc.Imgproc.cvtColor;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;


/**
 * 识别人脸
 * @author hqqich
 */
public class SimpleTest {

    // 一定要写，不然报错 "java.lang.UnsatisfiedLinkError: org.opencv.core.Mat.n_Mat(IIIDDDD)J"
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    // 灰度处理
    public static void main(String[]args) throws Exception {
        // 解决awt报错问题
        System.setProperty("java.awt.headless", "false");
        System.out.println(System.getProperty("java.library.path"));
        // 加载动态库
        //URL url = ClassLoader.getSystemResource("lib/opencv/opencv_java452.dll");
        //System.load(url.getPath());
        // 读取图像

        Mat image = imread("./image/face.png");
        if (image.empty()) {
            throw new Exception("image is empty");
        }
        imshow("Original Image", image);

        // 创建输出单通道图像
        Mat grayImage = new Mat(image.rows(), image.cols(), CvType.CV_8SC1);
        // 进行图像色彩空间转换
        cvtColor(image, grayImage, COLOR_RGB2GRAY);

        // 显示一下
        imshow("Processed Image", grayImage);
        //图片写，将灰度化的图片存入硬盘
        imwrite("./image/face_gray.png", grayImage);
        //waitKey();



        Mat faceImag = imread("./image/face_gray.png");
        // 人脸识别器
        CascadeClassifier faceDetector = new CascadeClassifier("F:\\Program Files\\opencv\\sources\\data\\haarcascades\\haarcascade_frontalface_alt.xml");
        // 在图片中检测人脸
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(faceImag, faceDetections);

        Rect[] rects = faceDetections.toArray();
        if(rects != null && rects.length >= 1){
            for (Rect rect : rects) {

                Imgproc.rectangle(faceImag,rect,Scalar.all(1));
            }
        }
        imshow("人脸检测图片",faceImag);
        waitKey();
    }
}

