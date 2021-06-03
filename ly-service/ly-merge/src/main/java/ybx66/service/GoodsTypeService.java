package ybx66.service;

import cn.ybx66.data_merge.pojo.GoodsType;
import com.github.pagehelper.PageInfo;


import java.util.List;

/****
 * @Author:Fox
 * @Description:GoodsType业务层接口
 * @Date 2019/6/14 0:16
 *****/
public interface GoodsTypeService {

    /***
     * GoodsType多条件分页查询
     * @param goodsType
     * @param page
     * @param size
     * @return
     */
    PageInfo<GoodsType> findPage(GoodsType goodsType, int page, int size);

    /***
     * GoodsType分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<GoodsType> findPage(int page, int size);

    /***
     * GoodsType多条件搜索方法
     * @param goodsType
     * @return
     */
    List<GoodsType> findList(GoodsType goodsType);


    /***
     * 删除GoodsType
     * @param id
     */
    int delete(String id);

    /***
     * 修改GoodsType数据
     * @param goodsType
     */
    int update(GoodsType goodsType);
    /**
     * 构建GoodsType的数据查询pojo
     */
//    GoodsType makeGoodsType(GoodsType goodsType);

    /***
     * 新增GoodsType
     * @param goodsType
     */
    Long add(GoodsType goodsType);

    /**
     * 根据ID查询GoodsType
     * @param id
     * @return
     */
     GoodsType findById(String id);

    /***
     * 查询所有GoodsType
     * @return
     */
    List<GoodsType> findAll();
}
