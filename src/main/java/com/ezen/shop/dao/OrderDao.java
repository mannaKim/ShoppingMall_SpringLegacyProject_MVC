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
public class OrderDao {
	private JdbcTemplate template;
	@Autowired 
	public OrderDao(ComboPooledDataSource dataSource) {
		this.template = new JdbcTemplate(dataSource);
	}
	
	
	public void insertOrders(String id) {
		String sql = "insert into orders(oseq,id) values(orders_seq.nextVal,?)";
		template.update(sql, id);
	}


	public int lookupMaxOseq() {
		String sql = "select max(oseq) from orders";
		List<Integer> list = template.query(sql, new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				int oseq = rs.getInt(1);
				return oseq;
			}
		});
		return list.get(0);
	}


	public void insertOrderDetail(CartVO cvo, int oseq) {
		String sql = "insert into order_detail(odseq,oseq,pseq,quantity)"
				+ " values(order_detail_seq.nextval,?,?,?)";
		template.update(sql, oseq, cvo.getPseq(), cvo.getQuantity());
	}
}
