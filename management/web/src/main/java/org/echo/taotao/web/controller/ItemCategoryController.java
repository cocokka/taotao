package org.echo.taotao.web.controller;

import com.google.common.collect.Lists;
import org.echo.taotao.common.easyui.TreeNodeBean;
import org.echo.taotao.domain.ItemCategory;
import org.echo.taotao.service.ItemCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by Administrator on 8/15/2017.
 */
@Controller
@RequestMapping("/item/category")
public class ItemCategoryController {

    @Autowired
    private ItemCategoryService itemCategoryService;


    @RequestMapping("list")
    @ResponseBody
    public List<TreeNodeBean> getList(@RequestParam(value = "id", defaultValue = "0") Long parentId) {
        List<TreeNodeBean> list = Lists.newArrayList();
        List<ItemCategory> categoryList = itemCategoryService.getAllByParentId(parentId);
        categoryList.stream()
                .forEach(i -> {
                    TreeNodeBean treeNodeBean = new TreeNodeBean();
                    treeNodeBean.setId(i.getId());
                    treeNodeBean.setText(i.getName());
                    treeNodeBean.setState(i.getIsParent() ? "closed" : "open");
                    list.add(treeNodeBean);
                });

        return list;
    }
}
