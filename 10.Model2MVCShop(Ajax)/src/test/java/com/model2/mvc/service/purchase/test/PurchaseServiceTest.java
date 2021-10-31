package com.model2.mvc.service.purchase.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.user.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "classpath:config/commonservice.xml" })
@ContextConfiguration(locations = { "classpath:config/context-common.xml",
		"classpath:config/context-aspect.xml",
		"classpath:config/context-mybatis.xml",
		"classpath:config/context-transaction.xml"
})
public class PurchaseServiceTest {
	
	
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	//@Test
	public void testAddPurchase() throws Exception {
		
		  Purchase purchase = new Purchase(); 
		  Product product = new Product();
		  User user = new User(); 
		  product.setProdNo(10042); 
		  user.setUserId("user12");
		  purchase.setPurchaseProd(product); 
		  purchase.setBuyer(user);
		  purchase.setPaymentOption("1"); 
		  purchase.setReceiverName("권도윤");
		  purchase.setReceiverPhone("010123456"); 
		  purchase.setDivyAddr("우리집");
		  purchase.setDivyRequest("빨리"); 
		  purchase.setDivyDate("20211005");

		  purchaseService.addPurchase(purchase);
		  
		  System.out.println(purchase);
		 
		  Assert.assertEquals(product, purchase.getPurchaseProd());
		  Assert.assertEquals(user, purchase.getBuyer()); 
		  Assert.assertEquals("1",purchase.getPaymentOption()); 
		  Assert.assertEquals("권도윤",purchase.getReceiverName()); 
		  Assert.assertEquals("010123456",purchase.getReceiverPhone()); 
		  Assert.assertEquals("우리집",purchase.getDivyAddr()); 
		  Assert.assertEquals("빨리",purchase.getDivyRequest()); 
		  Assert.assertEquals("20211005",purchase.getDivyDate());
		 
	}
	
	//@Test
	public void testGetPurchase() throws Exception {
		
		Purchase purchase = new Purchase();
		Product product = new Product();
		User user = new User();
		
		product.setProdNo(10042);
		user.setUserId("user12");
		
		purchase = purchaseService.getPurchase(10082);
		purchase.setBuyer(user);
		purchase.setPurchaseProd(product);

		System.out.println("purchase"+purchase);
		
		//user = userService.getUser("user12");
		//product = productService.getProduct(10042);
		
		Assert.assertEquals(10082, purchase.getTranNo());
		Assert.assertEquals(user, purchase.getBuyer());
		Assert.assertEquals("우리집", purchase.getDivyAddr());
		Assert.assertEquals("2021-10-05 00:00:00", purchase.getDivyDate());
		Assert.assertEquals("빨리", purchase.getDivyRequest());
		Assert.assertEquals("2021-10-17", purchase.getOrderDate().toString());
		Assert.assertEquals("1  ", purchase.getPaymentOption());
		Assert.assertEquals(product, purchase.getPurchaseProd());
		Assert.assertEquals("권도윤", purchase.getReceiverName());
		Assert.assertEquals("010123456", purchase.getReceiverPhone());
		Assert.assertEquals("1  ", purchase.getTranCode());
		
		Assert.assertNotNull(purchaseService.getPurchase(10082));
	}
	
	//@Test
	public void testUpdatePurchase() throws Exception {
		
		Purchase purchase = purchaseService.getPurchase(10082);
		Assert.assertNotNull(purchase);
		
		System.out.println(purchase);
		
		Assert.assertEquals(10082, purchase.getTranNo());
		Assert.assertEquals("우리집", purchase.getDivyAddr());
		Assert.assertEquals("2021-10-05 00:00:00", purchase.getDivyDate());
		Assert.assertEquals("빨리", purchase.getDivyRequest());
		Assert.assertEquals("2021-10-17", purchase.getOrderDate().toString());
		Assert.assertEquals("1  ", purchase.getPaymentOption());
		Assert.assertEquals("권도윤", purchase.getReceiverName());
		Assert.assertEquals("010123456", purchase.getReceiverPhone());
		Assert.assertEquals("1  ", purchase.getTranCode());
		
		purchase.setDivyAddr("너네집");
		purchase.setDivyDate("2021-10-30");
		purchase.setDivyRequest("천천히");
		purchase.setPaymentOption("2  ");
		purchase.setReceiverName("라마바");
		purchase.setReceiverPhone("010-222-222");

		purchaseService.updatePurchase(purchase);
		
		purchase = purchaseService.getPurchase(10082);
		
		Assert.assertNotNull(purchase);
		
		System.out.println(purchase);
		
		Assert.assertEquals("너네집", purchase.getDivyAddr());
		Assert.assertEquals("2021-10-30 00:00:00", purchase.getDivyDate());
		Assert.assertEquals("천천히", purchase.getDivyRequest());
		Assert.assertEquals("2021-10-17", purchase.getOrderDate().toString());
		Assert.assertEquals("2  ", purchase.getPaymentOption());
		Assert.assertEquals("라마바", purchase.getReceiverName());
		Assert.assertEquals("010-222-222", purchase.getReceiverPhone());
	}
	
	//@Test
	public void testUpdateTranCode() throws Exception {
		
		Purchase purchase = purchaseService.getPurchase(10082);
		Assert.assertNotNull(purchase);
		System.out.println("adada"+purchase);

		Product product = new Product();
		product.setProdNo(10042);
		System.out.println("bdbdb"+product);
		
		Assert.assertEquals("1  ", purchase.getTranCode());
		
		purchase.setTranCode("2  ");
		purchase.setPurchaseProd(product);

		purchaseService.updateTranCode(purchase);
		
		purchase = purchaseService.getPurchase(10082);
		
		Assert.assertNotNull(purchase);
		
		System.out.println(purchase);
		
		Assert.assertEquals("2  ", purchase.getTranCode());
	}
	
	//@Test
	public void testGetPurchaseListAll() throws Exception {
		
		Search search = new Search();
		search.setCurrentPage(1);
		search.setPageSize(3);
		
		String buyerId = "user12"; 
		
		System.out.println(search);
		
		Map<String,Object> map = purchaseService.getPurchaseList(search,buyerId);
		System.out.println("===================================");
		System.out.println("testGetPurcaseListAll map : " + map);
		System.out.println("===================================");
		
		List<Object> list = (List<Object>)map.get("list");
		Assert.assertEquals(3, list.size());
		/*
		System.out.println("list1:" + list.get(0));
		System.out.println("list2:" + list.get(1));
		System.out.println("list3:" + list.get(2));
		*/
		List<Purchase> list2 = new ArrayList<Purchase>();
		/*for(int i=0; i<list.size(); i++) {
			Purchase purchase = new Purchase();
			
			purchase = (Purchase) list.get(i);
			
			User user = new User();
			user.setUserId(buyerId);
			
			purchase.setBuyer(user);
			
			System.out.println("for문 안에 purchase : " + purchase);
			
			list2.add(purchase);
		}*/
		List<Purchase> list3 = new ArrayList<Purchase>();
		for (Object sqlList : list) {
			Purchase purchase = new Purchase();
			
			purchase = (Purchase) sqlList;
			User user = new User();
			user.setUserId(buyerId);
			
			purchase.setBuyer(user);
			
			System.out.println("foreach 문 안에 purchase : " + purchase);
			
			list3.add(purchase);
		}
		
		System.out.println("===================================");
		System.out.println("list : " + list);
		System.out.println("===================================");
		System.out.println("list2 : " + list2);
		System.out.println("===================================");
		System.out.println("list3 : " + list3);
		System.out.println("===================================");
		
		Integer totalCount = (Integer)map.get("totalCount");
		System.out.println(totalCount);
		
	}
	
	//@Test
	public void testGetPurchaseSaleList() throws Exception {
		
		Search search = new Search();
		search.setCurrentPage(1);
		search.setPageSize(3);
		Map<String,Object> map = purchaseService.getSaleList(search);
		
		List<Object> list = (List<Object>)map.get("list");
		Assert.assertEquals(3, list.size());
		
		System.out.println(list);
		
		Integer totalCount = (Integer)map.get("totalCount");
		System.out.println(totalCount);
		
	}
		
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}

