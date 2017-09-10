package org.echo.taotao.web.controller;

import org.echo.taotao.common.dto.TaotaoResult;
import org.echo.taotao.common.easyui.DataGridPageBean;
import org.echo.taotao.domain.ItemParameter;
import org.echo.taotao.service.ItemParameterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author Administrator on 8/18/2017
 * @version
 */
@Controller
@RequestMapping("/itemParameter")
public class ItemParameterController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ItemParameterService itemParameterService;


    /**
     * 检验对应的category id是否存在模板
     *
     * @param categoryId category id.
     * @return taotaoResult object.
     */
    @RequestMapping(value = "query/{categoryId}", method = RequestMethod.GET)
    @ResponseBody
    public TaotaoResult hasBindItemCategory(@PathVariable Long categoryId) {
        ItemParameter itemParameter = itemParameterService.getByCategoryId(categoryId);
        if (itemParameter == null) {
            logger.info("商品参数" + categoryId + "，模板不存在，可以新建模板");
            return TaotaoResult.bad("模板不存在，可以新建模板");
        } else {
            logger.info("商品参数" + categoryId + "，模板已存在，无需新建模板。" +
                    "若是在添加商品界面，则显示商品模板供填写商品参数信息。");
            return TaotaoResult.ok(itemParameter.getParamData());
        }
    }


    /**
     * saveOrUpdate/edit item parameter template.
     *
     * @param categoryId category id.
     * @param paramData  item parameter data.
     * @return taotaoResult object.
     */
    @RequestMapping(value = "save/{cid}", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult save(@PathVariable("cid") Long categoryId, @RequestParam String paramData) {
        boolean success = itemParameterService.saveOrUpdate(categoryId, paramData);
        if (success) {
            logger.info("为商品参数" + categoryId + "添加模板成功:" + paramData);
            return TaotaoResult.ok("添加模板成功");
        } else {
            logger.info("为商品参数" + categoryId + "添加模板失败。" + paramData);
            return TaotaoResult.bad("添加模板失败");
        }
    }


    /**
     * Get item categories by page.
     *
     * @param page page number.
     * @param rows page rows.
     * @return page bean.
     */
    @RequestMapping(value = {"", "list"}, method = RequestMethod.GET)
    @ResponseBody
    public DataGridPageBean getItemCategoryList(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                @RequestParam(value = "rows", defaultValue = "10") Integer rows) {
        return (DataGridPageBean) itemParameterService.getAllByPage(page, rows);
    }
}
