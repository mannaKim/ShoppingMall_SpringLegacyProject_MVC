package com.ezen.shop.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.shop.dao.AdminDao;
import com.ezen.shop.dto.ProductVO;
import com.ezen.shop.dto.QnaVO;
import com.ezen.shop.util.Paging;

@Service
public class AdminService {
	@Autowired
	AdminDao adao;

	public int loginCheck(String workId, String workPwd) {
		return adao.loginCheck(workId, workPwd);
	}

	public HashMap<String, Object> productList(int page, String key) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		Paging paging = new Paging();
		paging.setPage(page);
		paging.setTotalCount(adao.getAllCount("product", "name", key));
		
		result.put("productList", adao.productList(paging, key));
		result.put("paging", paging);
		return result;
	}
	
	public void insertProduct(ProductVO pvo) {
		adao.insertProduct(pvo);
	}

	public void updateProduct(ProductVO pvo) {
		adao.updateProduct(pvo);
	}

	public HashMap<String, Object> orderList(int page, String key) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		Paging paging = new Paging();
		paging.setPage(page);
		paging.setTotalCount(adao.getAllCount("order_view", "mname", key));
		
		result.put("orderList", adao.orderList(paging, key));
		result.put("paging", paging);
		return result;
	}

	public HashMap<String, Object> memberList(int page, String key) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		Paging paging = new Paging();
		paging.setPage(page);
		paging.setTotalCount(adao.getAllCount("member", "name", key));
		
		result.put("memberList", adao.memberList(paging, key));
		result.put("paging", paging);
		return result;
	}

	public HashMap<String, Object> qnaList(int page, String key) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		Paging paging = new Paging();
		paging.setPage(page);
		paging.setTotalCount(adao.getAllCountForTwoField("qna", "subject", "content", key));
		
		result.put("qnaList", adao.qnaList(paging, key));
		result.put("paging", paging);
		return result;
	}

	public void updateOrderResult(int[] resultArr) {
		for(int odseq : resultArr) {
			adao.updateOrderResult(odseq);
		}
	}

	public void updateQna(QnaVO qvo) {
		adao.updateQna(qvo);
	}
}
