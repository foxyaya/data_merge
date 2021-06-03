package ybx66.service;

import cn.ybx66.data_merge.pojo.Shop;
import com.github.pagehelper.PageInfo;


import java.util.List;

/****
 * @Author:Fox
 * @Description:Shop业务层接口
 * @Date 2019/6/14 0:16
 *****/
public interface ShopService {

    /***
     * Shop多条件分页查询
     * @param shop
     * @param page
     * @param size
     * @return
     */
    PageInfo<Shop> findPage(Shop shop, int page, int size);

    /***
     * Shop分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<Shop> findPage(int page, int size);

    /***
     * Shop多条件搜索方法
     * @param shop
     * @return
     */
    List<Shop> findList(Shop shop);


    /***
     * 删除Shop
     * @param id
     */
    int delete(String id);

    /***
     * 修改Shop数据
     * @param shop
     */
    int update(Shop shop);
    /**
     * 构建Shop的数据查询pojo
     */
//    Shop makeShop(Shop shop);

    /***
     * 新增Shop
     * @param shop
     */
    Long add(Shop shop);

    /**
     * 根据ID查询Shop
     * @param id
     * @return
     */
     Shop findById(String id);

    /***
     * 查询所有Shop
     * @return
     */
    List<Shop> findAll();
}
