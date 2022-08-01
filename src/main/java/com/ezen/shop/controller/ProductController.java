package com.ezen.shop.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.shop.service.ProductService;

@Controller
public class ProductController {
	@Autowired
	ProductService ps;
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String index(HttpServletRequest request, Model model) {		
//		List<ProductVO> nlist = ps.getNewList();
//		List<ProductVO> blist = ps.getBestList();
//		model.addAttribute("newProductList", nlist);
//		model.addAttribute("bestProductList", blist);
		model.addAttribute("newProductList", ps.getNewList());
		model.addAttribute("bestProductList", ps.getBestList());
		return "index";
	}
	
	@RequestMapping("category")
	public ModelAndView category(/* HttpServletRequest request, Model model */ 
			@RequestParam("kind") String kind) {
		// 매개변수 위치에 있는 @RequestParam("kind") String kind는
		// 변수 선언과 동시에 전달되는 파라미터를 받아서 저장하는 역할을 합니다.
		// HttpServletRequest request 선언과 String kind = request.getParameter("kind");는 생략이 가능합니다.
		// 전달된 파라미터가 10개면 매개변수가 @RequestParam("")의 형태로 10개가 선언되어 사용됩니다.
		// int형 변수도 선언이 가능합니다.
		
		// 단점 1. 문자데이터(a b c..)가 전달되는데 정수형 변수를 사용하면 안됩니다.
		// 단점 2. 기본 특성상 null값이 전달되면 오류가 발생합니다. null을 허용하려면 아래와 같이 명시해줍니다.
		// @RequestParam(value="kind", required=false) int kind
		
		ModelAndView mav = new ModelAndView(); 
		// ModelAndView : model에 addAttribute로 저장할 내용과 이동할 jsp 파일의 이름을 동시에 저장하고
		// 리턴하여 전달값과 이동페이지를 한번에 다룰 수 있게 하는 클래스
		
		// 기존방식
//		List<ProductVO> list = ps.getKindList(kind);
//		model.addAttribute("productKindList", list);
//		return "product/productKind";
		
		mav.addObject("productKindList", ps.getKindList(kind));
		mav.setViewName("product/productKind");
		
		return mav;
	}
	
	@RequestMapping("/productDetail")
	public ModelAndView product_detail(@RequestParam("pseq") int pseq) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("productVO", ps.getProduct(pseq));
		mav.setViewName("product/productDetail");
		return mav;
	}
}
