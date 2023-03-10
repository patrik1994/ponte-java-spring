package hu.ponte.hr.services;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class SignService {
    private final String SIGNING_ALGORITHM = "SHA256withRSA";
    private final String KEY_FILES_LOCATION = "config/keys/";
    private final String PRIVATE_KEY_FILENAME = "key.private";
    private final String PUBLIC_KEY_FILENAME = "key.pub";
    private final String RSA_INSTANCE = "RSA";

    @Getter
    PrivateKey privateKey;
    @Getter
    PublicKey publicKey;

    public SignService() throws URISyntaxException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        initPrivateKey();
        initPublicKey();
    }

    /**
     * Loads the private key (PKCS8 format) from file system and assign it to the privateKey class attribute.
     * @throws URISyntaxException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private void initPrivateKey() throws URISyntaxException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        URL uri = this.getClass().getClassLoader().getResource(KEY_FILES_LOCATION + PRIVATE_KEY_FILENAME);
        byte[] input = Files.readAllBytes(Paths.get(uri.toURI()));

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(input);
        KeyFactory kf = KeyFactory.getInstance(RSA_INSTANCE);

        this.privateKey = kf.generatePrivate(spec);
    }

    /**
     * Loads the public key from file system and assign it to the publicKey class attribute.
     * @throws URISyntaxException
     * @throws IOException
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    private void initPublicKey() throws URISyntaxException, IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        URL uri = this.getClass().getClassLoader().getResource(KEY_FILES_LOCATION + PUBLIC_KEY_FILENAME);
        byte[] input = Files.readAllBytes(Paths.get(uri.toURI()));

        X509EncodedKeySpec spec = new X509EncodedKeySpec(input);
        KeyFactory kf = KeyFactory.getInstance(RSA_INSTANCE);
        this.publicKey = kf.generatePublic(spec);
    }

    /**
     * Creates the digital signature for the input using the private key.
     * @param input
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public byte[] createDigitalSignature(byte[] input) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance(SIGNING_ALGORITHM);
        signature.initSign(privateKey);
        signature.update(input);

        return signature.sign();
    }

    public byte[] encodeBase64(byte[] input) {
        return Base64.getMimeEncoder().encode(input);
    }

    /**
     * Verifies that the digital signature is valid or not based on the signature, input and the public key.
     * @param input
     * @param signatureToVerify
     * @return
     * @throws Exception
     */
    public boolean verifyDigitalSignature(byte[] input, byte[] signatureToVerify) throws Exception {
        Signature signature = Signature.getInstance(SIGNING_ALGORITHM);
        signature.initVerify(publicKey);
        signature.update(input);
        return signature.verify(signatureToVerify);
    }
}
