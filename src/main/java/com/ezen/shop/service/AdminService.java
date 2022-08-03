package com.ezen.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.shop.dao.AdminDao;

@Service
public class AdminService {
	@Autowired
	AdminDao adao;
}
