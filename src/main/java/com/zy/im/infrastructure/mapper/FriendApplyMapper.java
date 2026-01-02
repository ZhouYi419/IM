package com.zy.im.infrastructure.mapper;

import com.zy.im.domain.FriendApply;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FriendApplyMapper {
    /**
     * 判断当前用户是否对目标用户存在待处理的好友申请
     * @param fromUuid 申请发起人
     * @param toUuid 申请接收人
     * @return 1 如果存在，0 如果不存在
     */
    @Select("SELECT COUNT(1) FROM friend_apply " +
            "WHERE from_uuid = #{fromUuid} AND to_uuid = #{toUuid} AND status = 0")
    Integer existsPending(@Param("fromUuid") String fromUuid,
                      @Param("toUuid") String toUuid);

    /**
     * 插入好友申请记录
     * @param friendApply 好友申请实体
     * @return 影响行数
     */
    @Insert("INSERT INTO friend_apply " +
            "(from_uuid, to_uuid,status)" +
            "VALUES " +
            "(#{fromUuid}, #{toUuid},#{status})")
    int insert(FriendApply friendApply);
}
