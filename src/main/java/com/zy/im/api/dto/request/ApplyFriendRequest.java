package com.zy.im.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ApplyFriendRequest {
    // 被申请人uuid
    @NotBlank(message = "被申请人不能为空")
    private String applyUuid;

    // 申请理由
    private String reason;
}
