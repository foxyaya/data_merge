package ybx66.service.impl;

import cn.ybx66.conmmon.pojo.PageDTO;
import cn.ybx66.conmmon.pojo.PageResDTO;
import cn.ybx66.conmmon.utils.BeanUtil;
import cn.ybx66.conmmon.vo.ResultDescDTO;
import cn.ybx66.conmmon.vo.ResultMessageDTO;
import cn.ybx66.data_merge.pojo.Shop;
import cn.ybx66.data_merge.pojo.Upload;
import cn.ybx66.userapi.feigin.UserFigin;
import cn.ybx66.userapi.pojo.User;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import ybx66.dto.ShopDTO;
import ybx66.dto.ShopResDTO;
import ybx66.mapper.ShopMapper;
import ybx66.service.ShopService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/5/2 11:34
 * @description
 */
@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    public ShopMapper shopMapper;

    @Autowired
    public UserFigin userFigin;

    @Override
    public ResultMessageDTO delete(String id) {
        Shop list = shopMapper.selectByPrimaryKey(id);
        if (list==null || list.getFlag()==0){
            return new ResultMessageDTO(400, ResultDescDTO.FAIL,"商铺不存在");
        }
        list.setFlag(0);
        int i = shopMapper.updateByPrimaryKey(list);
        return i==1 ? new ResultMessageDTO(200, ResultDescDTO.OK,"删除成功") : new ResultMessageDTO(400, ResultDescDTO.FAIL,"删除失败");
    }

    /**
     * 更新店铺信息
     * @param shop
     * @return
     */
    @Override
    public ResultMessageDTO update(Shop shop) {
        int i = shopMapper.updateByPrimaryKey(shop);
        return i==1 ? new ResultMessageDTO(200, ResultDescDTO.OK,"更新成功") : new ResultMessageDTO(400, ResultDescDTO.FAIL,"更新失败");
    }

    @Override
    public ResultMessageDTO add(Shop shop) {
        //先查询用户是否有店铺 不管是否被限制 用就是不能开店
//        Shop select = new Shop();
//        select.setUserId(shop.getUserId());
//        List<Shop> list = shopMapper.select(select);
//        if (list!=null || list.size()!=0) return new ResultMessageDTO(400, ResultDescDTO.FAIL,"已开店/曾已开店");
        int res =0;
        try{
            BeanUtil.autoSetAttrOnInsert(shop);
            shop.setFlag(2);
            res = shopMapper.insert(shop);
        }catch (Exception e){
            e.printStackTrace();
            return new ResultMessageDTO(400, ResultDescDTO.FAIL,"商铺名重复");
        }
        if (res==0){
            return new ResultMessageDTO(400, ResultDescDTO.FAIL,"新增失败");
        }
        return new ResultMessageDTO(200, ResultDescDTO.OK,shop.getId());
    }

    @Override
    public ResultMessageDTO findById(String id) {
        Shop shop = shopMapper.selectByPrimaryKey(id);
        if (shop==null || shop.getFlag()==0){
            return new ResultMessageDTO(400, ResultDescDTO.FAIL,"查找失败");
        }
        return new ResultMessageDTO(200, ResultDescDTO.OK,shop);
    }

    @Override
    public ResultMessageDTO findAllByOnSelf(String userId) {
         userId = JSONObject.parseObject(userId).getString("userId");
        if (userId==null|| userId.equals("")){
            return new ResultMessageDTO(400,ResultDescDTO.FAIL,"未登录");
        }
        List<String> names =new ArrayList<>();
        try{
            Example example = new Example(Shop.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("userId",userId);
            criteria.andEqualTo("flag",1);
            criteria.orEqualTo("userId",userId);
            criteria.andEqualTo("flag",2);
            List<Shop> shopList = shopMapper.selectByExample(example);
            if (shopList!=null && shopList.size()!=0){
                for (Shop shop : shopList) {
                    if (shop!=null){
                        if (shop.getShopName()!=null && !shop.getShopName().equals("")){
                            names.add(shop.getShopName());
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResultMessageDTO(200,ResultDescDTO.OK,names);
    }

    @Override
    public ResultMessageDTO findAllByPayOnSelf(String userId) {
        userId = JSONObject.parseObject(userId).getString("userId");
        if (userId==null|| userId.equals("")){
            return new ResultMessageDTO(400,ResultDescDTO.FAIL,"未登录");
        }
        List<String> names =new ArrayList<>();
        try{
            Example example = new Example(Shop.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("userId",userId);
            criteria.andEqualTo("flag",1);
            List<Shop> shopList = shopMapper.selectByExample(example);
            if (shopList!=null && shopList.size()!=0){
                for (Shop shop : shopList) {
                    if (shop!=null){
                        if (shop.getShopName()!=null && !shop.getShopName().equals("")){
                            names.add(shop.getShopName());
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResultMessageDTO(200,ResultDescDTO.OK,names);
    }

    @Override
    public ResultMessageDTO findAllByAll() {
        List<String> names =new ArrayList<>();
        try{
            Example example = new Example(Shop.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("flag",1);
            criteria.orEqualTo("flag",2);
            List<Shop> shopList = shopMapper.selectByExample(example);
            if (shopList!=null && shopList.size()!=0){
                for (Shop shop : shopList) {
                    if (shop!=null){
                        if (shop.getShopName()!=null && !shop.getShopName().equals("")){
                            names.add(shop.getShopName());
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResultMessageDTO(200,ResultDescDTO.OK,names);
    }

    @Override
    public ResultMessageDTO findAll(ShopDTO shopDTO) {
        if (shopDTO.getUserId()==null|| shopDTO.getUserId().equals("")){
            return new ResultMessageDTO(400,ResultDescDTO.FAIL,"未登录");
        }
        List<Shop> shopList =null;
        int sum =0;
        try{
            Example example = new Example(Shop.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("userId",shopDTO.getUserId());
            criteria.andEqualTo("flag",1);
            criteria.orEqualTo("userId",shopDTO.getUserId());
            criteria.andEqualTo("flag",2);
            //分页
            Page<Shop> uploadPage = PageHelper.startPage(shopDTO.getPage(), shopDTO.getLimit());
            shopList = shopMapper.selectByExample(example);
            //分页解析
            PageInfo<Shop> pageInfo = new PageInfo<Shop>(uploadPage.getResult());
            sum=(int)pageInfo.getTotal();
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResultMessageDTO(200,ResultDescDTO.OK,new PageResDTO(sum,shopList));
    }

    @Override
    public ResultMessageDTO findAllbyAdmin(ShopDTO shopDTO) {
        List<ShopResDTO> shopResDTOS= null;
        List<Shop> shopList =null;
        int sum =0;
        try{
            Example example = new Example(Shop.class);
            Example.Criteria criteria = example.createCriteria();
//            criteria.andEqualTo("flag",1);
            if (shopDTO.getShopName()==null || shopDTO.getShopName().equals("")){
                criteria.andLike("shopName",null);
            }else {
                criteria.andLike("shopName","%"+shopDTO.getShopName()+"%");
            }
            criteria.andEqualTo("flag",1);
            criteria.orEqualTo("flag",2);
            if (shopDTO.getShopName()==null || shopDTO.getShopName().equals("")){
                criteria.andLike("shopName",null);
            }else {
                criteria.andLike("shopName","%"+shopDTO.getShopName()+"%");
            }
            //分页
            Page<Shop> uploadPage = PageHelper.startPage(shopDTO.getPage(), shopDTO.getLimit());
            shopList = shopMapper.selectByExample(example);
            if (shopList!=null && shopList.size()!=0){
                shopResDTOS = new ArrayList<>();
                for (Shop shop : shopList) {
                    ShopResDTO shopResDTO = new ShopResDTO();
                    BeanUtils.copyProperties(shop,shopResDTO);
//                    //增添所属用户
//                    try{
//                        ResultMessageDTO userInfo = userFigin.getUserInfo(shop.getUserId());
//                        User user = JSON.parseObject(JSON.toJSONString(userInfo.getMessage()), User.class);
//                        shopResDTO.setOwn(user.getUsername());
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
                    shopResDTOS.add(shopResDTO);
                }
            }
            //分页解析
            PageInfo<Shop> pageInfo = new PageInfo<Shop>(uploadPage.getResult());
            sum=(int)pageInfo.getTotal();
        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResultMessageDTO(200,ResultDescDTO.OK,new PageResDTO(sum,shopResDTOS));
    }
}
