package org.echo.taotao.dao;

import org.apache.ibatis.annotations.Param;
import org.echo.taotao.domain.ItemDescription;
import org.echo.taotao.domain.ItemDescriptionExample;

import java.util.List;

public interface ItemDescriptionMapper {
    int countByExample(ItemDescriptionExample example);

    int deleteByExample(ItemDescriptionExample example);

    int deleteByPrimaryKey(Long itemId);

    int insert(ItemDescription record);

    int insertSelective(ItemDescription record);

    List<ItemDescription> selectByExampleWithBLOBs(ItemDescriptionExample example);

    List<ItemDescription> selectByExample(ItemDescriptionExample example);

    ItemDescription selectByPrimaryKey(Long itemId);

    int updateByExampleSelective(@Param("record") ItemDescription record, @Param("example") ItemDescriptionExample example);

    int updateByExampleWithBLOBs(@Param("record") ItemDescription record, @Param("example") ItemDescriptionExample example);

    int updateByExample(@Param("record") ItemDescription record, @Param("example") ItemDescriptionExample example);

    int updateByPrimaryKeySelective(ItemDescription record);

    int updateByPrimaryKeyWithBLOBs(ItemDescription record);

    int updateByPrimaryKey(ItemDescription record);
}