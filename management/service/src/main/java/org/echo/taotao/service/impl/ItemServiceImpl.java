package org.echo.taotao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.echo.taotao.common.dto.DefaultPageBean;
import org.echo.taotao.common.dto.TaotaoResult;
import org.echo.taotao.common.easyui.DataGridPageBean;
import org.echo.taotao.common.util.IDUtils;
import org.echo.taotao.dao.ItemMapper;
import org.echo.taotao.domain.Item;
import org.echo.taotao.domain.ItemExample;
import org.echo.taotao.service.ItemDescriptionService;
import org.echo.taotao.service.ItemParameterItemService;
import org.echo.taotao.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 8/13/2017.
 */
@Service("itemService")
public class ItemServiceImpl implements ItemService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ItemDescriptionService itemDescriptionService;

    @Autowired
    private ItemParameterItemService itemParameterItemService;

    @Override
    public Item getById(Long id) {
        return itemMapper.selectByPrimaryKey(id);
    }

    @Override
    public DefaultPageBean<Item> getAllByPage(int offset, int limit) {
        ItemExample itemExample = new ItemExample();
        PageHelper.startPage(offset, limit);
        List<Item> rows = itemMapper.selectByExample(itemExample);
        PageInfo<Item> pageInfo = new PageInfo<>(rows);
        long total = pageInfo.getTotal();
        DefaultPageBean<Item> result = new DataGridPageBean(total, rows);
        return result;
    }

    @Transactional
    @Override
    public TaotaoResult save(Item item, String itemDescription, String itemParameter) throws Exception {
        Long itemId = IDUtils.genItemId();
        item.setId(itemId);
        item.setStatus((byte) 1); // '商品状态，1-正常，2-下架，3-删除'
        item.setCreated(new Date());
        item.setUpdated(new Date());
        itemMapper.insert(item);

        TaotaoResult result = saveItemDescription(itemId, itemDescription);
        if (result.getStatus() != 200) {
            throw new RuntimeException(result.getMsg());
        }

        result = saveItemParameterItem(itemId, itemParameter);
        if (result.getStatus() != 200) {
            throw new RuntimeException(result.getMsg());
        }
        return TaotaoResult.ok();
    }

    /**
     * 添加规格参数
     *
     * @param itemId
     * @param itemParameter
     * @return
     */
    private TaotaoResult saveItemParameterItem(Long itemId, String itemParameter) {
        boolean flag = itemParameterItemService.save(itemId, itemParameter);
        if (flag) {
            return TaotaoResult.ok();
        } else {
            return TaotaoResult.build(100, "添加规格参数失败.");
        }
    }

    /**
     * 添加商品描述信息
     *
     * @param itemId
     * @param itemDescription
     * @return
     */
    private TaotaoResult saveItemDescription(Long itemId, String itemDescription) {
        boolean flag = itemDescriptionService.save(itemId, itemDescription);
        if (flag) {
            return TaotaoResult.ok();
        } else {
            return TaotaoResult.build(100, "添加商品描述信息失败.");
        }
    }
}
