package com.ezen.shop.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.shop.dao.AdminDao;
import com.ezen.shop.util.Paging;

@Service
public class AdminService {
	@Autowired
	AdminDao adao;

	public int loginCheck(String workId, String workPwd) {
		return adao.loginCheck(workId, workPwd);
	}

	public HashMap<String, Object> productList(int page) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		Paging paging = new Paging();
		paging.setPage(page);
		paging.setTotalCount(adao.getAllCount("product"));
		
		result.put("productList", adao.productList(paging));
		result.put("paging", paging);
		return result;
	}
}
