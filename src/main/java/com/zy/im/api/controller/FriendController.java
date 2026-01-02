package com.zy.im.api.controller;

import com.zy.im.api.dto.request.ApplyFriendRequest;
import com.zy.im.application.service.FriendService;
import com.zy.im.common.BaseResponse;
import com.zy.im.common.ResultUtils;
import com.zy.im.common.security.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friend")
public class FriendController {

    private final FriendService friendService;

    @PostMapping("/apply")
    public BaseResponse<String> applyFriendRequest(@Validated @RequestBody ApplyFriendRequest request){
        String uuid = UserContext.getUserId();
        String applyUuid = request.getApplyUuid();
        friendService.apply(uuid,applyUuid);

        return ResultUtils.success("好友申请已发送");
    }
}
