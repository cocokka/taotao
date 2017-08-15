package org.echo.taotao.service.impl;

import com.google.common.collect.Maps;
import org.echo.taotao.common.util.IDUtils;
import org.echo.taotao.service.PictureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Administrator on 8/15/2017.
 */
@Service("pictureService")
public class PictureServiceImpl implements PictureService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public Map<String, String> upload(String originalName) throws Exception {
        Map<String, String> resultMap = Maps.newHashMap();
        String namePrefix = IDUtils.genImageName();
        String filename = namePrefix + originalName.substring(originalName.lastIndexOf("."));
        // TODO 现在直接存储在 /uplaod/images文件夹下，后续通过FTP传输到单独的图片服务器上。
        /*String directory = new DateTime().toString("yyyy/MM/dd");
        String path = "/upload/images";
        resultMap.put("error", "0"); // success is 0; failure is 1.
        resultMap.put("url", path + File.separator + directory + File.separator + name);*/
        resultMap.put("filename", filename);
        return resultMap;
    }
}
