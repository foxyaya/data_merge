package cn.ybx66.item.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Fox
 * @version 1.0
 * @date 2020/3/3 23:03
 */
@Table(name="tb_brand")
@Data
public class Brand {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    private String name;

    private String image;

    private Character letter;
}
