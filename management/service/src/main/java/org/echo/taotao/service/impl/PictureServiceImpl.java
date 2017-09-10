package org.echo.taotao.service.impl;

import com.google.common.collect.Maps;
import org.apache.commons.configuration.Configuration;
import org.echo.taotao.common.util.FtpUtils;
import org.echo.taotao.common.util.IDUtils;
import org.echo.taotao.service.PictureService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Map;

/**
 * Created by Administrator on 8/15/2017.
 */
@Service("pictureService")
public class PictureServiceImpl implements PictureService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Configuration configuration;


    @Override
    public Map<String, String> upload(String originalName, InputStream inputStream) throws Exception {
        Map<String, String> resultMap = Maps.newHashMap();
        String namePrefix = IDUtils.genImageName();
        String filename = namePrefix + originalName.substring(originalName.lastIndexOf("."));
        String filePath = new DateTime().toString("yyyy/MM/dd");
        String rootPath = configuration.getString("resource.images.server.url");
        boolean flag = FtpUtils.uploadFile(filename, filePath, inputStream);
        if (flag) {
            resultMap.put("error", "0"); // success is 0; failure is 1.
            resultMap.put("url", String.format("%s/%s/%s", rootPath, filePath, filename));
        } else {
            resultMap.put("error", "1"); // success is 0; failure is 1.
            resultMap.put("url", String.format("文件上传失败：%s", originalName));
        }

        return resultMap;
    }
}
