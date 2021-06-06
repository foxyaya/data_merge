package ybx66.controller;

import cn.ybx66.conmmon.vo.ResultMessageDTO;
import ybx66.anno.Decrypt;
import ybx66.anno.Encrypt;
import ybx66.configure.JdbcConnectConfig;
import ybx66.dto.BestGoodsDTO;
import ybx66.dto.ColumnDTO;
import ybx66.dto.JDBCDTO;
import ybx66.dto.MergeDTO;
import ybx66.service.DataMergeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/4/28 15:55
 * @description 数据整合
 */
@RestController
@RequestMapping("/merger")
public class DataMergeController {

    @Autowired
    public DataMergeService dataMergeService;


    /**
     * 整合指定表数据
     * @param mergeDTO getOtherMerge
     * @return
     * @throws SQLException
     */
    @PostMapping("/getMerge")
    @Decrypt
    @Encrypt
    public ResultMessageDTO getMerge(@RequestBody MergeDTO mergeDTO) throws SQLException {
        return dataMergeService.getMerge(mergeDTO);
    }

    /**
     * 整合其他表数据
     * @param mergeDTO
     * @return
     * @throws SQLException
     */
    @PostMapping("/getOtherMerge")
    @Decrypt
    @Encrypt
    public ResultMessageDTO getOtherMerge(@RequestBody MergeDTO mergeDTO) throws SQLException {
        return dataMergeService.getOtherMerge(mergeDTO);
    }
    @GetMapping("/startTask")
    public ResultMessageDTO startTask()  {
        return dataMergeService.startTask();
    }
    @GetMapping("/endTask")
    public ResultMessageDTO endTask()  {
        return dataMergeService.endTask();
    }
    @GetMapping("/getTask")
    public ResultMessageDTO getTask() {
        return dataMergeService.getTask();
    }

    /**
     * 获取表名 by 数据库
     * @param databaseName
     * @return ResultMessageDTO
     */
    @PostMapping("/getColumn")
    @Encrypt
    public ResultMessageDTO getColumn(@RequestParam String databaseName){
        return dataMergeService.getColumn(databaseName);
    }

    /**
     * 获取字段名 by 数据库和表
     * @param columnDTO
     * @return
     */
    @PostMapping("/getField")
    @Decrypt
    @Encrypt
    public ResultMessageDTO getField(@RequestBody ColumnDTO columnDTO){
        return dataMergeService.getField(columnDTO);
    }

    /**
     * 获取所有数据库
     * @param
     * @return ResultMessageDTO
     */
    @PostMapping("/getDatabase")
    @Encrypt
    public ResultMessageDTO getDatabase(){
        return dataMergeService.getDatabase();
    }

    /**
     * 获取商品推荐
     * @param bestGoodsDTO
     * @return
     */
    @PostMapping("/getBestGoods")
    public ResultMessageDTO getBestGoods(@RequestBody BestGoodsDTO bestGoodsDTO){
        return dataMergeService.getBestGoods(bestGoodsDTO);
    }

    /**
     * 弃用
     * @param jdbcdto
     * @return
     */
    @PostMapping("/getTwoData")
    public ResultMessageDTO getTwoData(JDBCDTO jdbcdto){
        return dataMergeService.getTwoData(jdbcdto);
    }

}
