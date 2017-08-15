package org.echo.taotao.service.impl;

import org.echo.taotao.common.dto.DefaultPageBean;
import org.echo.taotao.domain.Item;
import org.echo.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Administrator on 8/15/2017.
 */
public class ItemServiceImplTest extends BaseServiceImplTest {

    @Autowired
    private ItemService itemService;

    @org.junit.Test
    public void testGetById() throws Exception {
        Long id = 536563L;
        Item item = itemService.getById(id);
        assertNotNull(item);
        assertEquals(id, item.getId());
    }

    @org.junit.Test
    public void testGetAllByPage() throws Exception {
        int offset = 1;
        int limit = 10;
        DefaultPageBean<Item> pageBean = itemService.getAllByPage(offset, limit);
        assertNotNull(pageBean);
        assertNotNull(pageBean.getTotal());
        assertNotNull(pageBean.getRows());
        assertEquals(limit, pageBean.getRows().size());

    }

}