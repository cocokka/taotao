package org.echo.taotao.service;

import org.echo.taotao.domain.ItemCategory;

import java.util.List;

/**
 * Created by Administrator on 8/15/2017.
 */
public interface ItemCategoryService {

    /**
     * Recursive to get item category list,
     * and format it to tree node.
     *
     * @param parentId
     * @return
     */
    List<ItemCategory> getAllByParentId(Long parentId);
}
