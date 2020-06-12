package cn.ybx66.item.mapper;

import cn.ybx66.item.pojo.Brand;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author Fox
 * @version 1.0
 * @date 2020/3/3 23:06
 */
@Repository
public interface BrandMapper extends Mapper<Brand> {

    @Insert(
            "INSERT INTO tb_category_brand (category_id,brand_id)values(#{cid},#{bid})"
    )
     int insertCategoryBrand(@Param("cid") Long cid,@Param("bid") Long bid);
}
