package org.echo.taotao.common.util;

import org.joda.time.DateTime;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static org.junit.Assert.assertTrue;

/**
 * @author Administrator
 * @date 8/27/2017
 */
public class FtpUtilTest {


    @Test
    public void uploadFile() throws Exception {
        String filePath = "F:\\MobilePictures\\1\\IMG_0038.JPG";
        try {
            FileInputStream in = new FileInputStream(new File(filePath));
            boolean flag = FtpUtils.uploadFile("test.jpg", new DateTime().toString("yyyy/MM/dd"), in);
            assertTrue(flag);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void downloadFile() throws Exception {

    }

}