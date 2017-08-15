package org.echo.taotao.web.controller;

import com.google.common.collect.Maps;
import org.echo.taotao.common.util.JsonUtils;
import org.echo.taotao.service.PictureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Map;

/**
 * Created by Administrator on 8/15/2017.
 */
@Controller
@RequestMapping("/picture")
public class PictureController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String upload_temp_dir = "upload/images/";

    @Autowired
    private PictureService pictureService;


    /**
     * @param uploadFile 该名称是在js组建的指定的(filePostName  : "uploadFile",)
     * @param request
     * @return
     */
    @RequestMapping("upload")
    @ResponseBody
    public String pictureUpload(@RequestParam("uploadFile") MultipartFile uploadFile, HttpServletRequest request) {
        Map<String, Object> result = Maps.newHashMap();
        /*try {
            result = pictureService.upload(uploadFile.getOriginalFilename());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }*/

        // 判断文件是否为空
        if (!uploadFile.isEmpty()) {
            try {
                Map<String, String> resultMap = pictureService.upload(uploadFile.getOriginalFilename());
                String filename = resultMap.get("filename");
                // 文件保存路径

                String filePath = request.getSession().getServletContext().getRealPath("/") + upload_temp_dir
                        + filename;
                logger.info("the file path is " + filePath);
                // 转存文件
                uploadFile.transferTo(new File(filePath));

                result.put("error", 0); // success is 0; failure is 1.
                result.put("url", upload_temp_dir + filename);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                result.put("error", 1);
                result.put("message", "文件上传发生异常");
            }
        } else {
            logger.warn("未检测到文件资源");
            result.put("error", 1);
            result.put("message", "未检测到文件资源");
        }
        //为了保证功能的兼容性，需要把Result转换成json格式的字符串。
        String json = JsonUtils.objectToJson(result);
        return json;
    }

}
