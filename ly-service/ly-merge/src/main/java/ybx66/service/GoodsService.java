package ybx66.service;

import cn.ybx66.conmmon.vo.ResultMessageDTO;
import cn.ybx66.data_merge.pojo.Goods;
import com.github.pagehelper.PageInfo;
import ybx66.dto.GoodsDTO;


import java.util.List;

/****
 * @Author:Fox
 * @Description:Goods业务层接口
 * @Date 2019/6/14 0:16
 *****/
public interface GoodsService {


    /***
     * 删除Goods
     * @param id
     */
    ResultMessageDTO delete(String id);

    /***
     * 修改Goods数据
     * @param goods
     */
    ResultMessageDTO update(Goods goods);

    /***
     * 新增Goods
     * @param goods
     */
    ResultMessageDTO add(Goods goods);

    /**
     * 根据ID查询Goods
     * @param id
     * @return
     */
     ResultMessageDTO findById(String id);

    /***
     * 查询所有Goods user
     * @return
     */
    ResultMessageDTO findAll(GoodsDTO goodsDTO);
    /***
     * 查询所有Goods admin
     * @return
     */
    ResultMessageDTO findAllByAdmin(GoodsDTO goodsDTO);
}
