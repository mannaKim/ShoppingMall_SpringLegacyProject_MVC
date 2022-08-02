package com.ezen.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.shop.dao.CartDao;
import com.ezen.shop.dao.OrderDao;
import com.ezen.shop.dto.CartVO;

@Service
public class OrderService {
	@Autowired
	OrderDao odao;
	@Autowired
	CartDao cdao;
	
	public int insertOrder(String id) {
		int oseq = 0;
		// 아이디로 cart 테이블 조회
		List<CartVO> cartList = cdao.listCart(id);
		// orders 테이블에 레코드 삽입
		odao.insertOrders(id);
		// 방금 추가한 orders 테이블 레코드의 oseq 조회
		oseq = odao.lookupMaxOseq();
		// oseq와 조회한 카트 내용으로 order_detail 테이블에 레코드 추가
		for(CartVO cvo : cartList) {
			odao.insertOrderDetail(cvo, oseq);
			cdao.deleteCart(String.valueOf(cvo.getCseq()));
		}
		return oseq;
	}
}
