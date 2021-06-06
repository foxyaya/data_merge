package cn.ybx66.service;

import cn.ybx66.config.UploadProperties;
import cn.ybx66.conmmon.utils.BeanUtil;
import cn.ybx66.conmmon.vo.ResultDescDTO;
import cn.ybx66.conmmon.vo.ResultMessageDTO;
import cn.ybx66.data_merge.feigin.UploadFigin;
import cn.ybx66.data_merge.pojo.Upload;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.domain.ThumbImageConfig;
import com.github.tobato.fastdfs.proto.storage.DownloadFileWriter;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;


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

    @Autowired
    private UploadFigin uploadFigin;

    // 支持的文件类型
    //private static final List<String> suffixes = Arrays.asList("image/png", "image/jpeg");

    //原图
    public String upload(String userId,MultipartFile file) {
        try {

//            if (!checkTup(file)) return null;
            String name = file.getOriginalFilename();
            //上传到fastDFS
            String extension = StringUtils.substringAfterLast(file.getOriginalFilename(),".");//file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."+1));
            //原图
            StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), extension, null);

            Upload upload = new Upload();
            try {
                BeanUtil.autoSetAttrOnInsert(upload);
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
            String fullPath = storePath.getFullPath();
            upload.setName(name);
            upload.setUrl(prop.getBaseUrl() +storePath.getFullPath());
            upload.setUserId(userId);
            uploadFigin.add(upload);
            // 2.3、拼接图片地址
            return prop.getBaseUrl() +fullPath;
        } catch (IOException e) {
            log.info("数据流发生异常");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;

    }

    //缩略图
    public String uploadMin(MultipartFile file) {
        try {
//            if (!checkTup(file)) return null;
            String extension = StringUtils.substringAfterLast(file.getOriginalFilename(),".");//file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."+1));
            //缩略图
            StorePath storePath1 = storageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(), extension, null);
            return prop.getBaseUrl()+thumbImageConfig.getThumbImagePath(storePath1.getFullPath());
        } catch (IOException e) {
            log.info("数据流发生异常");
        }
        return null;

    }

    //下载文件  url为图片名称+格式
    public ResultMessageDTO getFile(String url){//url 格式 http://localhost:10010/api/upload/get?url=M00/00/00/rBBdBGBvziqAfpnHAAKgHDZlsvI424_60x60.jpg
        //对url进行分割
        String[] strings = url.split("/");             //断言：两个实现类 DownloadByteArray和DownloadFileWriter  结果集相对应的就是byte[] 和 String文件路径
//        if (!prop.getAllowTypes().contains(StringUtils.substringAfterLast(strings[strings.length-1],"."))){
//            log.info("下载失败，文件类型不支持：{}",StringUtils.substringAfterLast(strings[strings.length-1],"."));
//            return null;
//        }

        String s = storageClient.downloadFile("group1", "M00/00/00/"+url, new DownloadFileWriter("D:\\"+strings[strings.length-1]));//默认d盘
        if ("".equals(s)){
            return new ResultMessageDTO(400, ResultDescDTO.FAIL,"下载失败");
        }
        return new ResultMessageDTO(200, ResultDescDTO.OK,s);
    }

    //检验图片的合格性
    public boolean checkTup(MultipartFile file){
        // 1、图片信息校验
        // 1)校验文件类型
        try {
            String type = file.getContentType();
            if (!prop.getAllowTypes().contains(type)) {
                log.info("上传失败，文件类型不匹配：{}", type);
                return false;
            }
            // 2)校验图片内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                log.info("上传失败，文件内容不符合要求");
                return false;
            }
            return true;

        }catch (IOException e){
            log.info("数据流发生异常");
        }
        return true;
    }

}