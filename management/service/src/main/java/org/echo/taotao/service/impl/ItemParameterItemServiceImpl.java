package org.echo.taotao.service.impl;

import org.echo.taotao.dao.ItemParameterItemMapper;
import org.echo.taotao.domain.ItemParameterItem;
import org.echo.taotao.service.ItemParameterItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by Administrator on 8/15/2017.
 */
@Service("itemParameterItemService")
public class ItemParameterItemServiceImpl implements ItemParameterItemService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ItemParameterItemMapper itemParameterItemMapper;

    @Transactional
    @Override
    public boolean save(Long itemId, String itemParameter) {
        ItemParameterItem itemParamItem = new ItemParameterItem();
        itemParamItem.setItemId(itemId);
        itemParamItem.setParamData(itemParameter);
        itemParamItem.setCreated(new Date());
        itemParamItem.setUpdated(new Date());
        int count = itemParameterItemMapper.insert(itemParamItem);
        if (count == 1) {
            return true;
        } else {
            logger.error("Add itemParameterItem failed, effect row is " + count);
            return false;
        }
    }
}
