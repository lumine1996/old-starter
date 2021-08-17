package com.cgm.starter.util;

import com.cgm.starter.base.Constant;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * @author cgm
 */
@Slf4j
public class GetSeqJni {
    private GetSeqJni() {
    }

    public static native String getSeq();

    static {
        if (!isWindows()) {
            LibLoader.loadLib("/jni/libGetSeqJni.so");
        }
    }

    public static void checkAllowRun() {
        // 允许在Windows下运行, 以方便调试
        if (isWindows()) {
            return;
        }

        log.info("Authenticating...");
        List<String> whiteList = Arrays.asList("E3D77165E3C19DC2", "Modify this after obtaining other secret");
        String secret = getSeq();
        if (!whiteList.contains(secret)) {
            log.error("Unauthorized, Shutting down...");
            System.exit(1);
        }
        log.info("Secret \"{}\" Authorized.\nStarting Application...", secret);
    }

    private static boolean isWindows() {
        return System.getProperty(Constant.SYS_PROPERTY_OS_NAME).startsWith(Constant.OS_NAME_PREFIX_WINDOWS);
    }
}
