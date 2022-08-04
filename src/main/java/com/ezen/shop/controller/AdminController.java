package com.ezen.shop.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.shop.dto.MemberVO;
import com.ezen.shop.dto.OrderVO;
import com.ezen.shop.dto.ProductVO;
import com.ezen.shop.dto.QnaVO;
import com.ezen.shop.service.AdminService;
import com.ezen.shop.service.ProductService;
import com.ezen.shop.util.Paging;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

@Controller
public class AdminController {
	@Autowired
	AdminService as;
	
	@Autowired
	ServletContext context;
	
	@Autowired
	ProductService ps;
	
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
			if(request.getParameter("first")!=null) {
				session.removeAttribute("page");
				session.removeAttribute("key");
			}
			
			String key = "";
			if(request.getParameter("key")!=null) {
				key = request.getParameter("key");
				session.setAttribute("key", key);
			}else if(session.getAttribute("key")!=null) {
				key = (String)session.getAttribute("key");
			}else {
				session.removeAttribute("key");
			}
			
			int page = 1;
			if(request.getParameter("page")!=null) {
				page = Integer.parseInt(request.getParameter("page"));
				session.setAttribute("page", page);
			}else if(session.getAttribute("page")!=null) {
				page = (int)session.getAttribute("page");
			}else {
				session.removeAttribute("page");
			}
			
			HashMap<String, Object> resultMap = as.productList(page, key);
			mav.addObject("productList", (List<ProductVO>)resultMap.get("productList"));
			mav.addObject("paging", (Paging)resultMap.get("paging"));
			mav.addObject("key", key);
			mav.setViewName("admin/product/productList");
		}
		return mav;
	}
	
	@RequestMapping("/productWriteForm")
	public ModelAndView product_write_form(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		String [] kindList = {"Heels", "Boots", "Sandals", "Sneakers", "Slippers", "On Sale"};
		mav.addObject("kindList", kindList);
		mav.setViewName("admin/product/productWriteForm");
		return mav;
	}
	
	@RequestMapping(value="/productWrite", method=RequestMethod.POST)
	public String product_write(HttpServletRequest request) {
		String savePath = context.getRealPath("resources/product_images");
		ProductVO pvo = new ProductVO();
		
		try {
			MultipartRequest multi = new MultipartRequest(
					request, savePath, 5*1024*1024, "UTF-8", new DefaultFileRenamePolicy()
			);
			pvo.setName(multi.getParameter("name"));
			pvo.setKind(multi.getParameter("kind"));
			pvo.setPrice1(Integer.parseInt(multi.getParameter("price1")));
			pvo.setPrice2(Integer.parseInt(multi.getParameter("price2")));
			pvo.setPrice3(Integer.parseInt(multi.getParameter("price3")));
			pvo.setContent(multi.getParameter("content"));
			pvo.setImage(multi.getFilesystemName("image"));
			as.insertProduct(pvo);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "redirect:/productList"; 
	}
	
	@RequestMapping("/adminProductDetail")
	public ModelAndView admin_product_detail(HttpServletRequest request,
			@RequestParam("pseq") int pseq) {
		ModelAndView mav = new ModelAndView();
		
		//ProductService를 @Autowired 해서 전에 사용했던 메서드 사용
		ProductVO pvo = ps.getProduct(pseq);
		String [] kindList = {"", "Heels", "Boots", "Sandals", "Sneakers", "Slippers", "On Sale"};
		
		mav.addObject("productVO", pvo);
		mav.addObject("kind", kindList[Integer.parseInt(pvo.getKind())]);
		mav.setViewName("admin/product/productDetail");
		
		return mav;
	}
	
	@RequestMapping("/productUpdateForm")
	public ModelAndView product_update_form(HttpServletRequest request,
			@RequestParam("pseq") int pseq) {
		ModelAndView mav = new ModelAndView();
		ProductVO pvo = ps.getProduct(pseq);
		String [] kindList = {"Heels", "Boots", "Sandals", "Sneakers", "Slippers", "On Sale"};
		
		mav.addObject("productVO", pvo);
		mav.addObject("kindList", kindList);
		mav.setViewName("admin/product/productUpdate");
		
		return mav;
	}
	
	@RequestMapping(value="/productUpdate", method=RequestMethod.POST)
	public String product_update(HttpServletRequest request) {
		String savePath = context.getRealPath("resources/product_images");
		ProductVO pvo = new ProductVO();
		// 업데이트 이후 productDetail로 돌아가기 위해 전달할 상품번호(pseq)
		int pseq = 0;
		try {
			MultipartRequest multi = new MultipartRequest(
					request, savePath, 5*1024*1024, "UTF-8", new DefaultFileRenamePolicy()
			);
			pseq = Integer.parseInt(multi.getParameter("pseq"));
			pvo.setPseq(pseq);
			pvo.setName(multi.getParameter("name"));
			pvo.setKind(multi.getParameter("kind"));
			pvo.setPrice1(Integer.parseInt(multi.getParameter("price1")));
			pvo.setPrice2(Integer.parseInt(multi.getParameter("price2")));
			pvo.setPrice3(Integer.parseInt(multi.getParameter("price3")));
			pvo.setContent(multi.getParameter("content"));
			
			// productUpdate.jsp에서 checkbox로 얻은 값이 있다면 "y", 없다면 "n"으로 설정
			if(multi.getParameter("useyn")==null) pvo.setUseyn("n");
			else pvo.setUseyn("y");
			if(multi.getParameter("bestyn")==null) pvo.setBestyn("n");
			else pvo.setBestyn("y");
			
			// productUpdate.jsp에서 상품 이미지가 업데이트 되었다면 getFilesystemName으로 받고, 
			// 아니라면 따로 히든으로 보낸 "oldfilename"을 getParameter로 받아 설정
			if(multi.getFilesystemName("image")==null) 
				pvo.setImage(multi.getParameter("oldfilename"));
			else 
				pvo.setImage(multi.getFilesystemName("image"));
			
			as.updateProduct(pvo);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "redirect:/adminProductDetail?pseq="+pseq; 
	}
	
	
	@RequestMapping("/adminOrderList")
	public ModelAndView admin_order_list(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		if(session.getAttribute("workId")==null){
			mav.setViewName("redirect:/admin");
		}else {
			if(request.getParameter("first")!=null) {
				session.removeAttribute("page");
				session.removeAttribute("key");
			}
			
			String key = "";
			if(request.getParameter("key")!=null) {
				key = request.getParameter("key");
				session.setAttribute("key", key);
			}else if(session.getAttribute("key")!=null) {
				key = (String)session.getAttribute("key");
			}else {
				session.removeAttribute("key");
			}
			
			int page = 1;
			if(request.getParameter("page")!=null) {
				page = Integer.parseInt(request.getParameter("page"));
				session.setAttribute("page", page);
			}else if(session.getAttribute("page")!=null) {
				page = (int)session.getAttribute("page");
			}else {
				session.removeAttribute("page");
			}
			
			HashMap<String, Object> resultMap = as.orderList(page, key);
			mav.addObject("orderList", (List<OrderVO>)resultMap.get("orderList"));
			mav.addObject("paging", (Paging)resultMap.get("paging"));
			mav.addObject("key", key);
			mav.setViewName("admin/order/orderList");
		}
		return mav;
	}
	
	@RequestMapping("/orderUpdateResult")
	public String order_update_result(@RequestParam("result") int [] resultArr) {
		as.updateOrderResult(resultArr);
		return "redirect:/adminOrderList";
	}
	
	
	@RequestMapping("/memberList")
	public ModelAndView member_list(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		if(session.getAttribute("workId")==null){
			mav.setViewName("redirect:/admin");
		}else {
			if(request.getParameter("first")!=null) {
				session.removeAttribute("page");
				session.removeAttribute("key");
			}
			
			String key = "";
			if(request.getParameter("key")!=null) {
				key = request.getParameter("key");
				session.setAttribute("key", key);
			}else if(session.getAttribute("key")!=null) {
				key = (String)session.getAttribute("key");
			}else {
				session.removeAttribute("key");
			}
			
			int page = 1;
			if(request.getParameter("page")!=null) {
				page = Integer.parseInt(request.getParameter("page"));
				session.setAttribute("page", page);
			}else if(session.getAttribute("page")!=null) {
				page = (int)session.getAttribute("page");
			}else {
				session.removeAttribute("page");
			}
			
			HashMap<String, Object> resultMap = as.memberList(page, key);
			mav.addObject("memberList", (List<MemberVO>)resultMap.get("memberList"));
			mav.addObject("paging", (Paging)resultMap.get("paging"));
			mav.addObject("key", key);
			mav.setViewName("admin/member/memberList");
		}
		return mav;
	}
	
	@RequestMapping("/adminQnaList")
	public ModelAndView admin_qna_list(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		if(session.getAttribute("workId")==null){
			mav.setViewName("redirect:/admin");
		}else {
			if(request.getParameter("first")!=null) {
				session.removeAttribute("page");
				session.removeAttribute("key");
			}
			
			String key = "";
			if(request.getParameter("key")!=null) {
				key = request.getParameter("key");
				session.setAttribute("key", key);
			}else if(session.getAttribute("key")!=null) {
				key = (String)session.getAttribute("key");
			}else {
				session.removeAttribute("key");
			}
			
			int page = 1;
			if(request.getParameter("page")!=null) {
				page = Integer.parseInt(request.getParameter("page"));
				session.setAttribute("page", page);
			}else if(session.getAttribute("page")!=null) {
				page = (int)session.getAttribute("page");
			}else {
				session.removeAttribute("page");
			}
			
			HashMap<String, Object> resultMap = as.qnaList(page, key);
			mav.addObject("qnaList", (List<QnaVO>)resultMap.get("qnaList"));
			mav.addObject("paging", (Paging)resultMap.get("paging"));
			mav.addObject("key", key);
			mav.setViewName("admin/qna/qnaList");
		}
		return mav;
	}
}
