package com.zy.im.infrastructure.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FriendRelationMapper {
    /**
     * 判断两人是否是好友
     *
     * @param fromUuid 当前用户
     * @param toUuid 好友用户
     * @return 1 表示是好友，0 或 null 表示不是
     */
    @Select("""
        SELECT 1
        FROM friend_relation
        WHERE uuid = #{fromUuid}
          AND friend_uuid = #{toUuid}
          AND status = 0
        LIMIT 1
    """)
    Integer isFriend(@Param("fromUuid") String fromUuid,
                     @Param("toUuid") String toUuid);

    /**
     * 插入好友数据
     */
    int insert(@Param("uuid") String uuid,
               @Param("friendUuid") String friendUuid,
               @Param("status") Integer status);
}
