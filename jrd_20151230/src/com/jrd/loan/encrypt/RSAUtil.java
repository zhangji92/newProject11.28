package com.jrd.loan.encrypt;

import com.jrd.framework.security.Base64Utils;
import com.jrd.framework.security.RSAUtils;
import com.jrd.loan.base.JrdConfig;

public final class RSAUtil {
    private final static String SECRET_KEY = JrdConfig.getRSAPublicKey();

    /*
     * 数据加密
     */
    public static String encryptBase64ByPublicKey(String data) {
        String str = null;

        try {
            str = RSAUtils.encryptBase64ByPublicKey(data.getBytes(), SECRET_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return str;
    }

    /*
     * 数据解密
     */
    public static String decryptByPublicKey(String data) {
        String str = null;

        try {
            str = new String(RSAUtils.decryptByPublicKey(Base64Utils.decode(data), SECRET_KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return str;
    }

    //服务器解密
    public static String decryptByPrivateKey(String data) {
        String str = null;

        try {
            str = new String(RSAUtils.decryptByPrivateKey(Base64Utils.decode(data), "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKDe9bY8AltTm1GhsH09TEW/CSGl2rbNbKHJPc8Mx1KvPZqNKrevnPCC4jof+0+W70DIcW6K6lg9OBZU8dsAbtJKX1PEQljXqxJKpmt99QhI7Nawmrrx++0bXoGvFMb99hbrm5FfSXye6r43F3Q957lSeq3cqKzTW/CCA2AlFol/AgMBAAECgYAligGyZaxzHT5oPR/PCcfLmrmJxYcVEzifRDONJkE49TWd5AUs2ej4YsJhp+bf+quX5W46ymXG83osE+RgI2i4fwSyFcISK5ljQHL5Q7OS8kPAYoLoAow8dvSPSk+CPshvhZj/SIV8bENKs8UrEbdkmWvSQE669I+C40vPyCp7MQJBAPhtAp1mNp1RyQTs6EUdOl896RFxgQTt1C7nluOB36cB2Av9/HJKEdGGZd91HqYW4eUxo+pL4+0Iw8Hx+TDMrqkCQQClxpMQqRQwWIkR7aUO8j4BJALtTAk9SMUN/mMn3PNjKaPdC7KHk2nnDwYD+KnhGCn9eVJTtDp9htNygFWyLdfnAkEAtFHb/gf9SH/e25Zimhg3EH7Nt/2dWsiOpWUwnv7cKksqWLoJDaQ5/s4BEAvhLUmaulQn2J8xWBJulU5gRE2t0QJAecHirF7zFBtC+acJD3Q+tEnF2JJ7SsbS7NdF7rZrEQ85apY31zofk3TGX05ZP5mm5aQhIpeCSkYnsbauuoLPSwJAdn4gD8nMUDsT8lzV9q9u2jw/JLnMCmL5pO7Q9Ss3DEipZTmkPS+Yh7VW/OcR3PglJk8sICNUHGBeRr19PaJP3g=="));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return str;
    }
}
