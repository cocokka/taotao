package org.echo.taotao.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @uthor Administrator on 8/20/2017
 */
@Controller
@RequestMapping("/portal")
public class ViewController {

    @RequestMapping("{page}")
    public String toPage(@PathVariable String page) {
        return page;
    }

}
