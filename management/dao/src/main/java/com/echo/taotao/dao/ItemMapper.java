package com.echo.taotao.dao;

import com.echo.taotao.domain.Item;
import com.echo.taotao.domain.ItemExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItemMapper {
    int countByExample(ItemExample example);

    int deleteByExample(ItemExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Item record);

    int insertSelective(Item record);

    List<Item> selectByExample(ItemExample example);

    Item selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Item record, @Param("example") ItemExample example);

    int updateByExample(@Param("record") Item record, @Param("example") ItemExample example);

    int updateByPrimaryKeySelective(Item record);

    int updateByPrimaryKey(Item record);
}