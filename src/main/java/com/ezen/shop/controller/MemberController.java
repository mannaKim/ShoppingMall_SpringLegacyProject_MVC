package com.ezen.shop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ezen.shop.dto.MemberVO;
import com.ezen.shop.service.MemberService;

@Controller
public class MemberController {
	@Autowired
	MemberService ms;
	
	@RequestMapping("loginForm")
	public String login_form(Model model, HttpServletRequest request) {
		return "member/login";
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public String login(Model model, HttpServletRequest request) {
		String id = request.getParameter("id");
		String pwd = request.getParameter("pwd");
		MemberVO mvo = ms.getMember(id);
		
		String url = "member/login";
		if(mvo==null)
			model.addAttribute("message", "ID가 없습니다.");
		else if(mvo.getPwd()==null)
			model.addAttribute("message", "관리자에게 문의하세요.");
		else if(!mvo.getPwd().contentEquals(pwd))
			model.addAttribute("message", "비밀번호가 틀렸습니다.");
		else if(mvo.getPwd().equals(pwd)) {
			HttpSession session = request.getSession();
			session.setAttribute("loginUser", mvo);
			url = "redirect:/";
		}
		return url;
	}
	
	@RequestMapping("logout")
	public String logout(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute("loginUser");
		return "redirect:/";
	}
	
	@RequestMapping("/contract")
	public String contract(Model model, HttpServletRequest request) {
		return "member/contract";
	}
	
	@RequestMapping(value="joinForm", method=RequestMethod.POST)
	public String join_form(Model model, HttpServletRequest request) {
		return "member/joinForm";
	}
	
	@RequestMapping("idCheckForm")
	public String id_check_form(Model model, HttpServletRequest request) {
		String id = request.getParameter("id");
		MemberVO mvo = ms.getMember(id);
		if(mvo==null) model.addAttribute("result" , -1);
		else model.addAttribute("result", 1);
		model.addAttribute("id",id);
		return "member/idcheck";
	}
	
	@RequestMapping(value="join", method=RequestMethod.POST)
	public String join(Model model, HttpServletRequest request) {
		MemberVO mvo = new MemberVO();
		mvo.setId(request.getParameter("id"));
		mvo.setPwd(request.getParameter("pwd"));
		mvo.setName(request.getParameter("name"));
		mvo.setEmail(request.getParameter("email"));
		mvo.setPhone(request.getParameter("phone"));
		mvo.setZip_num(request.getParameter("zip_num"));
		mvo.setAddress1(request.getParameter("address1"));
		mvo.setAddress2(request.getParameter("address2"));
		mvo.setAddress3(request.getParameter("address3"));
		int result = ms.insertMember(mvo);
		
		if(result==1) model.addAttribute("message","회원가입 완료. 로그인하세요.");
		else model.addAttribute("message","회원가입 실패. 관리자에게 문의하세요.");
		
		return "member/login";
	}
	
	@RequestMapping("memberEditForm")
	public String member_edit_form(Model model, HttpServletRequest request) {
		return "member/memberUpdateForm";
	}
	
	@RequestMapping(value="memberUpdate", method=RequestMethod.POST)
	public String member_update(Model model, HttpServletRequest request) {
		MemberVO mvo = new MemberVO();
		mvo.setId(request.getParameter("id"));
		mvo.setPwd(request.getParameter("pwd"));
		mvo.setName(request.getParameter("name"));
		mvo.setEmail(request.getParameter("email"));
		mvo.setPhone(request.getParameter("phone"));
		mvo.setZip_num(request.getParameter("zip_num"));
		mvo.setAddress1(request.getParameter("address1"));
		mvo.setAddress2(request.getParameter("address2"));
		mvo.setAddress3(request.getParameter("address3"));
		int result = ms.updateMember(mvo);
		
		HttpSession session = request.getSession();
		if(result==1) session.setAttribute("loginUser", mvo);
		
		return "redirect:/";
	}
}
