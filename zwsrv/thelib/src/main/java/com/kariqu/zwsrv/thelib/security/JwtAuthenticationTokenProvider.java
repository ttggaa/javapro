package com.kariqu.zwsrv.thelib.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by simon on 11/04/17.
 */

@Component
public class JwtAuthenticationTokenProvider {

    private static final String CLAIM_KEY_CREATED = "created";

    private static final String CLAIM_KEY_APP_ID = "appid";
    private static final String CLAIM_KEY_AUTH_TYPE = "authtype";
    private static final String CLAIM_KEY_AUTH_ID = "authid";
    private static final String CLAIM_KEY_IDENTIFIER = "identifier";

    private static final String CLAIM_KEY_USER_ID = "userid";
    private static final String CLAIM_KEY_NICKNAME = "nickname";
//    private static final String CLAIM_KEY_AVATAR = "avatar";
    private static final String CLAIM_KEY_ROLE = "role";
    private static final String CLAIM_KEY_GENDER = "gender";


    private static final String CLAIM_KEY_TOKEN_COUNT = "tokencount";


    private String secretKey;
    private long tokenValidityInSeconds;
    private long tokenValidityInSecondsForRememberMe;

    @PostConstruct
    public void init() {
        this.secretKey = "A8c6efgHiJKlmnOp";
        this.tokenValidityInSeconds = 24*60*60*30*12;
        this.tokenValidityInSecondsForRememberMe = 24*60*60*30*12;
    }

    public String createToken(CurrentUserDetails currentUser, Boolean rememberMe) {

        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_CREATED, new Date());

        claims.put(CLAIM_KEY_APP_ID,currentUser.getAppId()!=null?currentUser.getAppId():"");
        claims.put(CLAIM_KEY_AUTH_TYPE, currentUser.getAuthType());
        claims.put(CLAIM_KEY_AUTH_ID, currentUser.getAuthId());
        claims.put(CLAIM_KEY_IDENTIFIER, currentUser.getIdentifier()!=null?currentUser.getIdentifier():"");

        claims.put(CLAIM_KEY_USER_ID, currentUser.getUserId());
        claims.put(CLAIM_KEY_NICKNAME, currentUser.getNickName()!=null?currentUser.getNickName():"");
        claims.put(CLAIM_KEY_ROLE,currentUser.getRole());
        claims.put(CLAIM_KEY_GENDER,currentUser.getGender());


        Date expirationDate = new Date(System.currentTimeMillis() + tokenValidityInSeconds * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public JwtAuthenticationClaims validateToken(String authToken) {
        try {
            final Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken).getBody();
            final Date expiration = claims.getExpiration();
            boolean isTokenExpired = expiration.before(new Date());
            if (!isTokenExpired) {
                return toAuthenticationClaims(claims);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void invalidate(String authToken) {

    }

    JwtAuthenticationClaims toAuthenticationClaims(final Claims claims) {

        String appId = claims.get(CLAIM_KEY_APP_ID)!=null?(String)claims.get(CLAIM_KEY_APP_ID):"";
        String authType = claims.get(CLAIM_KEY_AUTH_TYPE)!=null?(String)claims.get(CLAIM_KEY_AUTH_TYPE):"";
        int authId = claims.get(CLAIM_KEY_AUTH_ID)!=null?(Integer)claims.get(CLAIM_KEY_AUTH_ID):0;
        String identifier = claims.get(CLAIM_KEY_IDENTIFIER)!=null?(String)claims.get(CLAIM_KEY_IDENTIFIER):"";

        int userId = (Integer)claims.get(CLAIM_KEY_USER_ID);
        String nickName = claims.get(CLAIM_KEY_NICKNAME)!=null?(String)claims.get(CLAIM_KEY_NICKNAME):"";

        int role = claims.get(CLAIM_KEY_ROLE)!=null?(Integer)claims.get(CLAIM_KEY_ROLE):0;
        int gender = claims.get(CLAIM_KEY_GENDER)!=null?(Integer)claims.get(CLAIM_KEY_GENDER):0;


        JwtAuthenticationClaims claimsInfo = new JwtAuthenticationClaims();
        claimsInfo.setAppId(appId);
        claimsInfo.setAuthType(authType);
        claimsInfo.setAuthId(authId);
        claimsInfo.setIdentifier(identifier);

        claimsInfo.setUserId(userId);
        claimsInfo.setNickName(nickName);

        claimsInfo.setRole(role);
        claimsInfo.setGender(gender);

        return claimsInfo;
    }

}
