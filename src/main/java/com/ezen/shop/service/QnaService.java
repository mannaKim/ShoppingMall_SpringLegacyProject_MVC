package com.ezen.shop.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.shop.dao.QnaDao;
import com.ezen.shop.dto.QnaVO;
import com.ezen.shop.util.Paging;

@Service
public class QnaService {
	@Autowired
	QnaDao qdao;

	public HashMap<String, Object> listQna(String id, int page) {
		// qnaList와 paging을 받아갈 HashMap
		HashMap<String, Object> result = new HashMap<String, Object>();
		// paging 객체 설정
		Paging paging = new Paging();
		paging.setDisplayRow(5); 		//한페이지에 게시글 5개
		paging.setDisplayPage(5); 	//보이는 페이지목록 5개 <1 2 3 4 5>
		paging.setPage(page);
		paging.setTotalCount(qdao.getAllCount(id));
		// listQna메소드에 paging과 id를 전달해서 page에 보여질 정보만 읽어오기
		List<QnaVO> list = qdao.listQna(paging, id);
		
		result.put("qnaList", list);
		result.put("paging", paging);
		return result;
	}

	public QnaVO getQna(int qseq) {
		return qdao.getQna(qseq);
	}

	public void insertQna(QnaVO qvo) {
		qdao.insertQna(qvo);
	}
}
