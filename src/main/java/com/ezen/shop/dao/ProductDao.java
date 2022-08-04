package com.ezen.shop.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ezen.shop.dto.ProductVO;
import com.mchange.v2.c3p0.ComboPooledDataSource;

@Repository
public class ProductDao {
	
	// 데이터베이스에서 데이터의 삽입,수정,삭제,조회(CRUD)를 담당할 객체
	private JdbcTemplate template;
	
	
	// -- ComboPooledDataSource 객체를 선언하고 그 객체를 JdbcTemplate의 생성자의 전달인수로 사용합니다.
	/* 방법1 : 스프링컨테이너에 bean을 넣지않고 DBCP와 JDBCTemplate를 사용할 경우 설정 내용
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String userid = "scott";
	private String userpw = "tiger";
	private ComboPooledDataSource dataSource;
	public ProductDao(ComboPooledDataSource dataSource) {
		dataSource = new ComboPooledDataSource();
		try {
			dataSource.setDriverClass(driver);
			dataSource.setJdbcUrl(url);
			dataSource.setUser(userid);
			dataSource.setPassword(userpw);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		template = new JdbcTemplate();
		template.setDataSource(dataSource);
	} */
	
	/* 방법2
	@Autowired
	ComboPooledDataSource dataSource; // 스프링 컨테이너에 수동으로 넣어놓은 DBCP 연결 인스턴스를 꺼내옵니다.
	
	public ProductDao() {
		this.template = new JdbcTemplate(dataSource);
		// dataSource를 이용하여, template 객체를 수동으로 초기화를 해야하는 상황이므로, 생성자에서 초기화 합니다.
	} */
	
	// 방법3 : 위 명령들 보다 더 간단하게 생성자에 직접 매개변수로 넣기
	@Autowired  // Autowired로 bean을 꺼내와서 담는 동작은 생성자의 매개변수에도 가능합니다.
	public ProductDao(ComboPooledDataSource dataSource) {
		this.template = new JdbcTemplate(dataSource);
	}


	public List<ProductVO> getNewList() {
		String sql = "select * from new_pro_view";
			
		// List<ProductVO> list => template에 의한 select 조회 결과를 list에 저장
		// template.query(); => template을 이용한 테이블 조회, 결과는 List<ProductVO>형 리스트
		// List<ProductVO> list = template.query();
		// 익명 클래스 생성자로 객체 생성 : new RowMapper<ProductVO>(){}
		List<ProductVO> list = template.query(sql,new RowMapper<ProductVO>() {
			@Override
			public ProductVO mapRow(ResultSet rs, int rowNum) throws SQLException {
				ProductVO pvo = new ProductVO();
				pvo.setPseq(rs.getInt("pseq"));
				pvo.setName(rs.getString("name"));
				pvo.setPrice2(rs.getInt("price2"));
				pvo.setImage(rs.getString("image"));
				return pvo;
			}
		});
		
		return list;
	}

	
	public List<ProductVO> getBestList() {
		String sql = "select * from best_pro_view";
		
		List<ProductVO> list = template.query(sql,new RowMapper<ProductVO>() {
			@Override
			public ProductVO mapRow(ResultSet rs, int rowNum) throws SQLException {
				ProductVO pvo = new ProductVO();
				pvo.setPseq(rs.getInt("pseq"));
				pvo.setName(rs.getString("name"));
				pvo.setPrice2(rs.getInt("price2"));
				pvo.setImage(rs.getString("image"));
				return pvo;
			}
		});
		
		return list;
	}


	public List<ProductVO> getKindList(String kind) {
		String sql = "select * from product where kind=?";
		List<ProductVO> list = template.query(sql, new RowMapper<ProductVO>() {
			@Override
			public ProductVO mapRow(ResultSet rs, int rowNum) throws SQLException {
				ProductVO pvo = new ProductVO();
				pvo.setPseq(rs.getInt("pseq"));
				pvo.setName(rs.getString("name"));
				pvo.setPrice1(rs.getInt("price1"));
				pvo.setPrice2(rs.getInt("price2"));
				pvo.setImage(rs.getString("image"));
				pvo.setUseyn(rs.getString("useyn"));
				pvo.setBestyn(rs.getString("bestyn"));
				pvo.setIndate(rs.getTimestamp("indate"));
				return pvo;
			}
		}, kind);
		return list;
	}


	public ProductVO getProduct(int pseq) {
		String sql = "select * from product where pseq=?";
		List<ProductVO> list = template.query(sql, new RowMapper<ProductVO>() {
			@Override
			public ProductVO mapRow(ResultSet rs, int rowNum) throws SQLException {
				ProductVO pvo = new ProductVO();
				pvo.setPseq(rs.getInt("pseq"));
				pvo.setName(rs.getString("name"));
				pvo.setPrice1(rs.getInt("price1"));
				pvo.setPrice2(rs.getInt("price2"));
				pvo.setPrice3(rs.getInt("price3"));
				pvo.setKind(rs.getString("kind"));
				pvo.setImage(rs.getString("image"));
				pvo.setUseyn(rs.getString("useyn"));
				pvo.setBestyn(rs.getString("bestyn"));
				pvo.setContent(rs.getString("content"));
				pvo.setIndate(rs.getTimestamp("indate"));
				return pvo;
			}
		}, pseq);
		if(list.size()==0) return null;
		else return list.get(0);
	}
}
