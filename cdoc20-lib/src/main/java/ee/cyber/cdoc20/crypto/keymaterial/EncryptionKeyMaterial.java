package ee.cyber.cdoc20.crypto.keymaterial;

import java.security.Key;
import java.security.PublicKey;
import java.util.Optional;
import javax.crypto.SecretKey;
import javax.security.auth.DestroyFailedException;
import javax.security.auth.Destroyable;

import ee.cyber.cdoc20.crypto.EncryptionKeyOrigin;


/**
 * Represents key material required for encryption.
 */
public interface EncryptionKeyMaterial extends Destroyable {

    /**
     * @return the key to derive the encryption key if exists
     */
    Optional<Key> getKey();

    /**
     * @return identifier for the encryption key
     */
    String getLabel();

    /**
     * Identifies the origin of key derivation. This data is used to find the correct
     * encryption algorithm.
     * @return EncryptionKeyOrigin encryption key origin
     */
    EncryptionKeyOrigin getKeyOrigin();

    /**
     * Create EncryptionKeyMaterial from publicKey and keyLabel. To decrypt CDOC, recipient must have
     * the private key part of the public key. RSA and EC public keys are supported by CDOC.
     * @param publicKey public key
     * @param keyLabel  key label
     * @return EncryptionKeyMaterial object
     */
    static EncryptionKeyMaterial fromPublicKey(PublicKey publicKey, String keyLabel) {
        return new EncryptionKeyMaterial() {

            @Override
            public Optional<Key> getKey() {
                return Optional.of(publicKey);
            }

            @Override
            public String getLabel() {
                return keyLabel;
            }

            @Override
            public EncryptionKeyOrigin getKeyOrigin() {
                return EncryptionKeyOrigin.FROM_PUBLIC_KEY;
            }

            @Override
            public void destroy() {
                // no secret key material that needs to be destroyed
            }
        };
    }

    /**
     * Create EncryptionKeyMaterial from secret.
     * To decrypt CDOC, recipient must also have same preSharedKey that is identified by the same
     * keyLabel
     * @param preSharedKey preSharedKey will be used to generate key encryption key
     * @param keyLabel     unique identifier for preSharedKey
     * @return EncryptionKeyMaterial object
     */
    static EncryptionKeyMaterial fromSecret(SecretKey preSharedKey, String keyLabel) {
        return new EncryptionKeyMaterial() {

            @Override
            public Optional<Key> getKey() {
                return Optional.of(preSharedKey);
            }

            @Override
            public String getLabel() {
                return keyLabel;
            }

            @Override
            public EncryptionKeyOrigin getKeyOrigin() {
                return EncryptionKeyOrigin.FROM_SECRET;
            }

            @Override
            public void destroy() throws DestroyFailedException {
                preSharedKey.destroy();
            }

            @Override
            public boolean isDestroyed() {
                return preSharedKey.isDestroyed();
            }
        };
    }

    /**
     * Create PasswordEncryptionKeyMaterial from password.
     * To decrypt CDOC, recipient must also have same preSharedKey and salt that are identified by
     * the same keyLabel
     * @param password password chars for extracting pre-shared SecretKey
     * @param keyLabel unique identifier for preSharedKey
     * @return PasswordEncryptionKeyMaterial object
     */
    static PasswordEncryptionKeyMaterial fromPassword(
        char[] password, String keyLabel
    ) {
        return new PasswordEncryptionKeyMaterial(password, keyLabel);
    }

}
