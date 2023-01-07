package in.solomk.dictionary.api.security;

import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static java.util.Collections.singletonList;

@AllArgsConstructor
@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;

    public String generateToken(String subjectId) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .audience(singletonList("dictionary"))
                .issuer("dictionary")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(subjectId)
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims))
                         .getTokenValue();
    }
}
