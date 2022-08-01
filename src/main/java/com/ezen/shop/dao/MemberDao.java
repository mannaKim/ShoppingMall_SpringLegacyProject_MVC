package com.ezen.shop.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ezen.shop.dto.MemberVO;
import com.mchange.v2.c3p0.ComboPooledDataSource;

@Repository
public class MemberDao {
	private JdbcTemplate template;
	@Autowired
	public MemberDao(ComboPooledDataSource dataSource) {
		this.template = new JdbcTemplate(dataSource);
	}
	
	public MemberVO getMember(String id) {
		String sql = "select * from member where id=?";
		List<MemberVO> list = template.query(sql, new RowMapper<MemberVO>() {
			@Override
			public MemberVO mapRow(ResultSet rs, int rowNum) throws SQLException {
				MemberVO mvo = new MemberVO();
				mvo.setId(rs.getString("id"));
				mvo.setPwd(rs.getString("pwd"));
				mvo.setName(rs.getString("name"));
				mvo.setEmail(rs.getString("email"));
				mvo.setPhone(rs.getString("phone"));
				mvo.setZip_num(rs.getString("zip_num"));
				mvo.setAddress1(rs.getString("address1"));
				mvo.setAddress2(rs.getString("address2"));
				mvo.setAddress3(rs.getString("address3"));
//				mvo.setUseyn(rs.getString("useyn"));
//				mvo.setIndate(rs.getTimestamp("indate"));
				return mvo;
			}
		}, id);
		// ?에 해당하는 값은 template.query() 안에 명시해줍니다.
		// ?가 두 개 이상이라면 ,(컴마)로 구분해서 순서대로 명시합니다.
		
		if(list.size()==0) return null;
		else return list.get(0); 
		// 한 명의 멤버 데이터를 리턴받아야 하는 상황이라도, 결과를 리스트로 도출해서 첫번째 값을 리턴합니다.
	}
	
	public int insertMember(MemberVO mvo) {
		String sql = "insert into member(id,pwd,name,email,phone,zip_num,address1,address2,address3)"
				+ " values(?,?,?,?,?,?,?,?,?)";
		int result = template.update(sql,mvo.getId(),mvo.getPwd(),
				mvo.getName(),mvo.getEmail(),mvo.getPhone(),
				mvo.getZip_num(),mvo.getAddress1(),mvo.getAddress2(),mvo.getAddress3());
		return result;
	}

	public int updateMember(MemberVO mvo) {
		String sql = "update member set pwd=?, name=?, email=?, phone=?,"
				+ " zip_num=?, address1=?, address2=?, address3=?"
				+ " where id=?";
		int result = template.update(sql, mvo.getPwd(),mvo.getName(),mvo.getEmail(),mvo.getPhone(),
				mvo.getZip_num(),mvo.getAddress1(),mvo.getAddress2(),mvo.getAddress3(),
				mvo.getId());
		return result;
	}
}
