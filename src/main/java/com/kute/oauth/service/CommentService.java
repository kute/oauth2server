package com.kute.oauth.service;

import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

/**
 * Created by longbai on 2017/7/12.
 */
public interface CommentService {

    /**
     * DB查询是否有
     * 校验客户度 clientId
     * @param clientId
     * @return
     */
    public Boolean checkClientId(String clientId);

    public Boolean checkClientSecret(String secret);

    public boolean checkAuthCode(String authCode);

    public String genAccessToken() throws OAuthSystemException;

    String genAuthCode(String responseType) throws OAuthSystemException;

    /**
     * 根据授权代码获取用户名
     * @param authCode
     * @return
     */
    String getUsernameByAuthCode(String authCode);

    /**
     * 根据令牌获取用户名
     * @param accessToken
     * @return
     */
    String getUsernameByAccessToken(String accessToken);

    /**
     * 获取授权代码/令牌过期时间
     * @return
     */
    long getExpireIn();

    /**
     * 添加授权代码
     * @param authCode 授权代码
     * @param username 用户名
     */
    public void addAuthCode(String authCode, String username);

    /**
     * 添加访问令牌
     * @param accessToken 访问令牌
     * @param username 用户名
     */
    public void addAccessToken(String accessToken, String username);

    boolean checkAccessToken(String accessToken);
}
