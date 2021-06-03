package ybx66.service;

import cn.ybx66.data_merge.pojo.Goods;
import com.github.pagehelper.PageInfo;


import java.util.List;

/****
 * @Author:Fox
 * @Description:Goods业务层接口
 * @Date 2019/6/14 0:16
 *****/
public interface GoodsService {

    /***
     * Goods多条件分页查询
     * @param goods
     * @param page
     * @param size
     * @return
     */
    PageInfo<Goods> findPage(Goods goods, int page, int size);

    /***
     * Goods分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<Goods> findPage(int page, int size);

    /***
     * Goods多条件搜索方法
     * @param goods
     * @return
     */
    List<Goods> findList(Goods goods);


    /***
     * 删除Goods
     * @param id
     */
    int delete(String id);

    /***
     * 修改Goods数据
     * @param goods
     */
    int update(Goods goods);
    /**
     * 构建Goods的数据查询pojo
     */
//    Goods makeGoods(Goods goods);

    /***
     * 新增Goods
     * @param goods
     */
    Long add(Goods goods);

    /**
     * 根据ID查询Goods
     * @param id
     * @return
     */
     Goods findById(String id);

    /***
     * 查询所有Goods
     * @return
     */
    List<Goods> findAll();
}
