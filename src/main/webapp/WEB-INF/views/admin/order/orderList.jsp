<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/resources/admin/header.jsp" %>
<%@ include file="/resources/admin/sub_menu.jsp" %>
<article>
	<h1>주문리스트</h1>
	<form name="frm" method="post">
		<table style="float:right">
			<tr>
				<td>
					주문자 이름<input type="text" name="key" value="${key}">
					<input class="btn" type="button" name="btn_search" value="검색" onClick="go_search('adminOrderList');"> 
					<input class="btn" type="button" name="btn_total" value="전체보기 " onClick="go_total('adminOrderList');"> 
				</td>
			</tr>
		</table>
		<table id="orderList">
			<tr>
				<th>주문번호(처리여부)</th>
				<th>주문자</th>
				<th>상품명</th>
				<th>수량</th>
				<th>우편번호</th>
				<th>배송지</th>
				<th>전화</th>
				<th>주문일</th>
			</tr>
			<c:forEach items="${orderList}" var="orderVO">
				<tr>
					<td>
						<c:choose>
							<c:when test='${orderVO.result=="1"}'>
								<span style="font-weight: bold; color: blue">${orderVO.oseq}</span>
								(<input type="checkbox" name="result" value="${orderVO.odseq}">미처리)
							</c:when>
							<c:otherwise>
								<span style="font-weight: bold; color: red">${orderVO.oseq}</span>
								(<input type="checkbox" disabled="disabled" checked>처리완료)
							</c:otherwise>
						</c:choose>
					</td>
					<td>${orderVO.mname}(${orderVO.id})</td>
					<td>${orderVO.pname}</td>
					<td>${orderVO.quantity}</td>
					<td>${orderVO.zip_num}</td>
					<td>${orderVO.address1} ${orderVO.address2} ${orderVO.address3}</td>
					<td>${orderVO.phone}</td>
					<td><fmt:formatDate value="${orderVO.indate}" /></td>
				</tr>
			</c:forEach>
		</table>
		<div class="clear"></div>
		<input type="button" class="btn" style="width: 200px;" value="주문처리(입금확인)" 
			onClick="go_order_save()">
	</form>
	<br>
	<jsp:include page="/resources/paging/paging.jsp">
		<jsp:param value="adminOrderList" name="command" />
	</jsp:include>
</article>
<%@ include file="/resources/admin/footer.jsp" %>