package com.ezen.shop.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ezen.shop.dto.CartVO;
import com.mchange.v2.c3p0.ComboPooledDataSource;

@Repository
public class CartDao {
	private JdbcTemplate template;
	@Autowired 
	public CartDao(ComboPooledDataSource dataSource) {
		this.template = new JdbcTemplate(dataSource);
	}
	
	
	public void insertCart(CartVO cvo) {
		String sql = "insert into cart(cseq,id,pseq,quantity)"
				+ " values(cart_seq.nextval,?,?,?)";
		template.update(sql, cvo.getId(), cvo.getPseq(), cvo.getQuantity());
	}


	public List<CartVO> listCart(String id) {
		String sql = "select * from cart_view where id=?";
		List<CartVO> list = template.query(sql, new RowMapper<CartVO>() {
			@Override
			public CartVO mapRow(ResultSet rs, int rowNum) throws SQLException {
				CartVO cvo = new CartVO();
				cvo.setCseq(rs.getInt("cseq")); 					//cvo.setCseq(rs.getInt(1));
				cvo.setId(rs.getString("id")); 						//cvo.setResult(rs.getString(2));
				cvo.setMname(rs.getString("mname"));		//cvo.setResult(rs.getString(3));
				cvo.setPseq(rs.getInt("pseq"));						//cvo.setCseq(rs.getInt(4));
				cvo.setPname(rs.getString("pname")); 			//cvo.setResult(rs.getString(5));
				cvo.setQuantity(rs.getInt("quantity")); 			//cvo.setCseq(rs.getInt(6));
				cvo.setPrice2(rs.getInt("price2")); 				//cvo.setCseq(rs.getInt(7));
				cvo.setResult(rs.getString("result")); 			//cvo.setResult(rs.getString(8));
				cvo.setIndate(rs.getTimestamp("indate")); 	//cvo.setIndate(rs.getTimestamp(9));
				return cvo;
			}
		}, id);
		return list;
	}


	public void deleteCart(String cseq) {
		String sql = "delete from cart where cseq=?";
		template.update(sql, cseq); 	// == template.update(sql, Integer.parseInt(cseq));
	}
}
