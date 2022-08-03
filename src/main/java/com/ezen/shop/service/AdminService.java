package com.ezen.shop.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.shop.dao.AdminDao;

@Service
public class AdminService {
	@Autowired
	AdminDao adao;

	public int loginCheck(String workId, String workPwd) {
		return adao.loginCheck(workId, workPwd);
	}

	public HashMap<String, Object> productList() {
		HashMap<String, Object> result = new HashMap<String, Object>();
		result.put("productList", adao.productList());
		return result;
	}
}
