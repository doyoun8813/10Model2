package com.model2.mvc.web.purchase;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.user.UserService;


//==> 구매관리 Controller
@RestController
@RequestMapping("/purchase/*")
public class PurchaseRestController {
	
	///Field
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
		
	public PurchaseRestController(){
		System.out.println(this.getClass());
	}
	
	//==> classpath:config/common.properties  ,  classpath:config/commonservice.xml 참조 할것
	//==> 아래의 두개를 주석을 풀어 의미를 확인 할것
	@Value("#{commonProperties['pageUnit']}")
	//@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	//@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	@RequestMapping(value="json/getPurchase/{tranNo}", method=RequestMethod.GET)
	public Purchase getPurchase( @PathVariable int tranNo) throws Exception {
		
		System.out.println("/purchase/json/getPurchase : GET");
		System.out.println("tranNo : " + tranNo);
		
		//Business Logic
		return purchaseService.getPurchase(tranNo);
	}
	
	@RequestMapping(value="json/addPurchase", method=RequestMethod.POST)
	public Map addPurchase( @RequestBody Purchase purchase, HttpServletRequest request) throws Exception {

		System.out.println("/purchase/json/addPurchase : POST");
		System.out.println("addPurchase purchase : " + purchase);
		
		//Business Logic
		purchaseService.addPurchase(purchase);

		Map map = new HashMap();
		map.put("purchase", purchase);

		return map;
	}
	
	@RequestMapping(value="json/updatePurchase/{tranNo}", method=RequestMethod.GET)
	public Purchase updatePurchaseView( @PathVariable int tranNo ) throws Exception{

		System.out.println("/purchase/json/updatePurchaseView : GET");
		
		//Business Logic
		return purchaseService.getPurchase(tranNo);
	}
	
	@RequestMapping(value="json/updatePurchase", method=RequestMethod.POST)
	public Map updatePurchase( @RequestBody Purchase purchase , HttpSession session) throws Exception{

		System.out.println("/purchase/json/updatePurchase : POST");
		
		//Business Logic
		purchaseService.updatePurchase(purchase);
		
		purchase = purchaseService.getPurchase(purchase.getTranNo());
		
		Map map = new HashMap();
		map.put("purchase", purchase);
		return map;
	}
	
	@RequestMapping(value="json/listPurchase/{userId}")
	public Map listPurchase( @RequestBody Search search, @PathVariable String userId, HttpServletRequest request, HttpSession session) throws Exception{
		
		System.out.println("/purchase/json/listPurchase : GET / POST");
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		User user = new User();
		user.setUserId(userId);
		
		// Business logic 수행
		Map<String , Object> map = purchaseService.getPurchaseList(search, user.getUserId());
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		map.put("list", map.get("list"));
		map.put("resultPage", resultPage);
		map.put("search", search);
		
		return map;
	}
	
	@RequestMapping(value="json/updateTranCode/prodNo={prodNo}&tranCode={tranCode}", method=RequestMethod.GET)
	public Map updateTranCode(@PathVariable int prodNo, @PathVariable String tranCode, HttpServletRequest request, HttpServletResponse response ) throws Exception{
		
		System.out.println("/purchase/json/updateTranCode : GET");
		
		Product product = new Product();
		product.setProdNo(prodNo);
		System.out.println("UpdateTranCodeAction product : " + product);
		
		Purchase purchase = new Purchase();
		
		purchase.setPurchaseProd(product);
		purchase.setTranCode(tranCode);
		System.out.println("UpdateTranCodeAction purchase : " + purchase);
		
		purchaseService.updateTranCode(purchase);
		
		Map map = new HashMap();
		map.put("purchase", purchase);
		return map;
	}
	
	/* @RequestMapping("/updateTranCodeByProd.do") */
	@RequestMapping(value="json/updateTranCodeByProd/prodNo={prodNo}&tranCode={tranCode}", method=RequestMethod.GET)
	public Map updateTranCodeByProd(@PathVariable int prodNo, @PathVariable String tranCode, HttpServletRequest request, HttpServletResponse response ) throws Exception{
		
		System.out.println("/purchase/json/updateTranCodeByProd : GET");
		
		Product product = new Product();
		product.setProdNo(prodNo);
		System.out.println("UpdateTranCodeAction product : " + product);
		
		Purchase purchase = new Purchase();
		
		purchase.setPurchaseProd(product);
		purchase.setTranCode(tranCode);
		System.out.println("UpdateTranCodeAction purchase : " + purchase);
		
		purchaseService.updateTranCode(purchase);
		
		Map map = new HashMap();
		map.put("purchase", purchase);
		return map;
	}
}