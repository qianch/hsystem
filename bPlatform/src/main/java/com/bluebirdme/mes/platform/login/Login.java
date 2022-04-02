package com.bluebirdme.mes.platform.login;

import com.bluebirdme.mes.core.exception.BusinessException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public interface Login {
    /**
     * 登录
     * @throws NoSuchAlgorithmException
     * @throws BusinessException
     * @throws IOException
     */
    void doLogin() throws NoSuchAlgorithmException, BusinessException, IOException;

    /**
     * 错误
     * @param s
     * @throws BusinessException
     */
    void error(String s) throws BusinessException;

    /**
     * 成功
     */
    void success();
}
