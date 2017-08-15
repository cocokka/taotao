package org.echo.taotao.service;

/**
 * Created by Administrator on 8/15/2017.
 */
public interface ItemDescriptionService {

    /**
     * Save item description.
     *
     * @param itemId
     * @param description
     * @return
     */
    boolean save(Long itemId, String description);
}
