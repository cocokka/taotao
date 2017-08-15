package org.echo.taotao.service.impl;

import org.echo.taotao.domain.ItemCategory;
import org.echo.taotao.service.ItemCategoryService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Administrator on 8/15/2017.
 */
public class ItemCategoryServiceImplTest extends BaseServiceImplTest {

    @Autowired
    private ItemCategoryService itemCategoryService;

    @Test
    public void testGetAllByParentId() throws Exception {
        Long parentId = 0L;
        List<ItemCategory> categoryList = itemCategoryService.getAllByParentId(parentId);
        assertNotNull(categoryList);
        assertEquals(19, categoryList.size());
    }

}