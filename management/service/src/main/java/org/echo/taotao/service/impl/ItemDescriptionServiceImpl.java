package org.echo.taotao.service.impl;

import org.echo.taotao.dao.ItemDescriptionMapper;
import org.echo.taotao.domain.ItemDescription;
import org.echo.taotao.service.ItemDescriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by Administrator on 8/15/2017.
 */
@Service("itemDescriptionService")
public class ItemDescriptionServiceImpl implements ItemDescriptionService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ItemDescriptionMapper itemDescriptionMapper;

    @Override
    public boolean save(Long itemId, String description) {
        ItemDescription itemDesc = new ItemDescription();
        itemDesc.setItemId(itemId);
        itemDesc.setItemDesc(description);
        itemDesc.setCreated(new Date());
        itemDesc.setUpdated(new Date());
        int count = itemDescriptionMapper.insert(itemDesc);
        if (count == 1) {
            return true;
        } else {
            logger.error("Add itemDescription failed, effect row is " + count);
            return false;
        }
    }
}
