package com.zhaobh.test.springboottesttool.mapper;

import com.zhaobh.test.springboottesttool.entity.Agent;
import com.zhaobh.test.springboottesttool.vo.AgentVo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AgentMapper {
    @Insert("insert into t_agent " +
            "(name, entryTime, masterId, isNewS, addWagesRate, partId, partName, groupId, groupName, isPartLeader, isGroupLeader) " +
            "values (#{vo.name}, #{vo.entryTime}, #{vo.masterId}, #{vo.isNewS}, #{vo.addWagesRate}, " +
            "#{vo.partId}, #{vo.partName}, #{vo.GroupId}, #{vo.GroupName}, #{vo.isPartLeader}, #{vo.isGroupLeader})")
    public int addAgent(@Param("vo")AgentVo agentVo);

    @Delete("delete from t_agent where id = #{agentId}")
    public int removeAgent(Integer agentId);

    @Select("select * from t_agent where id = #{agentId}")
    public Agent getAgent(Integer agentId);

    @Update("update t_agent set name=#{vo.name}, entryTime=#{vo.entryTime}, isNewS=#{vo.isNewS}, addWagesRate=#{vo.addWagesRate}, " +
            "groupId=#{vo.groupId}, groupName=#{vo.groupName}, partId=#{vo.partId}, partName=#{vo.partName}, isGroupLeader=#{vo.isGroupLeader}, isPartLeader=#{vo.isPartLeader} " +
            "where id = #{vo.id}")
    public int editAgent(@Param("vo") AgentVo agentVo);

}
