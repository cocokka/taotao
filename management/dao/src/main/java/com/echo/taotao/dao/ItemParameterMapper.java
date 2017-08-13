package com.echo.taotao.dao;

import com.echo.taotao.domain.ItemParameter;
import com.echo.taotao.domain.ItemParameterExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemParameterMapper {
    int countByExample(ItemParameterExample example);

    int deleteByExample(ItemParameterExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ItemParameter record);

    int insertSelective(ItemParameter record);

    List<ItemParameter> selectByExampleWithBLOBs(ItemParameterExample example);

    List<ItemParameter> selectByExample(ItemParameterExample example);

    ItemParameter selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ItemParameter record, @Param("example") ItemParameterExample example);

    int updateByExampleWithBLOBs(@Param("record") ItemParameter record, @Param("example") ItemParameterExample example);

    int updateByExample(@Param("record") ItemParameter record, @Param("example") ItemParameterExample example);

    int updateByPrimaryKeySelective(ItemParameter record);

    int updateByPrimaryKeyWithBLOBs(ItemParameter record);

    int updateByPrimaryKey(ItemParameter record);
}