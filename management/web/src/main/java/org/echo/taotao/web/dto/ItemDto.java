package org.echo.taotao.web.dto;

import org.echo.taotao.domain.Item;

/**
 * Created by Administrator on 8/15/2017.
 */
public class ItemDto extends Item {
    private String desc;
    private String itemParams;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getItemParams() {
        return itemParams;
    }

    public void setItemParams(String itemParams) {
        this.itemParams = itemParams;
    }
}
