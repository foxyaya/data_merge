package cn.ybx66.item.service;

import cn.ybx66.conmmon.enums.ExceptionEnums;
import cn.ybx66.conmmon.exception.LyException;
import cn.ybx66.item.mapper.CategoryMapper;
import cn.ybx66.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author Fox
 * @version 1.0
 * @date 2020/2/25 13:09
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> queryCategoryListByPid(Long pid) {
        Category category = new Category();
        category.setParentId(pid);
        List<Category> list = categoryMapper.select(category);
        if (CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnums.CATEGORY_NOT_FOND);
        }
        return list;
    }
}
