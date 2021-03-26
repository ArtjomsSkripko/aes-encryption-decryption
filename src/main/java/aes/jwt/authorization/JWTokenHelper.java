package aes.jwt.authorization;

import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Objects;

import com.google.common.collect.Sets;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyOperation;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.BadJWTException;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import net.minidev.json.JSONObject;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class JWTokenHelper {

    @Autowired
    private Environment env;

    private static final String JWT_CLAIMS_USER_ID = "user_id";
    private static final String JWT_CLAIMS_USERNAME = "username";
    private static final String JWT_CLAIMS_NAME = "name";
    private static final String JWT_CLAIMS_SURNAME = "surname";
    private static final String JWT_CLAIMS_USER_ROLE = "role";
    private static final String JWT_CLAIMS_VALID_TO = "valid_to";

    public UserToken parseAccessToken(String token) throws ParseException, BadJOSEException, JOSEException {
        JWKSet jwkSet = new JWKSet();
        JWK hmacJWK = new OctetSequenceKey(Base64URL.encode(Objects.requireNonNull(env.getProperty("jwt.key")).getBytes()), KeyUse.SIGNATURE,
                Sets.newHashSet(KeyOperation.SIGN, KeyOperation.VERIFY), JWSAlgorithm.HS256,
                null, null, null, null, null, null);
        jwkSet = new JWKSet(ListUtils.union(jwkSet.getKeys(), Collections.singletonList(hmacJWK)));
        UserToken authContext = new UserToken();
        Exception returnException = null;
        try {
            JWKSource<SecurityContext> keySource = new ImmutableJWKSet<>(jwkSet);
            ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
            SignedJWT signedJWT = SignedJWT.parse(token);
            
            JWSKeySelector<SecurityContext> keySelector = new JWSVerificationKeySelector<>(signedJWT.getHeader().getAlgorithm(), keySource);
            jwtProcessor.setJWSKeySelector(keySelector);
            JWTClaimsSet claimsSet = jwtProcessor.process(token, null);
            authContext.setTokenId(claimsSet.getJWTID());
            authContext.setCustomerId(claimsSet.getStringClaim(JWT_CLAIMS_USER_ID));
            authContext.setUsername(claimsSet.getStringClaim(JWT_CLAIMS_USERNAME));
            authContext.setUserRole(claimsSet.getStringClaim(JWT_CLAIMS_USER_ROLE));
            authContext.setName(claimsSet.getStringClaim(JWT_CLAIMS_NAME));
            authContext.setSurname(claimsSet.getStringClaim(JWT_CLAIMS_SURNAME));
            authContext.setValidTo(ZonedDateTime.parse(claimsSet.getStringClaim(JWT_CLAIMS_VALID_TO)));
        } catch (BadJWTException e) {
            returnException = new Exception(e.getMessage());
        }
        authContext.setValidationException(returnException);
        return authContext;
    }

    public String createJWT(String userId, String userName, String name, String surname, String validTo) throws JOSEException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JWT_CLAIMS_USER_ID, userId);
        jsonObject.put(JWT_CLAIMS_USERNAME, userName);
        jsonObject.put(JWT_CLAIMS_USER_ROLE, "REGULAR_USER");
        jsonObject.put(JWT_CLAIMS_NAME, name);
        jsonObject.put(JWT_CLAIMS_SURNAME, surname);
        jsonObject.put(JWT_CLAIMS_VALID_TO, validTo);
        JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS256), new Payload(jsonObject));
        jwsObject.sign(new MACSigner(Objects.requireNonNull(env.getProperty("jwt.key"))));

        return jwsObject.serialize();
    }
}
