package ybx66.service;

import cn.ybx66.conmmon.vo.ResultMessageDTO;
import ybx66.dto.BestGoodsDTO;
import ybx66.dto.ColumnDTO;
import ybx66.dto.JDBCDTO;
import ybx66.dto.MergeDTO;

import java.sql.SQLException;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/4/28 15:57
 * @description
 */
public interface DataMergeService {

    ResultMessageDTO getMerge(MergeDTO mergeDTO) throws SQLException;

    ResultMessageDTO getOtherMerge(MergeDTO mergeDTO) throws SQLException;

    //获取字段
    ResultMessageDTO getField(ColumnDTO columnDTO);

    ResultMessageDTO getDatabase();

    //获取表名
    ResultMessageDTO getColumn(String databaseName);

    ResultMessageDTO getTwoData(JDBCDTO jdbcdto);

    ResultMessageDTO getBestGoods(BestGoodsDTO bestGoodsDTO);

    //开启任务
    ResultMessageDTO startTask();

    ResultMessageDTO endTask();
    //获取任务是否存在
    ResultMessageDTO getTask();
}
