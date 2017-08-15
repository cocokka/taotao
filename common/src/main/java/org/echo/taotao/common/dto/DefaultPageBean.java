package org.echo.taotao.common.dto;

import java.util.List;

/**
 * Created by Administrator on 8/15/2017.
 */
public class DefaultPageBean<T> {

    protected long total;
    protected List<T> rows;

    public DefaultPageBean() {
    }

    public DefaultPageBean(long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
