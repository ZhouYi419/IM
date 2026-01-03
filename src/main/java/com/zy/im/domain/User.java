package com.zy.im.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String uuid;
    // 用户名
    private String username;
    // 密码
    private String password;
    //头像
    private String avatar;
    // 状态
    private Integer status;
    // 创建时间
    private LocalDateTime createdTime;
    // 更新时间
    private LocalDateTime updatedTime;

}
