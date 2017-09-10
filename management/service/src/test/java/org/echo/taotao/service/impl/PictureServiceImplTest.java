package org.echo.taotao.service.impl;

import org.echo.taotao.service.PictureService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Administrator on 9/6/2017.
 */
public class PictureServiceImplTest extends BaseServiceImplTest {

    @Autowired
    private PictureService pictureService;

    @Test
    public void upload() throws Exception {
        String filePath = "F:\\MobilePictures\\1\\IMG_0038.JPG";
        try {
            FileInputStream in = new FileInputStream(new File(filePath));
            Map<String, String> resultMap = pictureService.upload("IMG_0038.JPG", in);
            assertNotNull(resultMap);
            assertEquals(resultMap.get("error"), "0");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}