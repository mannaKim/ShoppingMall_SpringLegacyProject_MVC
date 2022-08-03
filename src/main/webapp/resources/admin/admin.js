function worker_check(){
	if(document.frm.workId.value==""){
		alert("아이디를 입력하세요.");
		return false;
	}else if(document.frm.workPwd.value==""){
		alert("비밀번호를 입력하세요.");
		return false;
	}
	return true;
}

function go_search(requestName){
	if(document.frm.key.value=="") return;
	document.frm.action = requestName+"?page=1";
	document.frm.submit();
}

function go_total(requestName){
	document.frm.key.value="";
	document.frm.action = requestName+"?page=1";
	document.frm.submit();
}