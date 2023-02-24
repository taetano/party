package com.example.party.global;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/page")
public class PageController {

	//회원가입페이지 호출
	@RequestMapping("/signup")
	public ModelAndView showSignUpPage() {
		return new ModelAndView("signup"); // signup.html 파일 이름

	}

	//로그인 페이지 호출
	@RequestMapping("/loginPage")
	public ModelAndView showLoginPage() {
		return new ModelAndView("login"); // login.html 파일 이름

	}

	//index 페이지 호출
	@GetMapping("/indexPage")
	public ModelAndView showIndexPage() {
		return new ModelAndView("index"); // signup.html 파일 이름

	}

}
