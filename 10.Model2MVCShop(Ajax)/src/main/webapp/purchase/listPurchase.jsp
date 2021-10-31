<%@ page contentType="text/html; charset=euc-kr" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
<title>구매 목록조회</title>

<link rel="stylesheet" href="/css/admin.css" type="text/css">
<!-- CDN(Content Delivery Network) 호스트 사용 -->
<script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
<script src="../javascript/utilScript.js"></script>

<script type="text/javascript">
	function fncGetList(currentPage) {
		//document.getElementById("currentPage").value = currentPage;
		$("#currentPage").val(currentPage);
		//document.detailForm.submit();
		$("form").attr("method", "POST").attr("action", "/purchase/listPurchase").submit();
	}
	
	$(function(){
		$(".ct_list_pop td:nth-child(1)").on("click", function(){
			self.location = "/purchase/getPurchase?tranNo="+$(this).attr("data-tranno");
		});
		
		$(".ct_list_pop td:nth-child(3)").on("click", function(){
			self.location="/user/getUser?userId="+$(this).text().trim(); 
		});
		
		//==> UI 수정 추가부분  :  userId LINK Event End User 에게 보일수 있도록 
		$( ".ct_list_pop td:nth-child(1)" ).css("color" , "red");
		$( ".ct_list_pop td:nth-child(3)" ).css("color" , "red");
		$( ".ct_list_pop td:nth-child(11)" ).css("color" , "red");
		$("h7").css("color" , "red");
		$(".ct_list_pop:nth-child(4n+6)" ).css("background-color" , "whitesmoke");
		
		var obj = $(".ct_list_pop td:nth-child(11)");
		
		obj.each(function(index, elem){
			var tran_code = $(this).attr("data-trancode");
			if(tran_code == 2){
				$(this).text("물건도착");
				console.log("/purchase/updateTranCode?prodNo="+$(this).attr("data-prodno")+"&tranCode=3&page=${resultPage.currentPage}");
			}
		});
		
		$(".ct_list_pop td:nth-child(11):contains('물건도착')").on("click", function(){
			self.location="/purchase/updateTranCode?prodNo="+$(this).attr("data-prodno")+"&tranCode=3&page=${resultPage.currentPage}";
		});
	});
</script>
</head>

<body bgcolor="#ffffff" text="#000000">

<div style="width: 98%; margin-left: 10px;">

<form name="detailForm" action="/purchase/listPurchase" method="post">

<table width="100%" height="37" border="0" cellpadding="0"	cellspacing="0">
	<tr>
		<td width="15" height="37"><img src="/images/ct_ttl_img01.gif"width="15" height="37"></td>
		<td background="/images/ct_ttl_img02.gif" width="100%" style="padding-left: 10px;">
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="93%" class="ct_ttl01">구매 목록조회</td>
				</tr>
			</table>
		</td>
		<td width="12" height="37"><img src="/images/ct_ttl_img03.gif"	width="12" height="37"></td>
	</tr>
</table>

<table width="100%" border="0" cellspacing="0" cellpadding="0"	style="margin-top: 10px;">
	<tr>
		<td colspan="11" >전체 ${resultPage.totalCount} 건수, 현재 ${resultPage.currentPage} 페이지</td>
	</tr>
	<tr>
		<td class="ct_list_b" width="100">No</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">회원ID</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b" width="150">회원명</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">전화번호</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">배송현황</td>
		<td class="ct_line02"></td>
		<td class="ct_list_b">정보수정</td>
	</tr>
	<tr>
		<td colspan="11" bgcolor="808285" height="1"></td>
	</tr>
	<c:forEach var="purchase" items="${list}">
		<tr class="ct_list_pop">
			<td align="center" data-tranno="${purchase.tranNo}">
				<!-- <a href="/purchase/getPurchase?tranNo=${purchase.tranNo}">${purchase.rowNum}</a>-->
				${purchase.rowNum}
			</td>
			<td></td>
			<td align="left">
				<!-- <a href="/user/getUser?userId=${purchase.buyer.userId}">${purchase.buyer.userId}</a> -->
				${purchase.buyer.userId}
			</td>
			<td></td>
			<td align="left">${purchase.receiverName}</td>
			<td></td>
			<td align="left">${purchase.receiverPhone}</td>
			<td></td>
			<td align="left">
				현재
				<c:choose>
					<c:when test="${purchase.tranCode=='1  '}">
					구매완료 상태 입니다.
					</c:when>
					<c:when test="${purchase.tranCode=='2  '}">
					배송중 상태 입니다.
					</c:when>
					<c:when test="${purchase.tranCode=='3  '}">
					배송완료 상태 입니다.
					</c:when>
					<c:otherwise>
					판매중 상태 입니다.
					</c:otherwise>
				</c:choose>
			</td>
			<td></td>
			<td align="left" data-trancode="${purchase.tranCode}" data-prodno="${purchase.purchaseProd.prodNo}">
				<!-- jQuery 구현 -->
				<!--<c:choose>
					<c:when test="${purchase.tranCode=='2  '}">
					<a href="/purchase/updateTranCode?prodNo=${purchase.purchaseProd.prodNo}&tranCode=3&page=${resultPage.currentPage}">물건도착</a>
					</c:when>
					<c:otherwise>
					</c:otherwise>
				</c:choose>-->
			</td>
		</tr>
		<tr>
			<td colspan="11" bgcolor="D6D7D6" height="1"></td>
		</tr>
	</c:forEach>
</table>

<!-- PageNavigation Start -->
<table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-top:10px;">
	<tr>
		<td align="center">
			<input type="hidden" id="currentPage" name="currentPage" value=""/>
			
			<jsp:include page="../common/pageNavigator.jsp"/>
    	</td>
	</tr>
</table>
<!-- PageNavigation End -->

</form>

</div>

</body>
</html>