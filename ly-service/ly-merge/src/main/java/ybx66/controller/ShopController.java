package ybx66.controller;

import cn.ybx66.conmmon.vo.ResultMessageDTO;
import cn.ybx66.data_merge.pojo.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ybx66.dto.ShopDTO;
import ybx66.service.ShopService;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/5/2 11:33
 * @description 可以考虑数据传输的AES加密传输
 */
@RestController
@RequestMapping("/shop")
public class ShopController {
    @Autowired
    public ShopService shopService;

    /**
     * 新增商店
     * @param shop
     * @return
     */
    @PostMapping("/add")
    public ResultMessageDTO add(@RequestBody Shop shop){
        return shopService.add(shop);
    }

    //更新店铺
    @PutMapping("/update")
    public ResultMessageDTO update(@RequestBody Shop shop){
        return shopService.update(shop);
    }

    //删除Shop
    @DeleteMapping("/delete")
    public ResultMessageDTO delete(@RequestBody String  id){
        return shopService.delete(id);
    }

    //根据ID查询Shop
    @PostMapping("/findById")
    public ResultMessageDTO findById(@RequestBody String id){
        return shopService.findById(id);
    }

    //查询自己的Shop name
    @PostMapping("/findNameAll")
    public ResultMessageDTO findAllByOnSelf(@RequestBody String  userId){
        return shopService.findAllByOnSelf(userId);
    }
    @PostMapping("/findNameByPay")
    public ResultMessageDTO findAllByPayOnSelf(@RequestBody String  userId){
        return shopService.findAllByPayOnSelf(userId);
    }
    @GetMapping("/findAllByAll")
    public ResultMessageDTO findAllByAll(){
        return shopService.findAllByAll();
    }

    //查询自己的Shop 用户
    @PostMapping("/findAll")
    public ResultMessageDTO findAll(@RequestBody ShopDTO shopDTO){
        return shopService.findAll(shopDTO);
    }

    //查询所有Shop admin
    @PostMapping("/findAllByAdmin")
    public ResultMessageDTO findAllbyAdmin(@RequestBody ShopDTO shopDTO){
        return shopService.findAllbyAdmin(shopDTO);
    }


}
