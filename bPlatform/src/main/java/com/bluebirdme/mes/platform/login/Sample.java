package com.bluebirdme.mes.platform.login;

import javax.servlet.http.HttpSession;

/**
 * @author qianchen
 * @date 2020/05/21
 */

public class Sample extends AbstractLogin {
    public String cardNo;

    public Sample(String loginName, String password, String successUrl, HttpSession session, String cardNo) {
        super(loginName, password, successUrl, session, "zh_CN");
        this.cardNo = cardNo;
    }

    @Override
    public void doLogin() {
    }

    @Override
    public void success() {
    }
}
