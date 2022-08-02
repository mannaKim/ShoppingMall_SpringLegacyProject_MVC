package com.ezen.shop.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.shop.dao.CartDao;
import com.ezen.shop.dao.OrderDao;
import com.ezen.shop.dto.CartVO;
import com.ezen.shop.dto.OrderVO;

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

	public HashMap<String, Object> listOrderByOseq(int oseq) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		List<OrderVO> list = odao.listOrderByOseq(oseq);
		int totalPrice = 0;
		for(OrderVO ovo : list)
			totalPrice += ovo.getPrice2() * ovo.getQuantity();
		
		result.put("orderList", list);
		result.put("totalPrice", totalPrice);
		
		return result;
	}

	public ArrayList<OrderVO> listMyPage(String id) {
		ArrayList<OrderVO> orderList = new ArrayList<OrderVO>();
		// 진행중인 주문내역의 주문번호(oseq)들을 조회
		List<Integer> oseqList = odao.selectOseqOrderIng(id);
		
		// 위에서 얻은 주문번호(oseq)마다 주문내역(order_view에서) 조회
		for(Integer oseq : oseqList) {
			List<OrderVO> orderListIng = odao.listOrderByOseq(oseq);
			
			//조회한 주문 상품 리스트 중 첫번째 상품 꺼냄
			OrderVO ovo = orderListIng.get(0);
			ovo.setPname(ovo.getPname()+" 포함 "+orderListIng.size()+" 건"); // 상품명 변경
			int totalPrice = 0;
			for(OrderVO ovo1 : orderListIng)
				totalPrice += ovo1.getPrice2() * ovo1.getQuantity();
			ovo.setPrice2(totalPrice); // 리스트의 상품 총금액 계산해서 첫번째 상품의 가격으로 변경
			orderList.add(ovo); // 리턴될 리스트에 추가
		}
		return orderList;
	}

	public ArrayList<OrderVO> listOrderAll(String id) {
		ArrayList<OrderVO> orderList = new ArrayList<OrderVO>();
		List<Integer> oseqList = odao.selectOseqOrderAll(id);

		for(Integer oseq : oseqList) {
			List<OrderVO> orderListIng = odao.listOrderByOseq(oseq);
			OrderVO ovo = orderListIng.get(0);
			ovo.setPname(ovo.getPname()+" 포함 "+orderListIng.size()+" 건");
			int totalPrice = 0;
			for(OrderVO ovo1 : orderListIng)
				totalPrice += ovo1.getPrice2() * ovo1.getQuantity();
			ovo.setPrice2(totalPrice);
			orderList.add(ovo);
		}
		return orderList;
	}
}
