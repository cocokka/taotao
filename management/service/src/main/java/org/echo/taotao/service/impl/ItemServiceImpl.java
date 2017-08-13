package org.echo.taotao.service.impl;

import org.echo.taotao.dao.ItemMapper;
import org.echo.taotao.domain.Item;
import org.echo.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 8/13/2017.
 */
@Service("itemService")
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemMapper itemMapper;

    @Override
    public Item getById(Long id) {
        return itemMapper.selectByPrimaryKey(id);
    }
}
