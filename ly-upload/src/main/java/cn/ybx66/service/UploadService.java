package cn.ybx66.service;

import cn.ybx66.config.UploadProperties;
import cn.ybx66.controller.UploadController;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.domain.ThumbImageConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @author Fox
 * @version 1.0
 * @date 2020/3/6 18:43
 */
@Service
@Slf4j
//@EnableConfigurationProperties(UploadProperties.class)
public class UploadService {

//    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);
    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private UploadProperties prop;

    @Autowired
    private ThumbImageConfig thumbImageConfig; //缩略图

    // 支持的文件类型
    //private static final List<String> suffixes = Arrays.asList("image/png", "image/jpeg");

    public String upload(MultipartFile file) {
        try {
            // 1、图片信息校验
            // 1)校验文件类型
            String type = file.getContentType();
            if (!prop.getAllowTypes().contains(type)) {
                log.info("上传失败，文件类型不匹配：{}", type);
                return null;
            }
            // 2)校验图片内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                log.info("上传失败，文件内容不符合要求");
                return null;
            }
            // 2、保存图片
            // 2.1、生成保存目录
//            File dir = new File("D:\\heima\\upload");
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }
//
//            // 2.2、保存图片
//            file.transferTo(new File(dir, file.getOriginalFilename()));
            //上传到fastDFS
            String extension = StringUtils.substringAfterLast(file.getOriginalFilename(),".");//file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."+1));
            //原图
            StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), extension, null);
            //缩略图
            StorePath storePath1 = storageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(), extension, null);
            // 2.3、拼接图片地址
            //String url = prop.getBaseUrl() +storePath.getFullPath();
            String url =prop.getBaseUrl()+thumbImageConfig.getThumbImagePath(storePath1.getFullPath());
//            log.info(url);
            return url;
        } catch (Exception e) {
            return null;
        }
    }
}