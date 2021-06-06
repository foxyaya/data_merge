package ybx66.service;

import cn.ybx66.conmmon.pojo.PageDTO;
import cn.ybx66.conmmon.vo.ResultMessageDTO;
import cn.ybx66.data_merge.pojo.Upload;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/5/1 13:04
 * @description crud
 */
public interface UploadService {
    /**
     * 新增文件
     * @param upload
     * @return
     */
    ResultMessageDTO add(Upload upload) throws InvocationTargetException, IllegalAccessException;

    /**
     * 删除文件
     * @param upload
     * @return
     */
    ResultMessageDTO delete(String  id);

    /**
     * 查询个人的数据
     * @param userId
     * @return
     */
    ResultMessageDTO get(PageDTO pageDTO);
    /**
     * 查询全部的数据
     */
    ResultMessageDTO getAll();
}
