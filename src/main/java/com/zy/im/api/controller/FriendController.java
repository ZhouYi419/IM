package com.zy.im.api.controller;

import com.zy.im.api.dto.request.AgreeFriendRequest;
import com.zy.im.api.dto.request.ApplyFriendRequest;
import com.zy.im.api.dto.response.ApplyListResponse;
import com.zy.im.application.service.FriendService;
import com.zy.im.common.BaseResponse;
import com.zy.im.common.ResultUtils;
import com.zy.im.common.security.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friend")
public class FriendController {

    private final FriendService friendService;

    /**
     * 发起好友申请
     * @param request 申请请求
     * @return 申请结果
     */
    @PostMapping("/apply")
    public BaseResponse<String> applyFriendRequest(@Validated @RequestBody ApplyFriendRequest request){
        friendService.apply(request);
        return ResultUtils.success("好友申请已发送");
    }

    /**
     * 获取好友申请列表
     * @return 申请列表
     */
    @GetMapping("/apply-list")
    public BaseResponse<List<ApplyListResponse>> getApplyList(){
        return ResultUtils.success(friendService.getApplyList());
    }

    /**
     * 同意好友申请
     */
    @PostMapping("/apply-agree")
    public BaseResponse<String> agree(@RequestBody @Validated AgreeFriendRequest request) {
        friendService.agreeApply(request);
        return ResultUtils.success("成功");
    }
}
