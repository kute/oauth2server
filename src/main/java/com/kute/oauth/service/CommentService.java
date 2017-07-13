package com.kute.oauth.service;

import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

/**
 * Created by longbai on 2017/7/12.
 */
public interface CommentService {

    /**
     * DB��ѯ�Ƿ���
     * У��ͻ��� clientId
     * @param clientId
     * @return
     */
    public Boolean checkClientId(String clientId);

    public Boolean checkClientSecret(String secret);

    public boolean checkAuthCode(String authCode);

    public String genAccessToken() throws OAuthSystemException;

    String genAuthCode(String responseType) throws OAuthSystemException;

    /**
     * ������Ȩ�����ȡ�û���
     * @param authCode
     * @return
     */
    String getUsernameByAuthCode(String authCode);

    /**
     * �������ƻ�ȡ�û���
     * @param accessToken
     * @return
     */
    String getUsernameByAccessToken(String accessToken);

    /**
     * ��ȡ��Ȩ����/���ƹ���ʱ��
     * @return
     */
    long getExpireIn();

    /**
     * �����Ȩ����
     * @param authCode ��Ȩ����
     * @param username �û���
     */
    public void addAuthCode(String authCode, String username);

    /**
     * ��ӷ�������
     * @param accessToken ��������
     * @param username �û���
     */
    public void addAccessToken(String accessToken, String username);

    boolean checkAccessToken(String accessToken);
}
