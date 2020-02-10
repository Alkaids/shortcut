package com.gravel.shortcut.service;

import com.gravel.shortcut.domain.Result;

/**
 * @ClassName UrlConvertService
 * @Description: TODO
 * @Author gravel
 * @Date 2020/1/29
 * @Version V1.0
 **/
public interface UrlConvertService {

    /**
     * 得到短地址URL
     *
     * @param url
     * @return
     */
    Result<String> convertUrl(String url);

    /**
     * 将短地址URL 转换为正常的地址
     *
     * @param shortUrl
     * @return
     */
    Result<String> revertUrl(String shortUrl);

}
