package ybx66.service;

import cn.ybx66.conmmon.pojo.PageDTO;
import cn.ybx66.conmmon.vo.ResultMessageDTO;
import cn.ybx66.data_merge.pojo.Shop;
import com.github.pagehelper.PageInfo;
import ybx66.dto.ShopDTO;


import java.util.List;

/****
 * @Author:Fox
 * @Description:Shop业务层接口
 * @Date 2019/6/14 0:16
 *****/
public interface ShopService {


    /***
     * 删除Shop
     * @param id
     */
    ResultMessageDTO delete(String id);

    /***
     * 修改Shop数据
     * @param shop
     */
    ResultMessageDTO update(Shop shop);

    /***
     * 新增Shop
     * @param shop
     */
    ResultMessageDTO add(Shop shop);

    /**
     * 根据ID查询Shop
     * @param id
     * @return
     */
    ResultMessageDTO findById(String id);

    //查询自己的店铺
    ResultMessageDTO findAllByOnSelf(String userId);

    //查询自己支付了的店铺
    ResultMessageDTO findAllByPayOnSelf(String userId);

    //查询所有店铺
    ResultMessageDTO findAllByAll();

    /***
     * 查询自己的Shop 用户
     * @return
     */
    ResultMessageDTO findAll(ShopDTO shopDTO);

    /**
     * 查询所有Shop admin
     */
    ResultMessageDTO findAllbyAdmin(ShopDTO shopDTO);
}
