package com.ezen.shop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ezen.shop.dto.MemberVO;
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
}
