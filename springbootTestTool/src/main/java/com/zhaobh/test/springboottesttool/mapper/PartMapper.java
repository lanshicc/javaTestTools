package com.zhaobh.test.springboottesttool.mapper;

import com.zhaobh.test.springboottesttool.entity.Part;
import com.zhaobh.test.springboottesttool.vo.PartVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PartMapper {

    @Insert("insert into t_part " +
            "(name, leaderId) " +
            "values (#{vo.name}, #{vo.leaderId})")
    public int addPart(@Param("vo") PartVo partVo);

    @Select("select * from t_part")
    public List<Part> getParts();
}
