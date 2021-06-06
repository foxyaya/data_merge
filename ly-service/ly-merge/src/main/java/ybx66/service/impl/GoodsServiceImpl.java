package ybx66.service.impl;

import cn.ybx66.conmmon.pojo.PageResDTO;
import cn.ybx66.conmmon.utils.BeanUtil;
import cn.ybx66.conmmon.vo.ResultDescDTO;
import cn.ybx66.conmmon.vo.ResultMessageDTO;
import cn.ybx66.data_merge.pojo.Goods;
import cn.ybx66.data_merge.pojo.Shop;
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
import ybx66.dto.GoodsDTO;
import ybx66.dto.GoodsResDTO;
import ybx66.mapper.GoodsMapper;
import ybx66.mapper.ShopMapper;
import ybx66.service.GoodsService;
import ybx66.service.ShopService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/5/3 3:04
 * @description
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    public GoodsMapper goodsMapper;
    @Autowired
    public ShopService shopService;
    @Autowired
    public ShopMapper shopMapper;
    @Override
    public ResultMessageDTO delete(String id) {
        id = JSONObject.parseObject(id).getString("id");
        Goods list = goodsMapper.selectByPrimaryKey(id);
        if (list==null || list.getFlag()==0){
            return new ResultMessageDTO(400, ResultDescDTO.FAIL,"商品不存在");
        }
        list.setFlag(0);
        int i = goodsMapper.updateByPrimaryKey(list);
        return i==1 ? new ResultMessageDTO(200, ResultDescDTO.OK,"删除成功") : new ResultMessageDTO(400, ResultDescDTO.FAIL,"删除失败");

    }

    @Override
    public ResultMessageDTO update(Goods goods) {
        String shopNames = goods.getShopId();
        if (shopNames!=null && !shopNames.equals("")){
            Shop shop = new Shop();
            shop.setShopName(shopNames);
            List<Shop> select = shopMapper.select(shop);
            goods.setShopId(select.get(0).getId());
        }
        int i = goodsMapper.updateByPrimaryKey(goods);
        return i==1 ? new ResultMessageDTO(200, ResultDescDTO.OK,"更新成功") : new ResultMessageDTO(400, ResultDescDTO.FAIL,"更新失败");

    }

    @Override
    public ResultMessageDTO add(Goods goods) {
        int res =0;
        try{
            BeanUtil.autoSetAttrOnInsert(goods);
            String shopNames = goods.getShopId();
            if (shopNames!=null && !shopNames.equals("")){
                Shop shop = new Shop();
                shop.setShopName(shopNames);
                List<Shop> select = shopMapper.select(shop);
                goods.setShopId(select.get(0).getId());
            }
            res = goodsMapper.insert(goods);
        }catch (Exception e){
            e.printStackTrace();
            return new ResultMessageDTO(400, ResultDescDTO.FAIL,"异常");
        }
        if (res==0){
            return new ResultMessageDTO(400, ResultDescDTO.FAIL,"新增失败");
        }
        return new ResultMessageDTO(200, ResultDescDTO.OK,goods.getId());
    }

    @Override
    public ResultMessageDTO findById(String id) {
        id = JSONObject.parseObject(id).getString("id");
        Goods goods = goodsMapper.selectByPrimaryKey(id);
        if (goods==null || goods.getFlag()==0){
            return new ResultMessageDTO(400, ResultDescDTO.FAIL,"查找失败");
        }
        return new ResultMessageDTO(200, ResultDescDTO.OK,goods);
    }

    @Override
    public ResultMessageDTO findAll(GoodsDTO goodsDTO) {
        //获取店铺id
        if (goodsDTO.getShopName()==null || goodsDTO.getShopName().equals("")){
            return new ResultMessageDTO(400,ResultDescDTO.OK,"先选择店铺名");
        }
        String shopNames = goodsDTO.getShopName();
        String shopId=null;
        if (shopNames!=null && !shopNames.equals("")){
            Shop shop = new Shop();
            shop.setShopName(shopNames);
            List<Shop> select = shopMapper.select(shop);
            shopId =select.get(0).getId();
        }
        List<Goods> goodsList =null;
        int sum =0;
        try{
            Example example = new Example(Goods.class);
            Example.Criteria criteria = example.createCriteria();
            if (goodsDTO.getGoodsType()==null || goodsDTO.getGoodsType().equals("")){
                criteria.andEqualTo("typeId",null);
            }else {
                criteria.andEqualTo("typeId",goodsDTO.getGoodsType());
            }
            criteria.andEqualTo("shopId",shopId);
            criteria.andEqualTo("flag",1);
            if (goodsDTO.getGoodsName()==null || goodsDTO.getGoodsName().equals("")){
                criteria.andLike("goodsName",null);
            }else {
                criteria.andLike("goodsName","%"+goodsDTO.getGoodsName()+"%");
            }
            //分页
            Page<Shop> uploadPage = PageHelper.startPage(goodsDTO.getPage(), goodsDTO.getLimit());
            goodsList = goodsMapper.selectByExample(example);
            //分页解析
            PageInfo<Shop> pageInfo = new PageInfo<Shop>(uploadPage.getResult());
            sum=(int)pageInfo.getTotal();
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResultMessageDTO(200,ResultDescDTO.OK,new PageResDTO(sum,goodsList));
    }

    @Override
    public ResultMessageDTO findAllByAdmin(GoodsDTO goodsDTO) {
        String shopNames = goodsDTO.getShopName();
        String shopId=null;
        if (shopNames!=null && !shopNames.equals("")){
            Shop shop = new Shop();
            shop.setShopName(shopNames);
            List<Shop> select = shopMapper.select(shop);
            shopId =select.get(0).getId();
        }
        List<Goods> goodsList =null;
        List<GoodsResDTO> goodsResDTOS=null;
        int sum =0;
        try{
            Example example = new Example(Goods.class);
            Example.Criteria criteria = example.createCriteria();
            if (goodsDTO.getGoodsType()==null || goodsDTO.getGoodsType().equals("")){
                criteria.andEqualTo("typeId",null);
            }else {
                criteria.andEqualTo("typeId",goodsDTO.getGoodsType());
            }
            criteria.andEqualTo("flag",1);
            criteria.andEqualTo("shopId",shopId);
            if (goodsDTO.getGoodsName()==null || goodsDTO.getGoodsName().equals("")){
                criteria.andLike("goodsName",null);
            }else {
                criteria.andLike("goodsName","%"+goodsDTO.getGoodsName()+"%");
            }

            //分页
            Page<Shop> uploadPage = PageHelper.startPage(goodsDTO.getPage(), goodsDTO.getLimit());
            goodsList = goodsMapper.selectByExample(example);
            //分页解析
            PageInfo<Shop> pageInfo = new PageInfo<Shop>(uploadPage.getResult());
            if (goodsList!=null && goodsList.size()!=0){
                goodsResDTOS =new ArrayList<>();
                for (Goods goods : goodsList) {
                    GoodsResDTO goodsResDTO = new GoodsResDTO();
                    BeanUtils.copyProperties(goods,goodsResDTO);
                    ResultMessageDTO byId = shopService.findById(goods.getShopId());
                    //存在的获取店铺异常的情况
                    try{
                        Shop shop = JSON.parseObject(JSON.toJSONString(byId.getMessage()), Shop.class);
                        goodsResDTO.setShopName(shop.getShopName());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    goodsResDTOS.add(goodsResDTO);
                }
            }
            sum=(int)pageInfo.getTotal();
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResultMessageDTO(200,ResultDescDTO.OK,new PageResDTO(sum,goodsResDTOS));
    }
}
