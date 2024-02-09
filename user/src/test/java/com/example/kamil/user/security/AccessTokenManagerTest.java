package com.example.kamil.user.security;

import com.example.kamil.user.TestSupport;
import com.example.kamil.user.model.entity.User;
import com.example.kamil.user.model.properties.security.JwtData;
import com.example.kamil.user.model.properties.security.SecurityProperties;
import com.example.kamil.user.service.security.AccessTokenManager;
import com.example.kamil.user.utils.PublicPrivateKeyUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public  class AccessTokenManagerTest  extends TestSupport {


    private SecurityProperties securityProperties;
    private PublicPrivateKeyUtil publicPrivateKeyUtil;
    private AccessTokenManager accessTokenManager;


    private final String privateKeyStr = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQChCxoVp9X/77TanaJjqcK7Yy3g3AmiEhuKh0v3f6DeN0xQcE3ZvwhnNvNCQw9dogsXdm3E0ESRxtyji2B6X6Ql/J7TGH6AXYrdhNYb91H1TR+dIZ6QuxJZ66HPGyW2UxyGfvWJS/N2KpxmhCHcjpjtAEFbAyDAuxeSZd99SYwMixv4MQnNa+A/I+fZhf5St9j0uA8h6ppBKNOnPMFJmvxExvB34bp41qQk7mtmUX79jwUz9qVmXSzBFJ7/+SIFQCpBr5ltp9Z0FbtzFuW25gQWoJ44OR5WeDKsvzDvVFhybqDfjSp6Mcrctti7FDXqxH2JgXq67VmwIxlirs9vjUtNAgMBAAECggEAIWblFSLHjxsPvtw9J0C5pBYxGNdqpBPvZG8lgYMWQaDkkjaI3yXxPz81e0PfdwM6G7azk8PCxR2i+8TX0OnvaPWZl50X99XvDH0Vz50fgapqaTPk6I30g3QNDqDJ1KQuYXMJyF1ZdOPnppe3UkC9VULdS1w9udvUSsgYb6sGgj8v5ot9Xkfs10wvvBsQRvTg3YlDWKed4Z6LZdeQoDZS9Yh+RCJplGSgsIaFcHQuUMYQ3YzEhi9boNLLHjx34uSjS+h7j6trmDefrRAN+Ine91zI6Ud0NEs2jmViC0sDi1VTdCRhk6YVSR7RAjgGUPOa3m7xwjmZB2TIp8Nrw3JdgQKBgQDLD8FMb0JKM5NM7mB9+NtVh9prjR7zrjDFENYeFscKO+7lel9Vbuh+boLeYeA/8B8Q85IzR2OY5J+mgxCvNYVBKUrliY9C9Yd7w5FGW3ylhtLl1eby7BaAhEHLK32BM9Q9DFHsh97rHoaRB68E2BLoKbBrvPW8oTwFud9Z9Q2WrQKBgQDLBxIfFDLoAM+B692u3mNJiv8bm1524klXQfQVw+azOnrSf2OqDCE9g8+wEQLjURQ+PFTQFLG2qVq8sR8Zpe3oRnHbuGwPEOK8kxuSbSL66ScxFcHNyBGtJ+g66BxlG9IQoUUdqnZp3Kio3FaMVtpEQkVUzbqIV4IcaBRD/L87IQKBgBWH7SISpf/CKjs9B2Q0D+WoywMeaorCDI/IsjtshNxT8IfKivM/GE3Xn+6+iA2fg1vDx9vCFRBwJXF9cjZxwVhgbX2bMWKTMDNyUCQarUPL4O7X2G4nlFc6LtPBH8SkXzu9463l1kAt82zLWMZ1tsOBMCCsX44JtpiQ1dru/erdAoGBALpHw422d7q8u6gGJMFElK6ulMDcvkCwAQDJp82sj+jUeErYzTYBqSIK5a0y/oZZJwyvSFYfFqt1GabVkKZ0wyOCNweVX7g3EwJYq0yfTCARen8j6k0IC4fnfMFHpH30l7ngbkz40WryJeHBQLvFGMDWWUyk8kgUmnG74RQ8O61hAoGAJq/AxoDaX/Wut3RMlDIC9eGCzkuY6WlhFIk43A66wXy1Yg01qnOiulQXBRuuPbkEWdsI/93EA0zgDXTrqrxNEY+0uwTvAl8RdlYTP0Qv0mG0ivnktrXz3M/0vODnG0Z9Ac1WG8YiPpHrkMqSFm1MsVgYs2q6pYvL3KAONkJj3OM=";
    private final String publicKeyStr ="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoQsaFafV/++02p2iY6nCu2Mt4NwJohIbiodL93+g3jdMUHBN2b8IZzbzQkMPXaILF3ZtxNBEkcbco4tgel+kJfye0xh+gF2K3YTWG/dR9U0fnSGekLsSWeuhzxsltlMchn71iUvzdiqcZoQh3I6Y7QBBWwMgwLsXkmXffUmMDIsb+DEJzWvgPyPn2YX+UrfY9LgPIeqaQSjTpzzBSZr8RMbwd+G6eNakJO5rZlF+/Y8FM/alZl0swRSe//kiBUAqQa+ZbafWdBW7cxbltuYEFqCeODkeVngyrL8w71RYcm6g340qejHK3LbYuxQ16sR9iYF6uu1ZsCMZYq7Pb41LTQIDAQAB";

    @BeforeEach
    void setUp() {

        securityProperties = mock(SecurityProperties.class);

        JwtData jwtData = new JwtData(
                publicKeyStr,
                privateKeyStr, 1800000
        );

        SecurityProperties securityProperties = new SecurityProperties();
        securityProperties.setJwt(jwtData);

        publicPrivateKeyUtil = new PublicPrivateKeyUtil(securityProperties);

        accessTokenManager = new AccessTokenManager(securityProperties);
        when(this.securityProperties.getJwt()).thenReturn(jwtData);
    }

    @Test
    void testGenerate_itShouldGenerateAccessToken() {
        // Arrange
        User user = new User();
        user.setId(123L);
        user.setEmail("test@example.com");

        // Act
        String token = accessTokenManager.generate(user);
//
//        // Assert
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(publicPrivateKeyUtil.getPublicKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals("test@example.com", claims.get("email"));
        assertEquals("123", claims.getSubject());
    }

    @Test
    void testRead_itShouldReadClaimsFromToken() {
        // Arrange
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJlbWFpbCI6ImVtYWlsMUBnbWFpbC5jb20iLCJleHAiOjE3MDc1MTc2NDYsImlhdCI6MTcwNzUxNTg0Niwic3ViIjoiMSJ9.K6kZweBhhDLt_B2IiRg_bI5BoBBRfRDm7i0DXYwKff8yotp2KAQChPWyFhRZ70qlbPmiIUtiwSRr4YEVXxjFe64xh8nXt2NWYrKWxRak4NYtf62XUJzc9fEXt0pGujlF3NPL-_ydzbhxJeAPJh7WPiSzGRRmfbmpaxUxry5gu2Zo_BQZmHtpq4P0Fi8b8uFEIpjCaqiAOlanxn4lJ3gJMHUx9OiXIF9ljVJ8sNSg4BqgBeNKawv5PY4s4-6QzGikOv_Z56L8gdpfzRPzaR_l2UAmwsHnjdsg6Cl6dCSVM6BB4T7LKuaMXd-h7nyIdzTjfwDHvHPfvm8gS0BirvB-yA"; // Replace with a valid token

        // Act
        Claims claims = accessTokenManager.read(token);

        // Assert
        // Add specific assertions based on the structure/content of your token
        // Example:
        assertEquals("email1@gmail.com", claims.get("email"));
    }


    @Test
    void testGenerateWithInvalidUser_itShouldThrowException() {
        // Arrange
        User user = null;

        // Act and Assert
        assertThrows(NullPointerException.class, () -> accessTokenManager.generate(user));
    }

    @Test
    void testGenerateAndReadExpiredToken_itShouldThrowException() {
        // Arrange
        User user = new User();
        user.setId(123L);
        user.setEmail("test@example.com");

        // Mock an expired token
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() - 1000); // 1 second ago
        when(securityProperties.getJwt().getAccessTokenValidityTime()).thenReturn(1000); // 1 second

        // Act
        String token = accessTokenManager.generate(user);

        // Assert
        assertThrows(Exception.class, () -> accessTokenManager.read(token));
    }

    @Test
    void testGenerateAndReadTokenWithInvalidSignature_itShouldThrowException() {
        // Arrange
        User user = new User();
        user.setId(123L);
        user.setEmail("test@example.com");

        // Act
        String token = accessTokenManager.generate(user);

        // Modify the token to have an invalid signature
        token = token.replace("a", "b");

        // Assert
        String finalToken = token;
        assertThrows(Exception.class, () -> accessTokenManager.read(finalToken));
    }
}
