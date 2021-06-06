package ybx66.mapper;

import cn.ybx66.data_merge.pojo.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/4/28 16:01
 * @description
 */
@Repository
public interface DataMergeMapper  {

    //获取数据库表名
    @Select("select table_name from information_schema.tables where table_schema = #{tableName}")
    List<String> getDataTableName(@Param("tableName") String tableName);
    //获取列名
    @Select("select COLUMN_NAME from information_schema.COLUMNS where TABLE_NAME = #{columnName} and table_schema=#{tableName}")
    List<String> getColumnName(@Param("tableName") String tableName,@Param("columnName") String columnName);
    //获取全部的数据库
    @Select("SELECT SCHEMA_NAME FROM information_schema.SCHEMATA")
    List<String> getDatabase();
    //查询最优商品
    @Select("SELECT * FROM tb_goods_merge WHERE goods_name like CONCAT('%',#{goodsName},'%')  type_id= #{type} and flag = 1 ORDER BY goods_price DESC limit #{limit}")
    List<Goods> getBestGoods(@Param("goodsName") String goodsName,@Param("type")String type,@Param("limit") Integer limit);
    //同数据库数据整合 获取推荐数据 从表tb_goods_merge

}
