package org.echo.taotao.service.impl;

import org.echo.taotao.dao.ItemCategoryMapper;
import org.echo.taotao.domain.ItemCategory;
import org.echo.taotao.domain.ItemCategoryExample;
import org.echo.taotao.service.ItemCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 8/15/2017.
 */
@Service("itemCategoryService")
public class ItemCategoryServiceImpl implements ItemCategoryService {

    @Autowired
    private ItemCategoryMapper itemCategoryMapper;

    @Override
    public List<ItemCategory> getAllByParentId(Long parentId) {
        ItemCategoryExample example = new ItemCategoryExample();
        ItemCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<ItemCategory> itemCategories = itemCategoryMapper.selectByExample(example);
        return itemCategories;
    }
}
