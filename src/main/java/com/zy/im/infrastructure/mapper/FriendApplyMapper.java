package com.zy.im.infrastructure.mapper;

import com.zy.im.api.dto.response.ApplyListResponse;
import com.zy.im.domain.FriendApply;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface FriendApplyMapper {
    /**
     * 判断当前用户是否对目标用户存在待处理的好友申请
     */
    @Select("SELECT COUNT(1) FROM friend_apply " +
            "WHERE from_uuid = #{fromUuid} AND to_uuid = #{toUuid} AND status = 0")
    Integer existsPending(@Param("fromUuid") String fromUuid,
                      @Param("toUuid") String toUuid);

    /**
     * 插入好友申请记录
     */
    @Insert("INSERT INTO friend_apply " +
            "(from_uuid, to_uuid,status,reason)" +
            "VALUES " +
            "(#{fromUuid}, #{toUuid},#{status},#{reason})")
    int insert(FriendApply friendApply);


    /**
     * 查询收到的好友申请列表
     */
    List<ApplyListResponse> selectApplyList(@Param("toUuid") String toUuid);


    /**
     * 查询好友申请
     */
    FriendApply selectApply(@Param("fromUuid") String fromUuid,
                                @Param("toUuid") String toUuid);

    /**
     * 更新申请状态
     */
    int updateStatus(@Param("fromUuid") String fromUuid,
                     @Param("toUuid") String toUuid,
                     @Param("status") Integer status);
}
