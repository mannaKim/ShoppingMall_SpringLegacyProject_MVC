package com.ezen.shop.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ezen.shop.dto.ProductVO;
import com.ezen.shop.service.ProductService;

@Controller
public class ProductController {
	@Autowired
	ProductService ps;
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String index (HttpServletRequest request, Model model) {		
//		List<ProductVO> nlist = ps.getNewList();
//		List<ProductVO> blist = ps.getBestList();
//		model.addAttribute("newProductList", nlist);
//		model.addAttribute("bestProductList", blist);
		model.addAttribute("newProductList", ps.getNewList());
		model.addAttribute("bestProductList", ps.getBestList());
		return "index";
	}
}
