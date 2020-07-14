/**
 * localStorage存储的KEY
 */
var KEY = {
	/**
	 * 用户ID
	 */
    USER_ID: "uid",
	/**
	 * 用户名
	 */
    USER_NAME: "uname",
	/**
	 * 部门
	 */
    USER_DEPT: "user_dept",
	/**
	 * 唤醒状态
	 */
    WAKE_UP: "wakeup",
	/**
	 * 服务器IP
	 */
    SERVER_IP: "serverip",
	/**
	 * 版本号
	 */
    VERSION: "version"
}

/**
 * 0.正常
 * 1.错库
 * 2.非法入库
 * 3.非法出库
 * 4.超期
 */
var CheckResult = {
	/**
	 * 正常
	 */
    NORMAL: 0,
	/**
	 * 错库
	 */
    WRONG_POSITION: 1,
	/**
	 * 非法入库
	 */
    ILLEGAL_IN: 2,
	/**
	 * 非法出库
	 */
    ILLEGAL_OUT: 3,
	/**
	 * 超期
	 */
    EXPIRED: 4
}
