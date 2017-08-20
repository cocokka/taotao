package org.echo.taotao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.echo.taotao.common.dto.DefaultPageBean;
import org.echo.taotao.common.easyui.DataGridPageBean;
import org.echo.taotao.dao.ItemParameterMapper;
import org.echo.taotao.domain.ItemParameter;
import org.echo.taotao.domain.ItemParameterExample;
import org.echo.taotao.service.ItemParameterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static org.echo.taotao.domain.ItemParameterExample.Criteria;

/**
 * @Author Administrator
 * @Date 8/17/2017
 * @Description
 * @Version
 */
@Service("itemParameterService")
public class ItemParameterServiceImpl implements ItemParameterService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ItemParameterMapper itemParameterMapper;

    @Override
    public boolean saveOrUpdate(Long itemCategoryId, String parameter) {
        ItemParameter itemParameter = this.getByCategoryId(itemCategoryId);
        int count;
        if (itemParameter == null) {
            itemParameter = new ItemParameter();
            itemParameter.setItemCatId(itemCategoryId);
            itemParameter.setParamData(parameter);
            itemParameter.setCreated(new Date());
            itemParameter.setUpdated(new Date());
            count = itemParameterMapper.insert(itemParameter);
        } else {
            itemParameter.setParamData(parameter);
            itemParameter.setUpdated(new Date());
            count = itemParameterMapper.updateByPrimaryKeyWithBLOBs(itemParameter);
        }
        if (count == 1) {
            return true;
        } else {
            logger.error("Add item parameter failed, effect row is " + count);
            return false;
        }
    }

    @Override
    public ItemParameter getByCategoryId(Long itemCategoryId) {
        ItemParameterExample itemParameterExample = new ItemParameterExample();
        Criteria criteria = itemParameterExample.createCriteria();
        criteria.andItemCatIdEqualTo(itemCategoryId);
        List<ItemParameter> itemParameters = itemParameterMapper.selectByExampleWithBLOBs(itemParameterExample);
        if (CollectionUtils.isNotEmpty(itemParameters)) {
            return itemParameters.get(0);
        }
        return null;
    }

    @Override
    public DefaultPageBean<ItemParameter> getAllByPage(int offset, int limit) {
        ItemParameterExample example = new ItemParameterExample();
        PageHelper.startPage(offset, limit);
        List<ItemParameter> rows = itemParameterMapper.selectByExampleWithBLOBs(example);
        PageInfo<ItemParameter> pageInfo = new PageInfo<>(rows);
        long total = pageInfo.getTotal();
        return (DefaultPageBean<ItemParameter>) new DataGridPageBean(total, rows);
    }
}
