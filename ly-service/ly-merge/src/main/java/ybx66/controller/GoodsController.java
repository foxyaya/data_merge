package ybx66.controller;


import cn.ybx66.conmmon.vo.ResultMessageDTO;
import cn.ybx66.data_merge.pojo.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ybx66.dto.GoodsDTO;
import ybx66.service.GoodsService;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/5/3 11:57
 * @description 商品表
 */
@RestController
@RequestMapping("/goods")
public class GoodsController{

    @Autowired
    public GoodsService goodsService;

    @PostMapping("/add")
    public ResultMessageDTO add(@RequestBody Goods goods){
        return goodsService.add(goods);
    }

    @DeleteMapping("/delete")
    public ResultMessageDTO delete(@RequestBody String id){
        return goodsService.delete(id);
    }

    @PutMapping("/update")
    public ResultMessageDTO update(@RequestBody Goods goods){
        return goodsService.update(goods);
    }

    @PostMapping("/findById")
    public ResultMessageDTO findById(@RequestBody String id){
        return goodsService.findById(id);
    }

    @PostMapping("/findAll")
    public ResultMessageDTO findAll(@RequestBody GoodsDTO goodsDTO){
        return goodsService.findAll(goodsDTO);
    }

    @PostMapping("/findAllByAdmin")
    public ResultMessageDTO findAllByAdmin(@RequestBody GoodsDTO goodsDTO){
        return goodsService.findAllByAdmin(goodsDTO);
    }
}
