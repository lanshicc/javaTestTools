package com.zhaobh.test.springboottesttool.mapper;

import com.zhaobh.test.springboottesttool.entity.Group;
import com.zhaobh.test.springboottesttool.vo.GroupVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GroupMapper {

    @Insert("insert into t_group (name, leaderId, partId) values (#{vo.name}, #{vo.leaderId}, #{vo.partId})")
    public int addGroup(@Param("vo") GroupVo groupVo);

    @Select("select * from t_group where partId = #{partId}")
    public List<Group> getGroupsByPartId(Integer partId);
}
