package ybx66.mapper;

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

    //同数据库数据整合
}
