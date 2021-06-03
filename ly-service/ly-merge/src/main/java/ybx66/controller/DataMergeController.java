package ybx66.controller;

import cn.ybx66.conmmon.vo.ResultMessageDTO;
import ybx66.dto.ColumnDTO;
import ybx66.dto.MergeDTO;
import ybx66.service.DataMergeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/4/28 15:55
 * @description
 */
@RestController
@RequestMapping("/merger")
public class DataMergeController {

    @Autowired
    private DataMergeService dataMergeService;

    /**
     * 整合指定表数据
     * @param mergeDTO
     * @return
     * @throws SQLException
     */
    @PostMapping("/getMerge")
    public ResultMessageDTO getMerge(@RequestBody MergeDTO mergeDTO) throws SQLException {
        return dataMergeService.getMerge(mergeDTO);
    }

    /**
     * 获取表名 by 数据库
     * @param databaseName
     * @return ResultMessageDTO
     */
    @GetMapping("/getColumn")
    public ResultMessageDTO getColumn(@RequestParam String databaseName){
        return dataMergeService.getColumn(databaseName);
    }

    /**
     * 获取字段名 by 数据库和表
     * @param columnDTO
     * @return
     */
    @PostMapping("/getField")
    public ResultMessageDTO getField(@RequestBody ColumnDTO columnDTO){
        return dataMergeService.getField(columnDTO);
    }

    /**
     * 获取所有数据库
     * @param
     * @return ResultMessageDTO
     */
    @GetMapping("/getDatabase")
    public ResultMessageDTO getDatabase(){
        return dataMergeService.getDatabase();
    }

}
