package org.knowm.xchange.kraken.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import javax.crypto.Mac;
import javax.ws.rs.FormParam;

import org.apache.commons.codec.binary.Base64;
import org.knowm.xchange.service.BaseParamsDigest;
import si.mazi.rescu.RestInvocation;

/**
 * @author Benedikt Bünz
 */
public class KrakenDigest extends BaseParamsDigest {

    /**
     * Constructor
     *
     * @param secretKeyBase64
     * @throws IllegalArgumentException if key is invalid (cannot be base-64-decoded or the decoded
     *                                  key is invalid).
     */
    private KrakenDigest(byte[] secretKeyBase64) {

        super(secretKeyBase64, HMAC_SHA_512);
    }

    public static KrakenDigest createInstance(String secretKeyBase64) {

        if (secretKeyBase64 != null) {
            return new KrakenDigest(Base64.decodeBase64(secretKeyBase64.getBytes()));
        } else return null;
    }

    @Override
    public String digestParams(RestInvocation restInvocation) {

        MessageDigest sha256;
        try {
            sha256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(
                    "Illegal algorithm for post body digest. Check the implementation.");
        }
        sha256.update(restInvocation.getParamValue(FormParam.class, "nonce").toString().getBytes());
        sha256.update(restInvocation.getRequestBody().getBytes());

        Mac mac512 = getMac();
        mac512.update(("/" + restInvocation.getPath()).getBytes());
        mac512.update(sha256.digest());

        return new String(Base64.encodeBase64(mac512.doFinal())).trim();
    }
}
