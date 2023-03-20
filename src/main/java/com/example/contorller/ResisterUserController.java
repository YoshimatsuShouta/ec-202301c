package com.example.contorller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.UserInfo;
import com.example.form.ResisterUserForm;
import com.example.service.ResisterUserService;

/**
 * ユーザー情報を操作するコントローラー.
 * 
 * @author nanakono
 *
 */
@Controller
@RequestMapping("/resister")
public class ResisterUserController {

	@Autowired
	private ResisterUserService resisterUserService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * 使用するフォームオブジェクトをリクエストスコープに格納する.
	 * 
	 * @param form フォーム
	 * @return ユーザー登録画面
	 */
	@GetMapping("/toResister")
	public String resister(ResisterUserForm form) {
		return "/materialize-version/register_user";
	};

	/**
	 * ユーザー情報を登録します.
	 * 
	 * @param form   ユーザー情報用フォーム
	 * @param result
	 * @param model  モデル
	 * @return ログイン画面
	 */
	@PostMapping("/resisterUser")

	public String resisterUser(@Validated ResisterUserForm form, BindingResult result, Model model) {
		// * パスワード確認 *//

		if (!form.getConfirmationPassword().equals(form.getPassword()) || form.getConfirmationPassword().equals("")) {
			if (form.getConfirmationPassword().equals("")) {
				result.rejectValue("confirmationPassword", "", "確認用パスワードを入力してください。");
			} else {
				result.rejectValue("confirmationPassword", "", "パスワードと確認用パスワードが不一致です");
			}
		}

		// * メールアドレス重複確認 *//
		UserInfo existUser = resisterUserService.findByEmail(form.getEmail());
		if (existUser != null) {
			result.rejectValue("email", "", "そのメールアドレスはすでに使われています");
		}

		if (result.hasErrors()) {
			return resister(form);
		}
		form.setPassword(passwordEncoder.encode(form.getPassword()));
		// * ユーザー情報登録 *//
		UserInfo user = new UserInfo();
		user.setName(form.getName());
		user.setEmail(form.getEmail());
		user.setPassword(form.getPassword());
		user.setZipcode(form.getZipcode());
		user.setAddress(form.getAddress());
		user.setTelephone(form.getTelephone());
		model.addAttribute("user", user);
		resisterUserService.resisterUser(user);
		return "redirect:/Login/toLogin";
	}

}
