package com.zy.im.api.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApplyListResponse {
    // 申请人uuid
    private String applyUuid;
    // 申请人名字
    private String name;
    // 申请人头像
    private String avatar;
    // 申请原因
    private String reason;
    // 申请时间
    private LocalDateTime applyTime;
}
