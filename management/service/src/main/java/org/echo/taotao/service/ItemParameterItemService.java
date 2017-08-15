package org.echo.taotao.service;

/**
 * Created by Administrator on 8/15/2017.
 */
public interface ItemParameterItemService {

    /**
     * Save item parameter item.
     *
     * @param itemId
     * @param itemParameter
     * @return
     */
    boolean save(Long itemId, String itemParameter);

}
