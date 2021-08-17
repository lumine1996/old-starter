package com.cgm.starter.util;

import com.cgm.starter.base.ErrorCode;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 压缩文件工具类
 *
 * @author cgm
 */
public class ZipUtils {

    private ZipUtils() {

    }

    /**
     * 以文本形式读取tar包内的指定文件
     *
     * @param file     文件
     * @param pathname 文件名及路径
     * @return 文本
     */
    public static String readTarFile(MultipartFile file, String pathname) throws IOException {
        String originalName = file.getOriginalFilename();
        Assert.hasText(originalName, ErrorCode.USER_INVALID_INPUT);
        assert originalName != null;
        Assert.isTrue(originalName.endsWith(".tar"), ErrorCode.USER_INCORRECT_FORMAT);

        TarArchiveInputStream archiveInputStream = new TarArchiveInputStream(file.getInputStream());
        TarArchiveEntry entry;
        while ((entry = archiveInputStream.getNextTarEntry()) != null) {
            if (entry.getName().equals(pathname)) {
                break;
            }
        }
        return new String(getContent(archiveInputStream));
    }

    /**
     * 输入流转二进制数组
     *
     * @param is 输入流
     * @return 二进制数组
     */
    private static byte[] getContent(InputStream is) {
        ByteArrayOutputStream byteArrayOs = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            while (true) {
                int len = is.read(buffer);
                if (len == -1) {
                    break;
                }
                byteArrayOs.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return byteArrayOs.toByteArray();
    }
}
