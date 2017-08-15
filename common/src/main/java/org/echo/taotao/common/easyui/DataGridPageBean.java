package org.echo.taotao.common.easyui;

import org.echo.taotao.common.dto.DefaultPageBean;

import java.util.List;

/**
 * Created by Administrator on 8/14/2017.
 */
public class DataGridPageBean extends DefaultPageBean {

    public DataGridPageBean() {
    }

    public DataGridPageBean(long total, List rows) {
        super(total, rows);
    }
}
