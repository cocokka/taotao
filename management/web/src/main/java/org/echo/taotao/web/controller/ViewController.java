package org.echo.taotao.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * turn to page ...
 * Created by Administrator on 8/14/2017.
 */
@Controller
@RequestMapping("/views")
public class ViewController {

    @RequestMapping(value = {"", "index"}, method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(value = "{page}", method = RequestMethod.GET)
    public String toPage(@PathVariable("page") String page) {
        return page;
    }


    @RequestMapping(value = "{item}/{page}", method = RequestMethod.GET)
    public String toPage(@PathVariable("item") String item, @PathVariable("page") String page) {
        return item + "/" + page;
    }


}
