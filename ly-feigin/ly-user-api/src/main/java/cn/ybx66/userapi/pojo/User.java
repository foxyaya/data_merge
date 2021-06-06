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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_user")
public class User implements Serializable {
    @Id
    @Column(name = "id")
    public String id;

    @Column(name = "user_name")
    public String username;

    @Column(name = "password")
    public String password;

    @Column(name = "head_image")
    public String headImage;

    @Column(name = "phone")
    public String phone;

    @Column(name = "sign")
    public String sign;

    @Column(name = "salt")
    public String salt;

    @Column(name = "flag")
    public Integer flag;

    @Column(name = "ip")
    public String ip;

    @Column(name = "last_time")
    public Date lastTime;
    /**
     * 用户对应的角色集合
     */
    public List<Role> roles;
}
