package com.zy.im.application.service;

import com.zy.im.api.dto.request.ApplyFriendRequest;
import com.zy.im.api.dto.request.ProcessFriendRequest;
import com.zy.im.api.dto.response.ApplyListResponse;
import com.zy.im.common.exception.BusinessException;
import com.zy.im.common.exception.ErrorCode;
import com.zy.im.common.security.UserContext;
import com.zy.im.domain.FriendApply;
import com.zy.im.infrastructure.enums.FriendApplyStatus;
import com.zy.im.infrastructure.enums.FriendStatus;
import com.zy.im.infrastructure.mapper.FriendApplyMapper;
import com.zy.im.infrastructure.mapper.FriendRelationMapper;
import com.zy.im.infrastructure.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final UserMapper userMapper;
    private final FriendRelationMapper FriendRelationMapper;
    private final FriendApplyMapper friendApplyMapper;

    /**
     * 发送好友申请
     */
    @Transactional(rollbackFor = Exception.class)
    public void apply(ApplyFriendRequest request) {
        // 1.获取请求参数
        String uuid = UserContext.getUserId();
        String applyUuid = request.getApplyUuid();
        String reason = request.getReason();

        if (uuid == null){
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "用户异常");
        }

        // 1. 不能加自己
        if (uuid.equals(applyUuid)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不能添加自己为好友");
        }

        // 2. 对方是否存在
        Integer userExist = userMapper.existsByUuid(applyUuid);
        if (userExist == null || userExist == 0) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }

        // 3. 是否已经是好友
        Integer isFriend = FriendRelationMapper.isFriend(uuid, applyUuid);
        if (isFriend != null && isFriend == 1) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "已经是好友");
        }

        // 4. 是否已申请过（待处理）
        Integer isPending = friendApplyMapper.existsPending(uuid, applyUuid);
        if (isPending != null && isPending == 1) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "已发送过好友申请");
        }

        // 5. 保存好友申请
        FriendApply apply = new FriendApply();
        apply.setFromUuid(uuid);
        apply.setToUuid(applyUuid);
        apply.setReason(reason);
        apply.setStatus(FriendApplyStatus.PENDING.getCode());

        int result = friendApplyMapper.insert(apply);
        if (result != 1){
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "添加失败");
        }
    }

    /**
     * 获取好友申请列表
     */
    public List<ApplyListResponse> getApplyList() {
        String uuid = UserContext.getUserId();
        return friendApplyMapper.selectApplyList(uuid);
    }

    /**
     * 同意好友申请
     */
    @Transactional(rollbackFor = Exception.class)
    public void agreeApply(ProcessFriendRequest request) {
        // 1.获取请求参数
        String uuid = UserContext.getUserId();
        String fromUuid = request.getFromUuid();

        // 1. 查询申请记录
        FriendApply apply = friendApplyMapper.selectApply(fromUuid, uuid);
        if (apply == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "好友申请不存在");
        }

        // 2. 状态校验
        if (!(FriendApplyStatus.PENDING.getCode() == apply.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该申请已处理");
        }

        // 3. 插入好友关系
        FriendRelationMapper.insert(fromUuid, uuid, FriendStatus.NORMAL.getCode());
        FriendRelationMapper.insert(uuid, fromUuid,FriendStatus.NORMAL.getCode());

        // 4. 更新申请状态
        friendApplyMapper.updateStatus(
                fromUuid,
                uuid,
                FriendApplyStatus.ACCEPTED.getCode()
        );
    }

    /**
     * 拒绝好友申请
     */
    @Transactional(rollbackFor = Exception.class)
    public void rejectApply(ProcessFriendRequest request) {
        // 1.获取请求参数
        String uuid = UserContext.getUserId();
        String fromUuid = request.getFromUuid();

        // 1. 查询申请记录
        FriendApply apply = friendApplyMapper.selectApply(fromUuid, uuid);
        if (apply == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "好友申请不存在");
        }

        // 2. 状态校验
        if (!(FriendApplyStatus.PENDING.getCode() == apply.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该申请已处理");
        }

        // 3. 更新申请状态
        friendApplyMapper.updateStatus(
                fromUuid,
                uuid,
                FriendApplyStatus.REJECTED.getCode()
        );
    }
}