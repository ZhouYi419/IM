package com.zy.im.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class FriendRelation extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;
    // uuid
    private String uuid;
    // 好友uuid
    private String friendUuid;
    // 好友状态 0正常 1已删除
    private Integer status;
}
