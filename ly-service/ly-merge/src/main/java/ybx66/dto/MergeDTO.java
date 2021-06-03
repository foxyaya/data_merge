package ybx66.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/4/27 16:27
 * @description 同一系统mysql中不同库/表数据的整合
 */
@Data
public class MergeDTO implements Serializable {
    //库名
    public String dataA;

    public String dataB;

    //表
    public String tableA;

    public String tableB;

    //列名
    public String[] columnA;

    public String[] columnE;
}
