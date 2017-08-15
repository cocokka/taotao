package org.echo.taotao.web.controller;

import org.echo.taotao.common.dto.TaotaoResult;
import org.echo.taotao.common.easyui.DataGridPageBean;
import org.echo.taotao.domain.Item;
import org.echo.taotao.service.ItemService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Administrator on 8/13/2017.
 */

@Controller
@RequestMapping("/item")
public class ItemController {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ItemService itemService;

    @RequestMapping(value = "home", method = RequestMethod.GET)
    @ResponseBody
    public String item() {
        return "this is item page.";
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @ResponseBody
    public Item get(@PathVariable Long id) {
        Item item = itemService.getById(id);
        return item;
    }

    @RequestMapping(value = {"", "list"}, method = RequestMethod.GET)
    @ResponseBody
    public DataGridPageBean getItemList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                        @RequestParam(value = "rows", defaultValue = "10") Integer rows) {
        DataGridPageBean result = (DataGridPageBean) itemService.getAllByPage(page, rows);
        return result;
    }


    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult save(Item item, String desc, String itemParams) {
        try {
            TaotaoResult result = itemService.save(item, desc, itemParams);
            return result;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

}
