package com.ezen.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.ezen.shop.service.AdminService;

@Controller
public class AdminController {
	@Autowired
	AdminService as;
}
