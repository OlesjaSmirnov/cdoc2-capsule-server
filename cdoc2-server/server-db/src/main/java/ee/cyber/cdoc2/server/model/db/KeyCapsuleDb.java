package ee.cyber.cdoc2.server.model.db;

import ee.cyber.cdoc2.shared.crypto.Crypto;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


/**
 * CDOC2 key capsule database entity
 */
@Data
@Entity
@Table(name = "cdoc2_capsule")
@Slf4j
@EntityListeners(AuditingEntityListener.class)
@Accessors(chain = true)
public class KeyCapsuleDb {

    // key capsule type
    public enum CapsuleType {
        SECP384R1, // elliptic curve
        RSA
    }

    @PrePersist
    private void generateTransactionId() throws NoSuchAlgorithmException {
        byte[] sRnd = new byte[16];
        Crypto.getSecureRandom().nextBytes(sRnd);
        this.transactionId = String.format("KC%s", HexFormat.of().formatHex(sRnd));
    }

    @Id
    @Column(length = 34)
    @Size(max = 34)
    private String transactionId;

    /**
     * Depending on capsuleType:
     *  - secp384r1 base64 TLS encoded (97bytes) EC public key
     *  - DER encoded RSA public key
     */
    @NotNull
    @Column(nullable = false)
    @Size(max = 2500) // 16 K RSA public key is ~2100 bytes
    @JdbcTypeCode(SqlTypes.BINARY)
    private byte[] recipient;

    @NotNull
    @Column(nullable = false)
    @Size(max = 3000)
    @JdbcTypeCode(SqlTypes.BINARY)
    private byte[] payload;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CapsuleType capsuleType;

    @CreatedDate
    private Instant createdAt;

}
