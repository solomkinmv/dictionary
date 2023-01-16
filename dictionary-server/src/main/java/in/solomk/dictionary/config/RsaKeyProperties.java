package in.solomk.dictionary.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "rsa")
@ConditionalOnMissingBean(RsaKeyProperties.class)
public record RsaKeyProperties(RSAPublicKey publicKey, RSAPrivateKey privateKey) {

}
