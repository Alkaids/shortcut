package com.gravel.shortcut.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName QRcodeUtils
 * @Description: 基于zxing的二维码生成工具
 * @Author gravel
 * @Date 2020/2/17
 * @Version V1.0
 **/
public class QRcodeUtils {


    public static BufferedImage QREncode(String content) throws WriterException, IOException {
        int width = 200;
        int height = 200;
        String format = "png";
        Map<EncodeHintType, Object> hints = new HashMap<>();
        //内容编码格式
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        // 指定纠错等级
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        //设置二维码边的空度，非负数
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        Path tempPath = Paths.get(UUID.randomUUID().toString() + "." + format);

        if (!Files.exists(tempPath)) {
            Files.createFile(tempPath);
        }
        BufferedImage image = toBufferedImage(bitMatrix);
        // 删除中间文件
        Files.delete(tempPath);
        return image;

    }

    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }

        return image;
    }
}
