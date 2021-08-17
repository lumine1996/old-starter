package com.cgm.starter.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.InputStream;

/**
 * @author 提辖鲁
 * @author cgm
 */
@Slf4j
public class LibLoader {
    private LibLoader() {
    }

    public static void loadLib(String libSourcePath) {
        String folderName = System.getProperty("java.io.tmpdir") + "/lib/";
        File folder = new File(folderName);
        assert folder.mkdirs();
        File libFile = new File(folder, libSourcePath);
        try {
            InputStream in = LibLoader.class.getResourceAsStream(libSourcePath);
            FileUtils.copyInputStreamToFile(in, libFile);
            in.close();
            System.load(libFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Failed to load required lib...\nShutting down...");
            System.exit(1);
        }
    }
}
