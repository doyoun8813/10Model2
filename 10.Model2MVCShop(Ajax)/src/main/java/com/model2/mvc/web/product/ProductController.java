package com.model2.mvc.web.product;

import java.sql.Date;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
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
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.user.UserService;


//==> 상품관리 Controller
@Controller
@RequestMapping("/product/*")
public class ProductController {
	
	///Field
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	//setter Method 구현 않음
		
	public ProductController(){
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
	
	
	/* @RequestMapping("/addProductView.do") */ 
	@RequestMapping(value="addProduct", method=RequestMethod.GET) 
	public String addUserView() throws Exception {

		System.out.println("/product/addProductView : GET");
		
		return "redirect:/product/addProductView.jsp";
	}
	
	/* @RequestMapping("/addProduct.do") */
	@RequestMapping(value="addProduct", method=RequestMethod.POST)
	public String addUser( @ModelAttribute("product") Product product ) throws Exception {

		System.out.println("/product/addProduct : POST");
		String manuDate = product.getManuDate();
		System.out.println("addProduct manuDate : " + manuDate);		
		String replaceManuDate = manuDate.replace("-", "");
		product.setManuDate(replaceManuDate);
		System.out.println("addProduct replaceManuDate : " + replaceManuDate);		
		
		//Business Logic
		productService.addProduct(product);
		
		return "forward:/product/addProduct.jsp";
	}
	
	/* @RequestMapping("/getProduct.do") */
	@RequestMapping(value="getProduct", method=RequestMethod.GET)
	public String getProduct( @RequestParam("prodNo") int prodNo , Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session ) throws Exception {
		
		System.out.println("/product/getProduct : GET");
		//Business Logic
		Product product = productService.getProduct(prodNo);
		
		// 최근 본 상품 시작
		
		String history = (String)session.getAttribute("history");
		System.out.println("history : "+ history);
		
		String strProdNo = Integer.toString(prodNo);

		if(history == null){
			session.setAttribute("history", strProdNo);
			Cookie cookie = new Cookie("history", strProdNo);
			cookie.setMaxAge(-1);
			response.addCookie(cookie);
			System.out.println("쿠키 저장 완료");
		}else{
			String changeHistory = history+","+strProdNo;
			String[] historyArr = changeHistory.split(",");
			Set<String> hashSet = new HashSet<String>(Arrays.asList(historyArr));
			System.out.println("hashSet : " + hashSet);
			String[] resultArr = hashSet.toArray(new String[0]);
			System.out.println("resultArr.length : " + resultArr.length);
			System.out.println("Arrays.toString(resultArr) : "+Arrays.toString(resultArr));
			String changeHistoryArr = "";
			for(int i=0; i<resultArr.length; i++){
				if(i == resultArr.length-1){
					changeHistoryArr += resultArr[i];				
				}else{
					changeHistoryArr += resultArr[i]+",";				
				}
			}
			System.out.println("changeArr : " + changeHistoryArr);
			session.setAttribute("history",changeHistoryArr);
			Cookie cookie = new Cookie("history",changeHistoryArr);
			cookie.setMaxAge(-1);
			cookie.setPath("/");
			response.addCookie(cookie);
			System.out.println("쿠키 저장 완료");
		}
		// 최근 본 상품 끝
		
		// Model 과 View 연결
		model.addAttribute("product", product);
		
		return "forward:/product/getProduct.jsp";
	}
	
	/* @RequestMapping("/updateProductView.do") */
	@RequestMapping(value="updateProduct", method=RequestMethod.GET)
	public String updateProduct( @RequestParam("prodNo") int prodNo , Model model ) throws Exception{

		System.out.println("/product/updateProduct : GET");
		//Business Logic
		Product product = productService.getProduct(prodNo);
		
		System.out.println("updateProductView regDate : " + product.getRegDate());
		// Model 과 View 연결
		model.addAttribute("product", product);
		
		return "forward:/product/updateProductView.jsp";
	}
	
	/* @RequestMapping("/updateProduct.do") */
	@RequestMapping(value="updateProduct", method=RequestMethod.POST)
	public String updateProduct( @ModelAttribute("product") Product product , Model model , HttpSession session) throws Exception{

		System.out.println("/product/updateProduct : POST");
		int prodNo = product.getProdNo();
		Product productwo = productService.getProduct(prodNo);
		Date regDate = productwo.getRegDate();
		String strRegDate = regDate.toString();
		System.out.println(product);
		System.out.println("updateProduct strRegDate : " + strRegDate);
		
		//Business Logic
		productService.updateProduct(product);
		
		product.setRegDate(regDate);
		
		// Model 과 View 연결
		model.addAttribute("product", product);
		
		return "forward:/product/getProduct.jsp?menu=manage";
	}
	
	/* @RequestMapping("/listProduct.do") */
	@RequestMapping(value="listProduct")
	public String listProduct( @ModelAttribute("search") Search search , Model model , HttpServletRequest request) throws Exception{
		
		System.out.println("listProduct request menu : " + request.getParameter("menu"));
		
		System.out.println("/product/listProduct : GET / POST");
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		// Business logic 수행
		Map<String , Object> map=productService.getProductList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		// Model 과 View 연결
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		
		return "forward:/product/listProduct.jsp";
	}
}