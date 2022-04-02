package com.bluebirdme.mes.platform.login;

import com.bluebirdme.mes.core.exception.BusinessException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

/**
 * @author qianchen
 * @date 2020/05/21
 */
public abstract class AbstractLogin implements Login {
    public String loginName;
    public String password;
    public ModelAndView successView;
    public HttpSession session;
    public String language;
    public String successUrl;

    public AbstractLogin(String loginName, String password, String successUrl, HttpSession session, String language) {
        this.loginName = loginName;
        this.password = password;
        this.successUrl = successUrl;
        this.session = session;
        this.language = language;
        this.successView = new ModelAndView(successUrl);
    }

    @Override
    public void error(String errorMessage) throws BusinessException {
        throw new BusinessException(errorMessage);
    }
}
