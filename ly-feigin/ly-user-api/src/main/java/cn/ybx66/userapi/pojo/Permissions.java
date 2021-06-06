package cn.ybx66.userapi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_permissions")
public class Permissions implements Serializable {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "role_id")
    private String roleId;

    @Column(name= "permissions_name")
    private String permissionsName;
}
