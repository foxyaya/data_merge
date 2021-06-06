package cn.ybx66.userapi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_role")
public class Role implements Serializable {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "role_name")
    private String roleName;

    @Column(name = "flag")
    public Integer flag;

    @Column(name = "last_time")
    public Date lastTime;
    /**
     * 角色对应权限集合 弃用
     */
    private List<Permissions> permissions;

}
