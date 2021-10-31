package com.model2.mvc.service.purchase;

import java.util.Map;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Purchase;

public interface PurchaseService {
	
	// 구매하기
	public void addPurchase(Purchase purchase) throws Exception;

	// 구매내역 상세보기
	public Purchase getPurchase(int tranNo) throws Exception;

	// 구매이력조회
	public Map<String, Object> getPurchaseList(Search search, String buyerId) throws Exception;

	public Map<String, Object> getSaleList(Search search) throws Exception;

	// 구매내역 수정
	public void updatePurchase(Purchase purchase) throws Exception;

	// 구매처리 코드 변경
	public void updateTranCode(Purchase purchase) throws Exception;
}
