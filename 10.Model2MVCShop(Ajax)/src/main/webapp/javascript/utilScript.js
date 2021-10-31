// 뒤로가기 함수
function historyBack(){
	history.go(-1);
}

//도큐멘트 로딩 완료시 실행
$(function(){
	
	$(".history_back").on("click",function(e){
		e.preventDefault();
		historyBack();
	});
	
});