package org.echo.taotao.service;

import org.echo.taotao.common.dto.DefaultPageBean;
import org.echo.taotao.common.dto.TaotaoResult;
import org.echo.taotao.domain.Item;

/**
 * Created by Administrator on 8/13/2017.
 */
public interface ItemService {

    /**
     * Get item by id.
     *
     * @param id
     * @return
     */
    Item getById(Long id);

    /**
     * Paging to get all items.
     *
     * @param offset page number.
     * @param limit  page size.
     * @return
     */
    DefaultPageBean<Item> getAllByPage(int offset, int limit);


    /**
     * Save a single item.
     *
     * @param item
     * @param itemDescription
     * @param itemParameter
     * @return
     */
    TaotaoResult save(Item item, String itemDescription, String itemParameter) throws Exception;

}
