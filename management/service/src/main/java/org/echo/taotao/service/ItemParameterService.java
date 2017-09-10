package org.echo.taotao.service;

import org.echo.taotao.common.dto.DefaultPageBean;
import org.echo.taotao.domain.ItemParameter;

/**
 * @author Administrator on 8/17/2017
 * @version
 */
public interface ItemParameterService {

    /**
     * save/edit item parameter.
     *
     * @param itemCategoryId item category id.
     * @param parameter      item parameter, saveOrUpdate as json format.
     * @return true: save or update success, false: save or update failed.
     */
    boolean saveOrUpdate(Long itemCategoryId, String parameter);

    /**
     * Get item parameter by category id.
     *
     * @param itemCategoryId item category id.
     * @return item parameter if already exists, otherwise return null.
     */
    ItemParameter getByCategoryId(Long itemCategoryId);

    /**
     * Paging to get all item categories.
     *
     * @param offset page number.
     * @param limit  page size.
     * @return item parameters with page.
     */
    DefaultPageBean<ItemParameter> getAllByPage(int offset, int limit);
}
