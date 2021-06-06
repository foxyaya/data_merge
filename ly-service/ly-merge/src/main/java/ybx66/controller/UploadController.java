package ybx66.controller;

import cn.ybx66.conmmon.pojo.PageDTO;
import cn.ybx66.conmmon.vo.ResultMessageDTO;
import cn.ybx66.data_merge.pojo.Upload;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ybx66.service.UploadService;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/5/1 13:29
 * @description
 */
@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    public UploadService uploadService;

    /**
     * 新增用户数据
     * @param upload
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @PostMapping("/add")
    public ResultMessageDTO add(@RequestBody Upload upload) throws InvocationTargetException, IllegalAccessException {
        return uploadService.add(upload);
    }

    /**
     * 删除文件记录 实际文件不会被删除
     * @param id
     * @return
     */
    @DeleteMapping("/delete")
    public ResultMessageDTO delete(@RequestParam String id){
        return uploadService.delete(id);
    }

    /**
     * 查询用户拥有的文件列表
     *
     */
    @PostMapping("/get")
    public ResultMessageDTO get(@RequestBody PageDTO pageDTO){
        return uploadService.get(pageDTO);
    }

    /**
     * 管理员查询所有文件
     * @return
     */
    @GetMapping("/getAll")
    @RequiresRoles("admin")
    public ResultMessageDTO get(){
        return uploadService.getAll();
    }

}
