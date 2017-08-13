package org.echo.taotao.web.controller;

import org.echo.taotao.domain.Item;
import org.echo.taotao.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 8/13/2017.
 */

@Controller
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @RequestMapping(value = "home", method = RequestMethod.GET)
    @ResponseBody
    public String item() {
        return "this is item page.";
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public String get(@PathVariable Long id, Model model) {
        Item item = itemService.getById(id);
        model.addAttribute("item", item);
        return "item/detail";
    }

}
