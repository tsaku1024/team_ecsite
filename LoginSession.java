package jp.co.internous.sugar.model.session;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value="session",proxyMode=ScopedProxyMode.TARGET_CLASS)
public class LoginSession implements Serializable{
	private static final long serialVersionUID = -3681748332995672288L;
	private int userId;
	private int guestUserId;
	private String userName;
	private String password;
	private boolean loginFlag;

	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getGuestUserId() {
		return guestUserId;
	}
	public void setGuestUserId(int guestUserId) {
		this.guestUserId = guestUserId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isLoginFlag() {
		return loginFlag;
	}
	public void setLoginFlag(boolean loginFlag) {
		this.loginFlag = loginFlag;
	}
}
