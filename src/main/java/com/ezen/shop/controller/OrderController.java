package com.ezen.shop.controller;

import java.util.ArrayList;
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
import com.ezen.shop.dto.OrderVO;
import com.ezen.shop.service.OrderService;

@Controller
public class OrderController {
	@Autowired
	OrderService os;
	
	@RequestMapping("/orderInsert")
	public String order_insert(HttpServletRequest request) {
		int oseq = 0;
		HttpSession session = request.getSession();
		MemberVO mvo = (MemberVO) session.getAttribute("loginUser");
		if(mvo==null) {
			return "member/login";
		}else {
			//카트에 있는 상품들을 orders 테이블로 이동 & 주문번호(oseq) 리턴
			oseq = os.insertOrder(mvo.getId());
		}
		//방금 주문한 주문번호로 리스트 조회 후 화면에 표시하러 이동
		return "redirect:/orderList?oseq="+oseq;
	}
	
	@RequestMapping("/orderList")
	public ModelAndView order_list(@RequestParam("oseq") int oseq, HttpServletRequest request) {
		ModelAndView mav =  new ModelAndView();
		HttpSession session = request.getSession();
		MemberVO mvo = (MemberVO) session.getAttribute("loginUser");
		if(mvo==null) {
			mav.setViewName("member/login");
		}else {
//			List<OrderVO> list = os.listOrderByOseq(oseq);
//			int totalPrice=0;
//			for(OrderVO ovo : list) {
//				totalPrice += ovo.getPrice2() * ovo.getQuantity();
//			}
			// 위 명령을 HashMap으로 받아서 간단하게 표현
			HashMap<String, Object> result = new HashMap<String, Object>();
			result = os.listOrderByOseq(oseq);
			mav.addObject("orderList", (List<OrderVO>)result.get("orderList"));
			mav.addObject("totalPrice", (int)result.get("totalPrice"));
			mav.setViewName("mypage/orderList");
		}
		return mav;
	}
	
	@RequestMapping("/myPage")
	public ModelAndView myPage(HttpServletRequest request) {
		ModelAndView mav =  new ModelAndView();
		HttpSession session = request.getSession();
		MemberVO mvo = (MemberVO) session.getAttribute("loginUser");
		if(mvo==null) {
			mav.setViewName("member/login");
		}else {
			// mypage.jsp에 전달될 고객의 진행중인 주문 리스트 - orderList
			ArrayList<OrderVO> orderList = os.listMyPage(mvo.getId());
			mav.addObject("title", "["+mvo.getName()+"]님의 진행중인 주문 내역");
			mav.addObject("orderList", orderList);
			mav.setViewName("mypage/mypage");
		}
		return mav;
	}
	
	@RequestMapping("/orderDetail")
	public ModelAndView order_detail(HttpServletRequest request, @RequestParam("oseq") int oseq) {
		ModelAndView mav =  new ModelAndView();
		HttpSession session = request.getSession();
		MemberVO mvo = (MemberVO) session.getAttribute("loginUser");
		if(mvo==null) {
			mav.setViewName("member/login");
		}else {
			HashMap<String, Object> result = new HashMap<String, Object>();
			result = os.listOrderByOseq(oseq);
			List<OrderVO> list = (List<OrderVO>)result.get("orderList");
			mav.addObject("orderList", list);
			mav.addObject("totalPrice", (int)result.get("totalPrice"));
			mav.addObject("orderDetail", list.get(0));
			mav.setViewName("mypage/orderDetail");
		}
		return mav;
	}	
	
	@RequestMapping("/orderAll")
	public ModelAndView order_all(HttpServletRequest request) {
		ModelAndView mav =  new ModelAndView();
		HttpSession session = request.getSession();
		MemberVO mvo = (MemberVO) session.getAttribute("loginUser");
		if(mvo==null) {
			mav.setViewName("member/login");
		}else {
			// 고객의 토탈 주문 리스트 - orderList
			ArrayList<OrderVO> orderList = os.listOrderAll(mvo.getId());
			mav.addObject("title", "["+mvo.getName()+"]님의 총 주문 내역");
			mav.addObject("orderList", orderList);
			mav.setViewName("mypage/mypage");
		}
		return mav;
	}
}
