package org.echo.taotao.service;

import java.io.InputStream;
import java.util.Map;

/**
 * Created by Administrator on 8/15/2017.
 */
public interface PictureService {

    /**
     * upload pictures.
     *
     * @param originalName
     * @param inputStream
     * @return
     * @throws Exception
     */
    Map<String, String> upload(String originalName, InputStream inputStream) throws Exception;

}
