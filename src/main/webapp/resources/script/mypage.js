function go_cart(){
	if(document.formm.quantity.value==""){
		alert("수량을 입력하세요");
		document.formm.quantity.focus();
	}else{
		document.formm.action="shop.do?command=cartInsert";
		document.formm.submit();
	}
}

function go_cart_delete(){
	//자바스크립트에서 jsp 페이지 내에 있는 동일한 name의 두개이상의 입력란(input태그)들은
	//하나의 이름의 배열로 인식합니다.
	//<input type="checkbox" name="cseq" value="${cartVO.cseq}">
	//n번째 체크박스의 값 ==> document.formm.cseq[n]
	//그 말은 곧 배열의 길이를 나타내는 length속성이 존재한다는 뜻이며,
	//만약 input태그가 여러개가 아니라 한개만 존재하는 상태라면 length값은 undefined
	
	//혹시 사용자가 체크박스를 하나도 체크하지 않고 삭제버튼을 클릭했는지를 점검
	var count = 0;
	if(document.formm.cseq.length == undefined){
		//체크박스가 한개인 경우
		if(document.formm.cseq.checked==true) count++;
	}else{
		//체크박스가 두개 이상인 경우
		for(var i=0; i<document.formm.cseq.length; i++){
			if(document.formm.cseq[i].checked==true) count++;
		}
	}
	
	if(count==0)
		alert("삭제할 항목을 선택하세요.");
	else{
		document.formm.action="shop.do?command=cartDelete";
		document.formm.submit();
		//jsp파일에 있는 체크된 checkbox들의 value들(cseq값들)이 배열로 전송됩니다.
	}
}

function go_order_insert(){
	document.formm.action="shop.do?command=orderInsert";
	document.formm.submit();
}

function go_order(){
	document.formm.action="shop.do?command=orderInsertOne";
	document.formm.submit();
}