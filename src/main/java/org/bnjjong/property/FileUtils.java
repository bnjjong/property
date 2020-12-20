/*
 * COPYRIGHT (c) ADOP 2020
 * This software is the proprietary of ADOP
 *
 * @author <a href=“mailto:jordan@adop.cc“>jordan</a>
 * @since 2020/09/07
 */
package org.bnjjong.property;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;

/**
 * create on 2020/09/07.
 * <p> 클래스 설명 </p>
 * <p> {@link } and {@link }관련 클래스 </p>
 *
 * @author jordan
 * @version 1.0
 * @see
 * @since 지원하는 자바버전 (ex : 5+ 5이상)
 */
public class FileUtils {
    public static boolean isExistFile(String path) throws URISyntaxException {
        try {
            return getFileFromResource(path).exists();
        } catch (Exception e) {
//            log.debug("does not exist path >> {}", path);
            return false;
        }
    }

    public static File getFileFromResource(String path) throws URISyntaxException {
        URL res = FileUtils.class.getClassLoader().getResource(path);
        if (res == null) {
            throw new IllegalArgumentException("path is not valid!>>>" + path);
        }
        return Paths.get(res.toURI()).toFile();
    }
}
