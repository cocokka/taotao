package org.echo.taotao.service;

/**
 * @Author Administrator
 * @Date 8/15/2017
 * @Description
 * @Version
 */
public interface ItemParameterItemService {

    /**
     * Save item parameter item.
     *
     * @param itemId item id.
     * @param itemParameter item parameter, save as JSON format.
     * @return true:save success; false:save failed.
     */
    boolean save(Long itemId, String itemParameter);

}
