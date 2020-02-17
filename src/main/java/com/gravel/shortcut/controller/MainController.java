package com.gravel.shortcut.controller;

import com.google.zxing.WriterException;
import com.gravel.shortcut.domain.Result;
import com.gravel.shortcut.domain.ResultGenerator;
import com.gravel.shortcut.service.UrlConvertService;
import com.gravel.shortcut.utils.QRcodeUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @ClassName MainController
 * @Description: 主控制器
 * @Author gravel
 * @Date 2020/1/29
 * @Version V1.0
 **/
@RestController
public class MainController {

    @Resource
    private UrlConvertService urlConvertService;

    /**
     * 传入url 返回 转换成功的url
     *
     * @param url
     * @return
     */
    @PostMapping("/convert")
    public Result<String> convertUrl(@RequestParam String url) {
        return ResultGenerator.genSuccessResult(urlConvertService.convertUrl(url));
    }

    @GetMapping(value = "/qrcode", produces = MediaType.IMAGE_JPEG_VALUE)
    public BufferedImage getImage(@RequestParam String url) throws IOException, WriterException {
        return QRcodeUtils.QREncode(url);
    }

    @PostMapping("/revert")
    public Result<String> revertUrl(@RequestParam String shortUrl) {
        return ResultGenerator.genSuccessResult(urlConvertService.revertUrl(shortUrl));

    }

}
