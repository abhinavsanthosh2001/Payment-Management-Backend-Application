package com.assignment.paymentmanagementservice.util;

import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.Synchronized;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.SerializationUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.Base64;

@Configuration
@Converter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AesEncryptor implements AttributeConverter<Object, String> {

    static final String ENCRYPTION_CYPHER = "AES";
    @Value("${aes.encryption.key}")
    String encryptionKey;
    Key key;
    Cipher cipher;

    @Synchronized
    private Key getKey() {
        if (key == null) {
            key = new SecretKeySpec(encryptionKey.getBytes(), ENCRYPTION_CYPHER);
        }
        return key;
    }

    @Synchronized
    private Cipher getCipher() throws GeneralSecurityException {
        if (cipher == null) {
            cipher = Cipher.getInstance(ENCRYPTION_CYPHER);
        }
        return cipher;
    }

    @Synchronized
    private void initCipher(int encryptionMode) throws GeneralSecurityException {
        getCipher().init(encryptionMode, getKey());
    }

    @SneakyThrows
    @Override
    @Synchronized
    public String convertToDatabaseColumn(Object attribute) {
        if (attribute == null) {
            return null;
        }
        initCipher(Cipher.ENCRYPT_MODE);
        byte[] bytes = SerializationUtils.serialize(attribute);
        assert bytes != null;
        return Base64.getEncoder().encodeToString(getCipher().doFinal(bytes));
    }

    @SneakyThrows
    @Override
    @Synchronized
    public Object convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        initCipher(Cipher.DECRYPT_MODE);
        byte[] bytes = getCipher().doFinal(Base64.getDecoder().decode(dbData));
        return SerializationUtils.deserialize(bytes);

    }
}
