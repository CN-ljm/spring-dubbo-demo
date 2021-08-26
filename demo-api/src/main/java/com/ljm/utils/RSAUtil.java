package com.ljm.utils;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.List;

/**
 * @author Created by liangjiaming on 2021/8/26
 * @title
 * @Desc
 */
public class RSAUtil {

    private static Logger log = LoggerFactory.getLogger(RSAUtil.class);

    // SHA1WithRSA 签名算法
    public static final String SHA1_WHIT_RSA = "SHA1withRSA";
    // SHA256withRSA 签名算法
    public static final String SHA256_WHIT_RSA = "SHA256withRSA";
    // RSA算法签名库
    private static final String RSA = "RSA";
    // 默认私钥长度
    private static final int DEFAULT_KEY_SIZE = 2048;

    private static Object lock = new Object();

    private static KeyFactory keyFactory = null;

    /**
     * 签名
     * @param content
     * @param encoding
     * @param base64PriKey
     * @param signAlgorithm
     * @return
     */
    public static String sign(String content, Charset encoding, String base64PriKey, String signAlgorithm) {
        PrivateKey privateKey = null;
        String sa = signAlgorithm != null ? signAlgorithm : SHA256_WHIT_RSA;
        try {
            privateKey = getKeyFactory().generatePrivate(new PKCS8EncodedKeySpec(Base64.decodeBase64(base64PriKey)));
            Signature signature = Signature.getInstance(sa);
            signature.initSign(privateKey);
            signature.update(content.getBytes(encoding));
            byte[] signByte = signature.sign();
            return Base64.encodeBase64String(signByte);
        } catch (InvalidKeySpecException e) {
            log.error("InvalidKeySpecException", e);
        } catch (NoSuchAlgorithmException e) {
            log.error("NoSuchAlgorithmException", e);
        } catch (SignatureException e) {
            log.error("SignatureException", e);
        } catch (InvalidKeyException e) {
            log.error("InvalidKeyException", e);
        }
        log.error("sign fail，signAlgorithm：{}", sa);
        throw new RuntimeException("sign fail");
    }

    /**
     * 验签
     * @param content
     * @param encoding
     * @param signatureStr
     * @param base64PubKey
     * @param signAlgorithm
     * @return
     */
    public static boolean verify(String content, Charset encoding, String signatureStr, String base64PubKey, String signAlgorithm)  {
        PublicKey publicKey = null;
        String sa = signAlgorithm != null ? signAlgorithm : SHA256_WHIT_RSA;
        try {
            publicKey = getKeyFactory().generatePublic(new X509EncodedKeySpec(Base64.decodeBase64(base64PubKey)));
            Signature signature = Signature.getInstance(sa);
            signature.initVerify(publicKey);
            signature.update(content.getBytes(encoding));
            return signature.verify(Base64.decodeBase64(signatureStr));
        } catch (InvalidKeySpecException e) {
            log.error("InvalidKeySpecException", e);
        } catch (NoSuchAlgorithmException e) {
            log.error("NoSuchAlgorithmException", e);
        } catch (SignatureException e) {
            log.error("SignatureException", e);
        } catch (InvalidKeyException e) {
            log.error("InvalidKeyException", e);
        }
        log.error("verify fail，signAlgorithm：{}", sa);
        throw new RuntimeException("verify fail");
    }

    /**
     * 生成指定位数私钥和对应公钥，并编码成Base64字符串
     * @param priKeySize
     * @return Base64编码字符串私钥
     * @throws NoSuchAlgorithmException
     */
    public static List<String> genBase64PriKeyAndPubKey(int priKeySize) throws NoSuchAlgorithmException {
        SecureRandom sr = new SecureRandom();
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);
        keyPairGenerator.initialize(priKeySize != 0 ? priKeySize : DEFAULT_KEY_SIZE, sr);
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
//        BASE64Encoder encoder = new BASE64Encoder();
        String priKeyStr = Base64.encodeBase64String(privateKey.getEncoded());
        String pubKeyStr = Base64.encodeBase64String(publicKey.getEncoded());
        return Arrays.asList(priKeyStr, pubKeyStr);
    }

    /**
     * 根据私钥生成公钥
     * @param base64PriKey
     * @return base64编码后的公钥字符串
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws InvalidKeySpecException
     */
    public static String genBase64PubKeyByPriKey(String base64PriKey) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(base64PriKey));
        PublicKey publicKey = getKeyFactory().generatePublic(keySpec);
        return Base64.encodeBase64String(publicKey.getEncoded());
    }


    private static KeyFactory getKeyFactory() {
        if (keyFactory == null) {
            synchronized (lock) {
                if (keyFactory == null) {
                    try {
                        keyFactory = KeyFactory.getInstance(RSA);
                    } catch (NoSuchAlgorithmException e) {
                        log.error("create keyFactory fail", e);
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return keyFactory;
    }

    public static void main(String[] args) throws Exception {
        List<String> keyPair = genBase64PriKeyAndPubKey(2048);
        String priKey = keyPair.get(0);
        String pubKey = keyPair.get(1);
        System.out.println("privateKey:" + priKey);
        System.out.println("PublicKey:" + pubKey);

        String signStr = sign("addag您好，签名", Charset.forName("UTF-8"), priKey, null);
        System.out.println("signStr:" + signStr);
        boolean verify = verify("addag您好，签名", Charset.forName("UTF-8"), signStr, pubKey, null);
        System.out.println("verify:" + verify);
    }

}
