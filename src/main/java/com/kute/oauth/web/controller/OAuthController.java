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
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.common.message.types.TokenType;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.apache.oltu.oauth2.rs.response.OAuthRSResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by longbai on 2017/7/12.
 */
@Controller
@RequestMapping("api/v1")
public class OAuthController extends BaseController {

    @Resource
    private CommentService commentService;

    /**
     * 资源请求
     * @param request
     * @return
     * @throws OAuthSystemException
     * @throws OAuthProblemException
     */
    @RequestMapping(value = "/oauth/resource", method = RequestMethod.GET)
    public ResponseEntity<Object> getResource(
            HttpServletRequest request) throws OAuthSystemException, OAuthProblemException {

        OAuthAccessResourceRequest accessResourceRequest = new OAuthAccessResourceRequest(request, ParameterStyle.QUERY);

        String accessToken = accessResourceRequest.getAccessToken();

        if(!commentService.checkAccessToken(accessToken)) {
            OAuthResponse oauthResponse = OAuthRSResponse
                    .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                    .setRealm("Apache Oltu")
                    .setError(OAuthError.ResourceResponse.INVALID_TOKEN)
                    .buildHeaderMessage();

            HttpHeaders headers = new HttpHeaders();
            headers.add(OAuth.HeaderType.WWW_AUTHENTICATE, oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));
            return new ResponseEntity(headers, HttpStatus.UNAUTHORIZED);
        }

        //返回用户名
        String username = commentService.getUsernameByAccessToken(accessToken);
        return new ResponseEntity(username, HttpStatus.OK);
    }

    /**
     * 请求授权码
     * @param request
     * @return
     * @throws OAuthSystemException
     * @throws OAuthProblemException
     */
    @RequestMapping(value = "/oauth/authorize", method = RequestMethod.GET)
    public ResponseEntity<Object> validAndGenAuthCode(
            HttpServletRequest request) throws OAuthSystemException, OAuthProblemException {

        OAuthTokenRequest tokenRequest = new OAuthTokenRequest(request);

        String clientId = tokenRequest.getClientId();

        // 校验clientid
        if(!commentService.checkClientId(clientId)) {
            OAuthResponse oAuthResponse = getFailClientIdResponse();
            return new ResponseEntity<Object>(oAuthResponse.getBody(), HttpStatus.valueOf(oAuthResponse.getResponseStatus()));
        }

        String userName = "admin";

        String responseType = tokenRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE);
        String authCode = commentService.genAuthCode(responseType);
        if(null != authCode) {
            commentService.addAuthCode(authCode, userName);
        }

        OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse.authorizationResponse(request, HttpServletResponse.SC_FOUND);

        //设置校验码
        builder.setCode(authCode);

        // 获取客户端重定向uri
        String redirectUri = tokenRequest.getParam(OAuth.OAUTH_REDIRECT_URI);

        OAuthResponse response = builder.location(redirectUri).buildBodyMessage();

        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setLocation(new URI(response.getLocationUri()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<Object>(headers, HttpStatus.valueOf(response.getResponseStatus()));
    }

    /**
     * 拿着授权码请求accesstoken
     * @param request
     * @return
     */
    @RequestMapping(value = "/oauth/token", method = RequestMethod.GET)
    public ResponseEntity<Object> validAndGenAccessToken(
            HttpServletRequest request) throws OAuthSystemException, OAuthProblemException {

        OAuthTokenRequest tokenRequest = new OAuthTokenRequest(request);

        String clientId = tokenRequest.getClientId();

        // �ͻ���IDУ�飺client_id
        if(!commentService.checkClientId(clientId)) {
            OAuthResponse oAuthResponse = getFailClientIdResponse();
            return new ResponseEntity<Object>(oAuthResponse.getBody(), HttpStatus.valueOf(oAuthResponse.getResponseStatus()));
        }

        // �ͻ�����Կ��client_secret
        String secret = tokenRequest.getClientSecret();
        if(!commentService.checkClientSecret(secret)) {
            OAuthResponse oAuthResponse = getFailClientSecretResponse();
            return new ResponseEntity<Object>(oAuthResponse.getBody(), HttpStatus.valueOf(oAuthResponse.getResponseStatus()));
        }

        // ��Ȩ��У��
        // grant-type: GrantType.AUTHORIZATION_CODE
        // authorization_code��password��refresh_token��client_credentials
        String authCode = tokenRequest.getParam(OAuth.OAUTH_CODE);
        if(tokenRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.AUTHORIZATION_CODE.toString())){
            if(!commentService.checkAuthCode(authCode)){
                OAuthResponse response = getFailAuthCodeResponse();
                return new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
            }
        }

        // ���ɷ�������
        String accessToken = commentService.genAccessToken();
        commentService.addAccessToken(accessToken, commentService.getUsernameByAuthCode(authCode));

        OAuthResponse response = getSC_OKResponse(accessToken, Constants.A_HOUR);

        return new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
    }
}
