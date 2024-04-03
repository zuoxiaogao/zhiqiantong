package com.zhiqiantong.content;

import com.zhiqiantong.content.config.MultipartSupportConfig;
import com.zhiqiantong.content.feignclient.MediaServiceClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author Mr.M
 * @version 1.0
 * @description 测试使用feign远程上传文件
 * @date 2022/9/20 20:36
 */
@SpringBootTest
public class FeignUploadTest {

    @Autowired
    MediaServiceClient mediaServiceClient;

    //远程调用，上传文件
    @Test
    public void test() {
    
        MultipartFile multipartFile = MultipartSupportConfig.getMultipartFile(new File("F:\\develop\\test.html"));
        mediaServiceClient.uploadFile(multipartFile,"course/test.html");
    }

}
