function loginCheck(){
	if(document.loginFrm.id.value==""){
		alert("아이디는 필수 입력사항입니다.");
		document.loginFrm.id.focus();
		return false;
	}else if(document.loginFrm.pwd.value==""){
		alert("비밀번호는 필수 입력사항입니다.");
		document.loginFrm.pwd.focus();
		return false;
	}else{
		return true;
	}
}

function go_next(){
	//자바스크립트에서 jsp 페이지 내의 radio버튼에 대해
	//같은 name인 것이 여러개라면 name값에 의한 배열로 인식되어 사용됩니다.
	//즉, contract.jsp에서의 radio버튼(name=okon) => 동의함버튼:okon[0], 동의안함버튼:okon[1]
	if(document.contractFrm.okon[1].checked==true){
		//동의 안함이 선택되었다면
		alert("회원 약관에 동의하셔야 회원으로 가입이 가능합니다.");
	}else{
		document.contractFrm.action = "shop.do?command=joinForm";
		document.contractFrm.submit();
	}
}

function idcheck(){
	if(document.joinForm.id.value==""){
		alert("아이디를 입력하고 중복체크를 진행하세요.");
		document.joinForm.id.focus();
		return;
	}
	var url = "shop.do?command=idCheckForm&id="+document.joinForm.id.value;
	var opt = "toolbar=no, menubar=no, resizable=no, width=500, height=250, scrollbars=no";
	window.open(url,"IdCheck",opt);
}

function idok(userid){
	opener.joinForm.id.value=userid;
	opener.joinForm.reid.value=userid;
	self.close();
}

function post_zip(){
	var url = "shop.do?command=findZipNum";
	var opt = "toolbar=no, menubar=no, resizable=no, scrollbars=no,";
	opt = opt +"  width=550, height=300, top=300, left=300";
	window.open(url,"우편번호 찾기",opt);
}

function result(zip_num,sido,gugun,dong){
	//함수 호출 형태 : result('123-123','서울시','서대문구','대현동')
	opener.document.joinForm.zip_num.value=zip_num;
	opener.document.joinForm.address1.value=sido+" "+gugun+" "+dong;
	self.close();
}

function go_save(){
	if(document.joinForm.id.value==""){
		alert("아이디를 입력하세요.")
		document.joinForm.id.focus();
	}else if(document.joinForm.reid.value!=document.joinForm.id.value){
		alert("아이디 중복확인을 하지 않았습니다.")
		document.joinForm.id.focus();
	}else if(document.joinForm.pwd.value==""){
		alert("비밀번호를 입력하세요.")
		document.joinForm.pwd.focus();
	}else if(document.joinForm.pwd.value!=document.joinForm.pwdCheck.value){
		alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.")
		document.joinForm.pwd.focus();
	}else if(document.joinForm.name.value==""){
		alert("이름을 입력하세요.")
		document.joinForm.name.focus();
	}else if(document.joinForm.email.value==""){
		alert("이메일을 입력하세요.")
		document.joinForm.email.focus();
	}else {
		document.joinForm.action="shop.do";
		document.joinForm.submit();
	}
}

function go_update(){
	if(document.joinForm.pwd.value==""){
		alert("비밀번호를 입력하세요.")
		document.joinForm.pwd.focus();
	}else if(document.joinForm.pwd.value!=document.joinForm.pwdCheck.value){
		alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.")
		document.joinForm.pwd.focus();
	}else if(document.joinForm.name.value==""){
		alert("이름을 입력하세요.")
		document.joinForm.name.focus();
	}else if(document.joinForm.email.value==""){
		alert("이메일을 입력하세요.")
		document.joinForm.email.focus();
	}else {
		document.joinForm.action="shop.do";
		document.joinForm.submit();
	}	
}

function find_account(){
	var url="shop.do?command=findAccount";
	var opt = "toolbar=no, menubar=no, resizable=no, scrollbars=no,";
	opt = opt +"  width=700, height=500, top=300, left=300";
	window.open(url,"Find Id/Pw",opt);
}

function move_login(){
	opener.location.href="shop.do?command=loginForm";
	self.close();
}

function resetPw(){
	if(document.frm.pwd.value==""){
		alert("비밀번호를 입력하세요.")
		document.frm.pwd.focus();
		return false;
	}else if(document.frm.pwd.value!=document.frm.pwd_chk.value){
		alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.")
		document.frm.pwd_chk.focus();
		return false;
	}
	return true;
}