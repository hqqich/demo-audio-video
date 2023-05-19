package org.example;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.junit.Test;

public class BufferedImageTest {


  /**
   * BufferedImage实现图片灰度
   *
   * @param originalImage 输入
   * @return BufferedImage
   */
  public BufferedImage getGrayPicture(BufferedImage originalImage) {
    int green = 0, red = 0, blue = 0, rgb;
    int imageWidth = originalImage.getWidth();
    int imageHeight = originalImage.getHeight();
    for (int i = originalImage.getMinX(); i < imageWidth; i++) {
      for (int j = originalImage.getMinY(); j < imageHeight; j++) {
        //图片的像素点其实是个矩阵，这里利用两个for循环来对每个像素进行操作
        Object data = originalImage.getRaster()
            .getDataElements(i, j, null);//获取该点像素，并以object类型表示
        red = originalImage.getColorModel().getRed(data);
        blue = originalImage.getColorModel().getBlue(data);
        green = originalImage.getColorModel().getGreen(data);
        red = (red * 3 + green * 6 + blue * 1) / 10;
        green = red;
        blue = green;
        /*
         这里将r、g、b再转化为rgb值，因为bufferedImage没有提供设置单个颜色的方法，只能设置rgb。rgb最大为8388608，当大于这个值时，应减去255*255*255即16777216
         */
        rgb = (red * 256 + green) * 256 + blue;
        if (rgb > 8388608) {
          rgb = rgb - 16777216;
        }
        //将rgb值写回图片
        originalImage.setRGB(i, j, rgb);
      }

    }

    return originalImage;
  }

  @Test
  public void test01() throws IOException {
    File file = new File("..\\img\\b.png");
    BufferedImage read = ImageIO.read(file);

    //获取图像的宽度和高度
    int width = read.getWidth();
    int height = read.getHeight();

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {//行扫描
        read.setRGB(j, i, 0xff8c2028);
      }
    }
    // 应为是流，所以要使用 ImageIO将流写入文件
    ImageIO.write(read, "PNG", file);
  }

  @Test
  public void test01_2() throws IOException {
    File file = new File("..\\img\\a.png");
    BufferedImage read = ImageIO.read(file);

    BufferedImage grayPicture = getGrayPicture(read);

    ImageIO.write(grayPicture, "PNG", new File("./aaa.png"));
  }

  @Test
  public void test02() {
    int a = 0xff8c2028;  //一个16进制数

    System.out.println(a);  //打印成int
    System.out.println(String.format("%08x", a));  //打印成标准16进制

    System.out.println(String.valueOf((long) a));
  }

  @Test
  public void test03() {
    long x = 4287373352L;
    String hex = Long.toHexString(x);
    System.out.println(hex);

    String hex_2 = "ff8c2028";
    Long x_2 = Long.parseLong(hex_2, 16);
    System.out.println(x_2);
  }

  @Test
  public void test04() {
    int pow = (int) Math.pow(2, 32);
    System.out.println(pow);  //2147483647
    System.out.println(pow + 1);  //-2147483648
  }

}
