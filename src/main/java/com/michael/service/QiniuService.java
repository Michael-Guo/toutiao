package com.michael.service;

import com.alibaba.fastjson.JSONObject;
import com.michael.util.ToutiaoUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by GWC on 2016/7/12.
 */
@Service
public class QiniuService {

    private static final Logger logger = LoggerFactory.getLogger(QiniuService.class);

    //设置好账号的ACCESS_KEY和SECRET_KEY
    String ACCESS_KEY = "ePQOkHeovC9tWBN7ZBZ1fKxEgIi6EHV6BBEFR3ex";
    String SECRET_KEY = "OYHQ2Ko6s7fkNb26lLcZk04mk-pl9H1_8sknJNa7";
    //要上传的空间
    String bucketname = "toutiao";

    //密钥配置
    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    //创建上传对象
    UploadManager uploadManager = new UploadManager();

    private static String QINIU_IMAGE_DOMAIN = "http://oa6x3v184.bkt.clouddn.com/";


    //简单上传，使用默认策略，只需要设置上传的空间名就可以了
    public String getUpToken(){
        return auth.uploadToken(bucketname);
    }

    public String saveImage(MultipartFile file) throws IOException {
        try {
            int dotPos = file.getOriginalFilename().lastIndexOf(".");
            if (dotPos < 0) {
                return null;
            }
            String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
            if (!ToutiaoUtil.isFileAllowed(fileExt)) {
                return null;
            }

            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;
            //调用put方法上传
            Response res = uploadManager.put(file.getBytes(), fileName, getUpToken());
            //打印返回的信息
            //System.out.println(res.bodyString());

            if (res.isOK() && res.isJson()) {
                return QINIU_IMAGE_DOMAIN + JSONObject.parseObject(res.bodyString()).get("key");
            } else {
                logger.error("七牛异常:" + res.bodyString());
                return null;
            }

        } catch (QiniuException e) {
            // 请求失败时打印的异常的信息
            Response r = e.response;
            // 请求失败时打印的异常的信息
            //System.out.println(r.toString());
            logger.error("七牛异常:" + e.getMessage() + r.toString());
            return null;
        }
    }

}