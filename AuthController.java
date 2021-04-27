package jp.co.internous.sugar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import jp.co.internous.sugar.model.domain.MstUser;
import jp.co.internous.sugar.model.form.UserForm;
import jp.co.internous.sugar.model.mapper.MstUserMapper;
import jp.co.internous.sugar.model.mapper.TblCartMapper;
import jp.co.internous.sugar.model.session.LoginSession;

@Controller
@RequestMapping("/sugar/auth")
public class AuthController {
	@Autowired
	private LoginSession loginSession;
	Gson gson = new Gson();
	
	@Autowired
	private MstUserMapper userMap;
	
	@Autowired
	private TblCartMapper cartMap;

	
	@ResponseBody
	@PostMapping(value = "/login")
	public String login(@RequestBody UserForm form) {
		MstUser user = userMap.findByUserNameAndPassword(form.getUserName(), form.getPassword());
		if(user != null){
			//未ログイン状態でのカート情報があれば現在のIDへ移行
			if(cartMap.findCountByUserId(loginSession.getGuestUserId()) > 0 && loginSession.getGuestUserId() != 0) {
				cartMap.updateUserId(user.getId(),loginSession.getGuestUserId());
			}
			loginSession.setUserId(user.getId());
			loginSession.setPassword(user.getPassword());
			loginSession.setUserName(user.getUserName());
			loginSession.setLoginFlag(true);
			loginSession.setGuestUserId(0);
		}else {
			loginSession.setUserId(0);
			loginSession.setPassword(null);
			loginSession.setUserName(null);
			loginSession.setLoginFlag(false);
		}
		
		return gson.toJson(user);
	}

	@ResponseBody
	@PostMapping(value = "/logout")
	public String logout(@RequestBody UserForm form) {
		loginSession.setUserId(0);
		loginSession.setPassword(null);
		loginSession.setUserName(null);
		loginSession.setLoginFlag(false);
		loginSession.setGuestUserId(0);
		
		return "";
	}
	
	@ResponseBody
	@PostMapping(value = "/resetPassword")
	public String resetPassword(@RequestBody UserForm form) {
		MstUser user = userMap.findByUserNameAndPassword(form.getUserName(), form.getPassword());
		
		if(user == null) {
			return "現在のパスワードが正しくありません。";
		}
		if(user.getPassword().equals(form.getNewPassword())) {
			return "現在のパスワードと同一文字列が出力されました。";
		}
		if(!form.getNewPassword().equals(form.getNewPasswordConfirm())) {
			return "新パスワードと確認用パスワードが一致しません。";
		}	
		//パスワード再設定
		userMap.updatePassword(form.getUserName(),form.getNewPassword());
		loginSession.setPassword(form.getNewPassword());
		
		return "パスワードが再設定されました。";
	}
}
