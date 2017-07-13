package com.kute.oauth.service.impl;

import com.google.common.base.Strings;
import com.kute.oauth.cache.Cache;
import com.kute.oauth.service.CommentService;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.springframework.stereotype.Service;

/**
 * Created by longbai on 2017/7/12.
 */
@Service
public class CommentServiceImpl implements CommentService {

    public Boolean checkClientId(String clientId) {
        return null;
    }

    public Boolean checkClientSecret(String secret) {
        return null;
    }

    public boolean checkAuthCode(String authCode) {
        return false;
    }

    public String genAccessToken() throws OAuthSystemException{
        OAuthIssuerImpl authIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
        return authIssuerImpl.accessToken();
    }

    @Override
    public String genAuthCode(String responseType) throws OAuthSystemException {
        String authCode = null;
        if(!Strings.isNullOrEmpty(responseType) && responseType.equals(ResponseType.CODE.toString())) {
            OAuthIssuerImpl oAuthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
            authCode = oAuthIssuerImpl.authorizationCode();
        }
        return authCode;
    }

    public String getUsernameByAuthCode(String authCode) {
        return String.valueOf(Cache.get(authCode));
    }

    public String getUsernameByAccessToken(String accessToken) {
        return null;
    }

    public long getExpireIn() {
        return 3600L;
    }

    public void addAuthCode(String authCode, String username) {

    }

    public void addAccessToken(String accessToken, String username) {
        Cache.set(accessToken, username);
    }

    @Override
    public boolean checkAccessToken(String accessToken) {
        return Cache.get(accessToken) != null;
    }
}
