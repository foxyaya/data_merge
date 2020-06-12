package cn.ybx66.item.service;

import cn.ybx66.conmmon.enums.ExceptionEnums;
import cn.ybx66.conmmon.exception.LyException;
import cn.ybx66.conmmon.pojo.PageResult;
import cn.ybx66.item.mapper.BrandMapper;
import cn.ybx66.item.pojo.Brand;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author Fox
 * @version 1.0
 * @date 2020/3/3 23:06
 */
@Service
@Slf4j
public class BrandService {

    @Autowired
    private BrandMapper brandMapper;


    public PageResult<Brand> BrandByList(Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        //分页
        PageHelper.startPage(page,rows);
        //过滤
        Example example = new Example(Brand.class);
        if (StringUtils.isNotBlank(key)){
            //过滤条件
            example.createCriteria().orLike("name","%"+key+"%")
                    .orEqualTo("letter",key.toUpperCase());
        }
        //排序
        if (StringUtils.isNotBlank(sortBy)){
            String orderByClause = sortBy + (desc ? " DESC" : " ASC");
            example.setOrderByClause(orderByClause);
        }

        //查询
        List<Brand> list = brandMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnums.BRAND_NOT_FOUND);
        }

        //解析分页结果
        PageInfo<Brand> info =new PageInfo<>(list);
//        log.info(String.valueOf(info.getTotal()));
        return new PageResult<Brand>(info.getTotal(),list);
    }

    @Transactional
    public void insertBrand(Brand brand,List<Long> cids) {
        brand.setId(null);
        int i = brandMapper.insert(brand);
        if (i!=1){
            throw new LyException(ExceptionEnums.BRAND_INSERT_ERROR);
        }

        for (Long cid : cids) {
            int count = brandMapper.insertCategoryBrand(cid, brand.getId());
            if (count!=1){
                throw new LyException(ExceptionEnums.BRAND_INSERT_ERROR);
            }
        }
    }
}
