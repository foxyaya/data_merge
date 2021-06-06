package cn.ybx66.controller;

import cn.ybx66.conmmon.vo.ResultMessageDTO;
import cn.ybx66.service.UploadService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;

/**
 * @author Fox
 * @version 1.0
 * @date 2020/3/6 18:42
 */
@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private UploadService uploadService;

    /**
     * 上传图片功能
     * @param file
     * @return
     */
    @PostMapping("/image/{userId}")
    public ResponseEntity<String> uploadImage(@PathVariable("userId") String userId, @RequestParam("file") MultipartFile file) {
        String url = this.uploadService.upload(userId,file);
        if (StringUtils.isBlank(url)) {
            // url为空，证明上传失败
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // 返回200，并且携带url路径
        return ResponseEntity.ok(url);
    }
    /**
     * 上传图片功能-头像
     * @param file
     * @return
     */
    @PostMapping("/headImage")
    public ResponseEntity<String> uploadHeadImage(@RequestParam("file") MultipartFile file) {
        String url = this.uploadService.uploadMin(file);
        if (StringUtils.isBlank(url)) {
            // url为空，证明上传失败
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // 返回200，并且携带url路径
        return ResponseEntity.ok(url);
    }
    @GetMapping("/get")//url为图片名称+格式            ?url=21161616.jpg
    public ResultMessageDTO getFile(@RequestParam String url){
        return uploadService.getFile(url);
    }
}
