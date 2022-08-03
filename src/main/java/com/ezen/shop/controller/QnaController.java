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

import com.ezen.shop.dto.MemberVO;
import com.ezen.shop.dto.QnaVO;
import com.ezen.shop.service.QnaService;
import com.ezen.shop.util.Paging;

@Controller
public class QnaController {
	@Autowired
	QnaService qs;
	
	@RequestMapping("/qnaList")
	public ModelAndView qnaList(HttpServletRequest request) {
		ModelAndView mav =  new ModelAndView();
		HttpSession session = request.getSession();
		MemberVO mvo = (MemberVO) session.getAttribute("loginUser");
		if(mvo==null) {
			mav.setViewName("member/login");
		}else {
			// 출력할 페이지 설정(request나 session에 page가 있다면 그 페이지로, 아니면 1페이지로)
			int page = 1;
			if(request.getParameter("page")!=null) {
				page = Integer.parseInt(request.getParameter("page"));
				session.setAttribute("page", page);
			}else if(session.getAttribute("page")!=null) {
				page = (int)session.getAttribute("page");
			}else {
				session.removeAttribute("page");
			}
			HashMap<String, Object> result = qs.listQna(mvo.getId(), page);
			mav.addObject("qnaList", (List<QnaVO>)result.get("qnaList"));
			mav.addObject("paging", (Paging)result.get("paging"));
			mav.setViewName("qna/qnaList");
		}
		return mav;
	}
	
	@RequestMapping("/qnaView")
	public ModelAndView qna_view(HttpServletRequest request, @RequestParam("qseq") int qseq) {
		ModelAndView mav =  new ModelAndView();
		HttpSession session = request.getSession();
		MemberVO mvo = (MemberVO) session.getAttribute("loginUser");
		if(mvo==null) {
			mav.setViewName("member/login");
		}else {
			mav.addObject("qnaVO", qs.getQna(qseq));
			mav.setViewName("qna/qnaView");
		}
		return mav;
	}
	
	@RequestMapping("/qnaWriteForm")
	public String qna_write_form(HttpServletRequest request) {
		HttpSession session = request.getSession();
		MemberVO mvo = (MemberVO) session.getAttribute("loginUser");
		if(mvo==null) {
			return "member/login";
		}
		return "qna/qnaWrite";
	}
	
	@RequestMapping("/qnaWrite")
	public String qna_write(HttpServletRequest request,
			@RequestParam("subject") String subject, @RequestParam("content") String content) {
		HttpSession session = request.getSession();
		MemberVO mvo = (MemberVO) session.getAttribute("loginUser");
		if(mvo==null) {
			return "member/login";
		}else {
			QnaVO qvo = new QnaVO();
			qvo.setSubject(subject);
			qvo.setContent(content);
			qvo.setId(mvo.getId());
			qs.insertQna(qvo);
		}
		return "redirect:/qnaList";
	}
}
