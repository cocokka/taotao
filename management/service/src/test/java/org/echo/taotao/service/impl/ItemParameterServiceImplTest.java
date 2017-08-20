package org.echo.taotao.service.impl;

import org.echo.taotao.domain.ItemParameter;
import org.echo.taotao.service.ItemParameterService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * @Author Administrator
 * @Date 8/20/2017
 * @Description
 * @Version
 */
public class ItemParameterServiceImplTest extends BaseServiceImplTest {

    @Autowired
    private ItemParameterService itemParameterService;

    @Test
    public void saveOrUpdate() throws Exception {

    }

    @Test
    public void getByCategoryId() throws Exception {
        Long cidNotExists = 100000L;
        ItemParameter itemParameter = itemParameterService.getByCategoryId(cidNotExists);
        assertNull(itemParameter);

        Long cidExists = 560L;
        itemParameter = itemParameterService.getByCategoryId(cidExists);
        assertNotNull(itemParameter);
        assertEquals(itemParameter.getId(), Long.valueOf(2));
        assertEquals(itemParameter.getItemCatId(), cidExists);


    }

}