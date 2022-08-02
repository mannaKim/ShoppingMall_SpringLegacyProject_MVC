package com.ezen.shop.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.shop.dao.CartDao;
import com.ezen.shop.dto.CartVO;

@Service
public class CartService {
	@Autowired
	CartDao cdao;

	public void insertCart(CartVO cvo) {
		cdao.insertCart(cvo);
	}

	public HashMap<String, Object> listCart(String id) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		List<CartVO> list = cdao.listCart(id);
		int totalPrice = 0;
		for (CartVO cvo : list)
			totalPrice += cvo.getPrice2() * cvo.getQuantity();
		
		result.put("cartList", list);
		result.put("totalPrice", totalPrice);
		
		return result;
	}

	public void deleteCart(String cseq) {
		cdao.deleteCart(cseq);
	}
}
