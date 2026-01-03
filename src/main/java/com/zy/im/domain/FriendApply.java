package com.zy.im.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class FriendApply extends BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 申请者 UUID
     */
    private String fromUuid;

    /**
     * 被申请者 UUID
     */
    private String toUuid;

    /**
     * 添加原因
     */
    private String reason;

    /**
     * 状态：
     * 0 - 待处理
     * 1 - 同意
     * 2 - 拒绝
     */
    private Integer status;
}
