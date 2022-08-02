package com.ezen.shop.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ezen.shop.dto.CartVO;
import com.ezen.shop.dto.OrderVO;
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


	public List<OrderVO> listOrderByOseq(int oseq) {
		String sql = "select * from order_view"
				+ " where oseq=?"
				+ " order by oseq desc";
		List<OrderVO> list = template.query(sql, new RowMapper<OrderVO>() {
			@Override
			public OrderVO mapRow(ResultSet rs, int rowNum) throws SQLException {
				OrderVO ovo = new OrderVO();
				ovo.setOdseq(rs.getInt(1));
				ovo.setOseq(rs.getInt(2));
				ovo.setIndate(rs.getTimestamp(3));
				ovo.setId(rs.getString(4));
				ovo.setMname(rs.getString(5));
				ovo.setZip_num(rs.getString(6));
				ovo.setAddress1(rs.getString(7));
				ovo.setAddress2(rs.getString(8));
				ovo.setAddress3(rs.getString(9));
				ovo.setPhone(rs.getString(10));
				ovo.setPseq(rs.getInt(11));
				ovo.setPname(rs.getString(12));
				ovo.setPrice2(rs.getInt(13));
				ovo.setQuantity(rs.getInt(14));
				ovo.setResult(rs.getString(15));
				return ovo;
			}
		}, oseq);
		return list;
	}


	public List<Integer> selectOseqOrderIng(String id) {
		String sql = "select distinct oseq from order_view"
				+ " where id=? and result='1'"
				+ " order by oseq desc";
		List<Integer> list = template.query(sql, new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt("oseq");
			}
		}, id);
		return list;
	}


	public List<Integer> selectOseqOrderAll(String id) {
		String sql = "select distinct oseq"
				+ " from (select * from order_view order by result)"
				+ " where id=?";
		List<Integer> list = template.query(sql, new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt("oseq");
			}
		}, id);
		return list;
	}
}
