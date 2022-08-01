package com.ezen.shop.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.shop.dto.CartVO;
import com.ezen.shop.dto.MemberVO;
import com.ezen.shop.service.CartService;

@Controller
public class CartController {
	@Autowired
	CartService cs;
	
	@RequestMapping("cartInsert")
	public String cartInsert(
			//RequestParam을 사용하고 있지만, 로그인 체크를 위해 HttpServletRequest도 매개변수로 선언합니다.
			HttpServletRequest request,
			@RequestParam("pseq") int pseq,
			@RequestParam("quantity") int quantity) { 
		//현재 메서드는 카트에 물건을 넣고, cartList 리퀘스트로 이동할 예정이므로,
		//Model이 필요치 않은 상황입니다.
		//따라서 ModelAndView는 사용하지 않고, String으로 리턴해서 이동합니다.
		
		HttpSession session = request.getSession();
		MemberVO mvo = (MemberVO) session.getAttribute("loginUser");
		
		if(mvo==null) {
			return "member/login";
		}else {
			CartVO cvo = new CartVO();
			cvo.setId(mvo.getId());
			cvo.setPseq(pseq);
			cvo.setQuantity(quantity);
			
			cs.insertCart(cvo);
			return "redirect:/cartList";
		}
	}
	
	@RequestMapping("cartList")
	public ModelAndView cart_list(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		
		HttpSession session = request.getSession();
		MemberVO mvo = (MemberVO) session.getAttribute("loginUser");
		
		if(mvo==null) {
			mav.setViewName("member/login");
		}else {
			List<CartVO> list = cs.listCart(mvo.getId());
			int totalPrice = 0;
			for(CartVO cvo : list)
				totalPrice += cvo.getPrice2() * cvo.getQuantity();
			
			mav.addObject("totalPrice", totalPrice);
			mav.addObject("cartList", list);
			mav.setViewName("mypage/cartList");
		}
		return mav;
	}
	
}
