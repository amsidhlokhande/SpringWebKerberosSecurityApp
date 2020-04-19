package com.amsidh.mvc;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.security.kerberos.test.MiniKdc;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Properties;

@Slf4j
public class MiniKDCApp {
    private static final String KRB_WORK_DIR = "kdc-work";

    public static void main(String... args) throws Exception {
        String[] kdcConf = {prepareWorkDir(),
                resolvePathOfMiniKdcConfig("minikdc-krb5.conf"),
                resolveKeytabFilePath("example.keytab"), "client/localhost", "HTTP/localhost"};
        Properties conf = MiniKdc.createConf();
        conf.setProperty(MiniKdc.DEBUG, "true");
        MiniKdc.main(kdcConf);
    }

    public static String prepareWorkDir() throws IOException {
        File directory = Paths.get(KRB_WORK_DIR).normalize().toFile();
        FileUtils.deleteQuietly(directory);
        FileUtils.forceMkdir(directory);
        return directory.toString();
    }

    public static String resolvePathOfMiniKdcConfig(String miniKdcConfigFileName) throws URISyntaxException {
        URL url = Thread.currentThread().getContextClassLoader().getResource("minikdc-krb5.conf");
        String configFilePath = Paths.get(url.toURI()).toString();
        System.out.println(configFilePath);
        return configFilePath;
    }

    public static String resolveKeytabFilePath(String keytabFileName) {
        return Paths.get(KRB_WORK_DIR).resolve(keytabFileName).toString();
    }
}
