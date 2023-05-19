package org.example.identifyface;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import org.example.util.ImageUtils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;


/**
 * 参考链接：<br>
 * <a href="http://www.cppcns.com/ruanjian/java/472353.html"></a>
 */
public class FaceRecognize extends JFrame {
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private static final String cascadeFileFullPath = "F:\\Program Files\\opencv\\sources\\data\\haarcascades\\haarcascade_frontalface_alt.xml";
    private static final String photoPath = "G:\\";
    private JPanel contentPane;
    protected static VideoPanel videoCamera = new VideoPanel();
    private static final Size faceSize = new Size(165, 200);
    private static VideoCapture capture = new VideoCapture();

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        FaceRecognize frame = new FaceRecognize();
        frame.setVisible(true);
        frame.invokeCamera(frame, videoCamera);
    }

    public void invokeCamera(JFrame frame, VideoPanel videoPanel) {
        new Thread() {
            public void run() {
                CascadeClassifier faceCascade = new CascadeClassifier();
                faceCascade.load(cascadeFileFullPath);
                try {
                    capture.open(0);
                    Scalar color = new Scalar(0, 255, 0);
                    MatOfRect faces = new MatOfRect();
                    // Mat faceFrames = new Mat();
                    if (capture.isOpened()) {
                        System.out.println(">>>>>>video camera in working");
                        Mat faceMat = new Mat();
                        while (true) {
                            capture.read(faceMat);
                            if (!faceMat.empty()) {
                                faceCascade.detectMultiScale(faceMat, faces);
                                Rect[] facesArray = faces.toArray();
                                if (facesArray.length >= 1) {
                                    for (int i = 0; i < facesArray.length; i++) {
                                        Imgproc.rectangle(faceMat, facesArray[i].tl(), facesArray[i].br(), color, 2);
                                        videoPanel.setImageWithMat(faceMat);
                                        frame.repaint();
                                        // videoPanel.repaint();
                                    }
                                }
                            } else {
                                System.out.println(">>>>>>not found anyinput");
                                break;
                            }
                            Thread.sleep(80);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("invoke camera error: " + e.getMessage() + e);
                }
            }
        }.start();
    }

    /**
     * Create the frame.
     */

    public FaceRecognize() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1024, 768);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel cameraGroup = new JPanel();
        cameraGroup.setBounds(10, 10, 988, 580);
        contentPane.add(cameraGroup);
        cameraGroup.setLayout(null);

        JLabel videoDescriptionLabel = new JLabel("Video");
        videoDescriptionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        videoDescriptionLabel.setBounds(0, 10, 804, 23);
        cameraGroup.add(videoDescriptionLabel);

        videoCamera.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        videoCamera.setBounds(10, 43, 794, 527);
        cameraGroup.add(videoCamera);

        // JPanel videoPreview = new JPanel();
        VideoPanel videoPreview = new VideoPanel();
        videoPreview.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        videoPreview.setBounds(807, 359, 171, 211);
        cameraGroup.add(videoPreview);

        JLabel lblNewLabel = new JLabel("Preview");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setBounds(807, 307, 171, 42);
        cameraGroup.add(lblNewLabel);

        JPanel buttonGroup = new JPanel();
        buttonGroup.setBounds(65, 610, 710, 35);
        contentPane.add(buttonGroup);
        buttonGroup.setLayout(new GridLayout(1, 0, 0, 0));

        JButton photoButton = new JButton("Take Photo");
        photoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(">>>>>>take photo performed");
                StringBuffer photoPathStr = new StringBuffer();
                photoPathStr.append(photoPath);
                try {
                    if (capture.isOpened()) {
                        Mat myFace = new Mat();
                        while (true) {
                            capture.read(myFace);
                            if (!myFace.empty()) {
                                Image previewImg = ImageUtils.scale2(myFace, 165, 200, true);// 等比例缩放
                                TakePhotoProcess takePhoto = new TakePhotoProcess(photoPath.toString(), myFace);
                                takePhoto.start();// 照片写盘
                                videoPreview.SetImageWithImg(previewImg);// 在预览界面里显示等比例缩放的照片
                                videoPreview.repaint();// 让预览界面重新渲染
                                break;
                            }
                        }
                    }
                } catch (Exception ex) {
                    System.out.println(">>>>>>take photo error: " + ex.getMessage() + ex);
                }
            }
        });
        buttonGroup.add(photoButton);

        JButton trainButton = new JButton("Train");
        buttonGroup.add(trainButton);

        JButton identifyButton = new JButton("Identify");
        buttonGroup.add(identifyButton);
    }
}
