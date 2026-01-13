package com.zy.im.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AgreeFriendRequest {
    @NotBlank(message = "申请人 uuid 不能为空")
    private String fromUuid;
}
