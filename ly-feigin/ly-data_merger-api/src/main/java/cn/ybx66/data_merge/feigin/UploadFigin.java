package cn.ybx66.data_merge.feigin;

import cn.ybx66.conmmon.vo.ResultMessageDTO;
import cn.ybx66.data_merge.config.FeignConfiguration;
import cn.ybx66.data_merge.pojo.Upload;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/5/1 13:41
 * @description 上传下载Feigin
 */
@FeignClient(name = "merger",configuration = FeignConfiguration.class)
@RequestMapping("/upload")
public interface UploadFigin {
    /**
     * 删除文件记录 实际文件不会被删除
     * @param id
     * @return
     */
    @DeleteMapping("/delete")
     ResultMessageDTO delete(@RequestParam String id);

    /**
     * 新增文件数据
     * @param upload
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @PostMapping("/add")
     ResultMessageDTO add(@RequestBody Upload upload) throws InvocationTargetException, IllegalAccessException;
}
