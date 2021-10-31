package com.model2.mvc.web.purchase;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.impl.ProductServiceImpl;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.user.UserService;


//==> 구매관리 Controller
@Controller
@RequestMapping("/purchase/*")
public class PurchaseController {
	
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
		
	public PurchaseController(){
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
	
	
	/* @RequestMapping("/addPurchaseView.do") */
	@RequestMapping(value="addPurchase", method=RequestMethod.GET)
	public String addPurchaseView(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		
		System.out.println("/purchase/addPurchase : GET");
		
		Integer prodNo = Integer.parseInt(request.getParameter("prod_no"));
		System.out.println("AddPurchaseView prodNo : " + prodNo);
		
		Product product = productService.getProduct(prodNo);
		System.out.println("AddPurchaseView product : " + product);
		request.setAttribute("product", product);
		
		User user = (User)session.getAttribute("user");
		System.out.println("AddPurchaseView에 user : " + user);
		request.setAttribute("user", user);
		
		return "forward:/purchase/addPurchaseView.jsp";
	}
	
	/* @RequestMapping("/addPurchase.do") */
	@RequestMapping(value="addPurchase", method=RequestMethod.POST)
	public String addPurchase( @ModelAttribute("purchase") Purchase purchase, HttpServletRequest request) throws Exception {

		System.out.println("/purchase/addPurchase : POST");
		System.out.println("addPurchase.do purchase : " + purchase);
		
		System.out.println(request.getParameter("prodNo"));
		System.out.println(request.getParameter("buyerId"));
		
		Product product = new Product();
		product.setProdNo(Integer.parseInt(request.getParameter("prodNo")));
		
		System.out.println("addPurchaseAction product : " + product);
		
		User user = new User();
		user.setUserId(request.getParameter("buyerId"));
		System.out.println("addPurchaseAction user : " + user);
		
		purchase.setPurchaseProd(product);
		purchase.setBuyer(user);
		
		System.out.println("AddPurchaseAction purchaseVO : " + purchase);
		
		//Business Logic
		purchaseService.addPurchase(purchase);

		return "forward:/purchase/addPurchase.jsp";
	}
	
	/* @RequestMapping("/getPurchase.do") */
	@RequestMapping(value="getPurchase", method=RequestMethod.GET)
	public String getPurchase( @RequestParam("tranNo") int tranNo , Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session ) throws Exception {
		
		System.out.println("/purchase/getPurchase : GET");
		//Business Logic
		Purchase purchase = purchaseService.getPurchase(tranNo);
		
		// Model 과 View 연결
		model.addAttribute("purchase", purchase);
		
		return "forward:/purchase/getPurchase.jsp";
	}
	
	/* @RequestMapping("/updatePurchaseView.do") */
	@RequestMapping(value="updatePurchase", method=RequestMethod.GET)
	public String updatePurchaseView( @RequestParam("tranNo") int tranNo , Model model ) throws Exception{

		System.out.println("/purchase/updatePurchaseView : GET");
		//Business Logic
		Purchase purchase = purchaseService.getPurchase(tranNo);
		
		// Model 과 View 연결
		model.addAttribute("purchase", purchase);
		
		return "forward:/purchase/updatePurchaseView.jsp";
	}
	
	/* @RequestMapping("/updatePurchase.do") */
	@RequestMapping(value="updatePurchase", method=RequestMethod.POST)
	public String updatePurchase( @ModelAttribute("purchase") Purchase purchase , @RequestParam("tranNo") int tranNo , Model model , HttpSession session) throws Exception{

		System.out.println("/purchase/updatePurchase : POST");
		
		//Business Logic
		purchaseService.updatePurchase(purchase);
		
		purchase = purchaseService.getPurchase(tranNo);
		
		// Model 과 View 연결
		model.addAttribute("purchase", purchase);
		
		return "forward:/purchase/updatePurchase.jsp";
	}
	
	/* @RequestMapping("/listPurchase.do") */
	@RequestMapping(value="listPurchase")
	public String listPurchase( @ModelAttribute("search") Search search , Model model , HttpServletRequest request, HttpSession session) throws Exception{
		
		System.out.println("/purchase/listPurchase : GET / POST");
		System.out.println("/purchase/listPurchase page : " + request.getParameter("page"));
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		
		if(request.getParameter("page") != null) {
			search.setCurrentPage(Integer.parseInt(request.getParameter("page")));
		}
		
		search.setPageSize(pageSize);
		
		User user = (User) session.getAttribute("user");
		
		// Business logic 수행
		Map<String , Object> map = purchaseService.getPurchaseList(search, user.getUserId());
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		// Model 과 View 연결
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		
		return "forward:/purchase/listPurchase.jsp";
	}
	
	/* @RequestMapping("/updateTranCode.do") */
	@RequestMapping(value="updateTranCode", method=RequestMethod.GET)
	public String updateTranCode(@ModelAttribute("purchase") Purchase purchase , HttpServletRequest request, HttpServletResponse response ) throws Exception{
		
		System.out.println("/purchase/updateTranCode : GET");
		System.out.println("/purchase/updateTranCode page : " + request.getParameter("page"));
		
		Product product = new Product();
		product.setProdNo(Integer.parseInt(request.getParameter("prodNo")));
		System.out.println("UpdateTranCodeAction product : " + product);
		
		purchase.setPurchaseProd(product);
		purchase.setTranCode(request.getParameter("tranCode"));
		System.out.println("UpdateTranCodeAction purchase : " + purchase);
		
		purchaseService.updateTranCode(purchase);
		
		return "redirect:/purchase/listPurchase?page="+request.getParameter("page");
	}
	
	/* @RequestMapping("/updateTranCodeByProd.do") */
	@RequestMapping(value="updateTranCodeByProd", method=RequestMethod.GET)
	public String updateTranCodeByProd(@ModelAttribute("purchase") Purchase purchase , HttpServletRequest request, HttpServletResponse response ) throws Exception{
		
		System.out.println("/purchase/updateTranCodeByProd : GET");
		System.out.println("/purchase/updateTranCodeByProd page : " + request.getParameter("page"));
		
		Product product = new Product();
		product.setProdNo(Integer.parseInt(request.getParameter("prodNo")));
		System.out.println("UpdateTranCodeAction product : " + product);
		
		purchase.setPurchaseProd(product);
		purchase.setTranCode(request.getParameter("tranCode"));
		System.out.println("UpdateTranCodeAction purchase : " + purchase);
		
		purchaseService.updateTranCode(purchase);
		
		return "redirect:/product/listProduct?page="+request.getParameter("page")+"&menu=manage";
	}
}