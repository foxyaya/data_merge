package cn.ybx66.data_merge.pojo;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 文件pojo类
 */

@Data
@Table(name="tb_upload")
public class Upload implements Serializable {

  @Id
  @Column(name = "id")
  private String id;

  @Column(name = "name")
  private String name;

  @Column(name = "user_id")
  private String userId;

  @Column(name = "url")
  private String url;

  @Column(name = "flag")
  private Integer flag;

  @Column(name = "last_time")
  private Date lastTime;


}
