package com.amsidh.mvc.kerberos;

import org.springframework.security.kerberos.client.KerberosRestTemplate;

import java.nio.file.Paths;

public class KdcKerberosRestTemplate {

    private static String kerbLocation = ".\\kdc-work\\krb5.conf";
    private static String keyTabLocation = ".\\kdc-work\\example.keytab";
    private static String userPrincipal = "client/localhost";
    private static String serverRequestUrl = "http://localhost:8282/secured";

    static {
        String path = Paths.get(kerbLocation).normalize().toAbsolutePath().toString();
        System.setProperty("java.security.krb5.conf", path);
        System.setProperty("sun.security.krb5.debug", "true");
        // disable usage of local kerberos ticket cache
        System.setProperty("http.use.global.creds", "false");
    }

    public static void main(String... args) {
        KerberosRestTemplate kerberosRestTemplate = new KerberosRestTemplate(
                Paths.get(keyTabLocation).normalize().toAbsolutePath().toString(), userPrincipal);
        String response = kerberosRestTemplate.getForObject(serverRequestUrl, String.class);
        System.out.println("Response received from server :::" + response);

    }
}
