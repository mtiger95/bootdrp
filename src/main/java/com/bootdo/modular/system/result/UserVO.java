package com.bootdo.modular.system.result;

import com.bootdo.modular.system.domain.UserDO;
import lombok.Getter;

/**
 * @author gaoyuzhe
 * @date 2017/12/15.
 */
@Getter
public class UserVO {
    /**
     * 更新的用户对象
     */
    private UserDO userDO = new UserDO();
    /**
     * 旧密码
     */
    private String pwdOld;
    /**
     * 新密码
     */
    private String pwdNew;

    public void setUserDO(UserDO userDO) {
        this.userDO = userDO;
    }

    public void setPwdOld(String pwdOld) {
        this.pwdOld = pwdOld;
    }

    public void setPwdNew(String pwdNew) {
        this.pwdNew = pwdNew;
    }
}
