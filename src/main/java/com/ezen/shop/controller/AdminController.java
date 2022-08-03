package com.ezen.shop.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.shop.dto.ProductVO;
import com.ezen.shop.service.AdminService;
import com.ezen.shop.util.Paging;

@Controller
public class AdminController {
	@Autowired
	AdminService as;
	
	@RequestMapping("/admin")
	public String admin() {
		return "admin/adminLoginForm";
	}
	
	@RequestMapping("/adminLogin")
	public ModelAndView admin_login(HttpServletRequest request,
			@RequestParam("workId") String workId, @RequestParam("workPwd") String workPwd) {
		ModelAndView mav = new ModelAndView();
		
		int result = as.loginCheck(workId, workPwd);
		// result 값이 1이면 로그인, 0이면 비밀번호 오류, -1이면 아이디 없음
		
		if(result==1) {
			HttpSession session = request.getSession();
			session.setAttribute("workId", workId);
			mav.setViewName("redirect:/productList");
		}else if(result==0) {
			mav.addObject("message", "비밀번호를 확인하세요.");
			mav.setViewName("admin/adminLoginForm");
		}else if(result==-1) {
			mav.addObject("message", "아이디를 확인하세요.");
			mav.setViewName("admin/adminLoginForm");
		}
		return mav;
	}
	
	@RequestMapping("/productList")
	public ModelAndView product_list(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		String id = (String)session.getAttribute("workId");
		if(id==null){
			mav.setViewName("redirect:/admin");
		}else {
			int page = 1;
			if(request.getParameter("page")!=null) {
				page = Integer.parseInt(request.getParameter("page"));
				session.setAttribute("page", page);
			}else if(session.getAttribute("page")!=null) {
				page = (int)session.getAttribute("page");
			}else {
				session.removeAttribute("page");
			}
			HashMap<String, Object> resultMap = as.productList(page);
			mav.addObject("productList", (List<ProductVO>)resultMap.get("productList"));
			mav.addObject("paging", (Paging)resultMap.get("paging"));
			mav.setViewName("admin/product/productList");
		}
		return mav;
	}
}
