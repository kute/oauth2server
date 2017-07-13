package com.kute.oauth.web.controller;

import com.kute.oauth.service.CommentService;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by longbai on 2017/7/12.
 */
public class BaseController {

    @Resource
    private CommentService commentService;

    private OAuthResponse getFailResponse(int code, String error, String errorDesc, String state) throws OAuthSystemException {
        return OAuthASResponse
                .errorResponse(code)
                .setError(error)
                .setErrorDescription(errorDesc)
                .setErrorUri(state)
                .buildJSONMessage();
    }

    public OAuthResponse getFailClientIdResponse() throws OAuthSystemException{
        return getFailResponse(HttpServletResponse.SC_BAD_REQUEST,
                OAuthError.TokenResponse.INVALID_CLIENT, "Invalid ClientId", null);
    }

    public OAuthResponse getFailClientSecretResponse() throws OAuthSystemException {
        return getFailResponse(HttpServletResponse.SC_UNAUTHORIZED,
                OAuthError.TokenResponse.UNAUTHORIZED_CLIENT, "Wrong ClientSecret", null);
    }

    public OAuthResponse getFailAuthCodeResponse() throws OAuthSystemException {
        return getFailResponse(HttpServletResponse.SC_BAD_REQUEST,
                OAuthError.TokenResponse.INVALID_GRANT, "Wrong AuthCode", null);
    }

    public OAuthResponse getSC_OKResponse(String accessToken, Long expireTime) throws OAuthSystemException {
        return OAuthASResponse
                .tokenResponse(HttpServletResponse.SC_OK)
                .setAccessToken(accessToken)
                .setExpiresIn(String.valueOf(expireTime))
                .buildJSONMessage();
    }
}
