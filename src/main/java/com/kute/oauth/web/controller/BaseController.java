package com.kute.oauth.web.controller;

import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by longbai on 2017/7/12.
 */
public class BaseController {

    public OAuthResponse getFailClientIdResponse() throws OAuthSystemException{
        return OAuthASResponse
                .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                .setErrorDescription("无效的客户端Id")
                .buildJSONMessage();
    }

    public OAuthResponse getFailClientSecretResponse() throws OAuthSystemException {
        return OAuthResponse.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                .setError(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT)
                .setErrorDescription("客户端安全KEY认证失败！")
                .buildJSONMessage();
    }

    public OAuthResponse getFailAuthCodeResponse() throws OAuthSystemException {
        return OAuthResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                .setError(OAuthError.TokenResponse.INVALID_GRANT)
                .setErrorDescription("错误的授权码")
                .buildJSONMessage();
    }

    public OAuthResponse getSC_OKResponse(String accessToken, Long expireTime) throws OAuthSystemException {
        return OAuthASResponse
                .tokenResponse(HttpServletResponse.SC_OK)
                .setAccessToken(accessToken)
                .setExpiresIn(String.valueOf(expireTime))
                .buildJSONMessage();
    }
}
