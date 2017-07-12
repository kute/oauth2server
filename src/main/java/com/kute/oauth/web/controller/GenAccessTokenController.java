package com.kute.oauth.web.controller;

import com.kute.oauth.service.CommentService;
import com.kute.oauth.web.util.Constants;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by longbai on 2017/7/12.
 */
@Controller
@RequestMapping("api/v1")
public class GenAccessTokenController extends BaseController {

    @Resource
    private CommentService commentService;

    /**
     * 请求 授权码
     * @param request
     * @return
     * @throws OAuthSystemException
     * @throws OAuthProblemException
     */
    public ResponseEntity<Object> validAndGenAuthCode(
            HttpServletRequest request) throws OAuthSystemException, OAuthProblemException {
        return null;
    }

    /**
     * 带着授权码来请求 访问令牌
     * @param request
     * @return
     */
    @RequestMapping(value = "/products/{productKey}/oauth/authorize", method = RequestMethod.GET)
    public ResponseEntity<Object> validAndGenAccessToken(
            HttpServletRequest request) throws OAuthSystemException, OAuthProblemException {

        OAuthTokenRequest tokenRequest = new OAuthTokenRequest(request);

        String clientId = tokenRequest.getClientId();

        // 客户端ID校验：client_id
        if(!commentService.checkClientId(clientId)) {
            OAuthResponse oAuthResponse = getFailClientIdResponse();
            return new ResponseEntity<Object>(oAuthResponse.getBody(), HttpStatus.valueOf(oAuthResponse.getResponseStatus()));
        }

        // 客户端密钥：client_secret
        String secret = tokenRequest.getClientSecret();
        if(!commentService.checkClientSecret(secret)) {
            OAuthResponse oAuthResponse = getFailClientSecretResponse();
            return new ResponseEntity<Object>(oAuthResponse.getBody(), HttpStatus.valueOf(oAuthResponse.getResponseStatus()));
        }

        // 授权码校验
        // grant-type: GrantType.AUTHORIZATION_CODE
        // authorization_code、password、refresh_token、client_credentials
        String authCode = tokenRequest.getParam(OAuth.OAUTH_CODE);
        if(tokenRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.AUTHORIZATION_CODE.toString())){
            if(!commentService.checkAuthCode(authCode)){
                OAuthResponse response = getFailAuthCodeResponse();
                return new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
            }
        }

        // 生成访问令牌
        String accessToken = commentService.genAccessToken();
        commentService.addAccessToken(accessToken, commentService.getUsernameByAuthCode(authCode));

        OAuthResponse response = getSC_OKResponse(accessToken, Constants.A_HOUR);

        return new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
    }
}
