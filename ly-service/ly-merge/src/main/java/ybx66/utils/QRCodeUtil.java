package ybx66.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Hashtable;

/**
 * @Author: nullWagesException
 * @Date: 2019/9/1 18:29
 * @Description:
 */
public class QRCodeUtil {

    // 二维码尺寸
    public static final int QRCODE_SIZE = 300;

    // 存放二维码的路径
    public static final String PAY_PATH = "C://Users/ACER/Desktop/表情包";

    /**
     * 生成二维码
     * @param content   源内容
     * @param outputStream 输出流
     * @throws Exception
     */
    public static void createImage(String content, OutputStream outputStream) throws Exception {
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE,
                hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        // 存到outputStream中
        ImageIO.write(image, "jpg", outputStream);
        outputStream.close();
    }

    /**
     * 生成微信二维码
     * @param content   源内容
     * @throws Exception
     */
    public static File createRemoteImage(String content) throws Exception {
        File file = new File(PAY_PATH,"test"+".jpg");
        if (!file.exists()){
            file.createNewFile();
        }
        QRCodeUtil.createImage(content, new FileOutputStream(file));
        return file;
    }

//    public static void main(String[] args) throws Exception {
////        createRemoteImage("<!DOCTYPE html><html> <head><title>我的第一个 HTML 页面</title></head><body><p>body 元素的内容会显示在浏览器中。</p><p>title 元素的内容会显示在浏览器的标题栏中。</p></body></html>");
//        createRemoteImage("weixin://wxpay/bizpayurl?pr=VjQsAhszz");
//    }
}
