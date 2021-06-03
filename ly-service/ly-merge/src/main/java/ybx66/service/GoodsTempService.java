package ybx66.service;

import cn.ybx66.data_merge.pojo.GoodsTemp;
import com.github.pagehelper.PageInfo;


import java.util.List;

/****
 * @Author:Fox
 * @Description:GoodsTemp业务层接口
 * @Date 2019/6/14 0:16
 *****/
public interface GoodsTempService{

    /***
     * GoodsTemp多条件分页查询
     * @param goodsTemp
     * @param page
     * @param size
     * @return
     */
    PageInfo<GoodsTemp> findPage(GoodsTemp goodsTemp, int page, int size);

    /***
     * GoodsTemp分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<GoodsTemp> findPage(int page, int size);

    /***
     * GoodsTemp多条件搜索方法
     * @param goodsTemp
     * @return
     */
    List<GoodsTemp> findList(GoodsTemp goodsTemp);


    /***
     * 删除GoodsTemp
     * @param id
     */
    int delete(String id);

    /***
     * 修改GoodsTemp数据
     * @param goodsTemp
     */
    int update(GoodsTemp goodsTemp);
    /**
     * 构建GoodsTemp的数据查询pojo
     */
//    GoodsTemp makeGoodsTemp(GoodsTemp goodsTemp);

    /***
     * 新增GoodsTemp
     * @param goodsTemp
     */
    Long add(GoodsTemp goodsTemp);

    /**
     * 根据ID查询GoodsTemp
     * @param id
     * @return
     */
     GoodsTemp findById(String id);

    /***
     * 查询所有GoodsTemp
     * @return
     */
    List<GoodsTemp> findAll();
}
