package com.model2.mvc.web.product;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.user.UserService;

//==> 상품관리 RestController
@RestController
@RequestMapping("/product/*")
public class ProductRestController {

	/// Field
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	// setter Method 구현 않음

	public ProductRestController() {
		System.out.println(this.getClass());
	}

	// ==> classpath:config/common.properties , classpath:config/commonservice.xml
	// 참조 할것
	// ==> 아래의 두개를 주석을 풀어 의미를 확인 할것
	@Value("#{commonProperties['pageUnit']}")
	// @Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;

	@Value("#{commonProperties['pageSize']}")
	// @Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;

	@RequestMapping(value = "json/getProduct/{prodNo}", method = RequestMethod.GET)
	public Product getProduct(@PathVariable int prodNo) throws Exception {

		System.out.println("/product/json/getProduct : GET");
		System.out.println("prodNo : " + prodNo);

		// Business Logic
		return productService.getProduct(prodNo);
	}

	@RequestMapping(value = "json/addProduct", method = RequestMethod.POST)
	public Map addProduct(@RequestBody Product product) throws Exception {

		System.out.println("/product/json/addProduct : POST");
		String manuDate = product.getManuDate();
		System.out.println("addProduct manuDate : " + manuDate);
		String replaceManuDate = manuDate.replace("-", "");
		product.setManuDate(replaceManuDate);
		System.out.println("addProduct replaceManuDate : " + replaceManuDate);

		// Business Logic
		productService.addProduct(product);

		Map map = new HashMap();
		map.put("product", product);

		return map;
	}

	@RequestMapping(value = "json/updateProduct/{prodNo}", method = RequestMethod.GET)
	public Product updateProduct(@PathVariable int prodNo) throws Exception {

		System.out.println("/product/json/updateProduct : GET");

		// Business Logic
		return productService.getProduct(prodNo);
	}

	@RequestMapping(value = "json/updateProduct", method = RequestMethod.POST)
	public Map updateProduct(@RequestBody Product product, HttpSession session) throws Exception {

		System.out.println("/product/json/updateProduct : POST");
		int prodNo = product.getProdNo();
		Product productwo = productService.getProduct(prodNo);
		Date regDate = productwo.getRegDate();
		String strRegDate = regDate.toString();
		System.out.println(product);
		System.out.println("updateProduct strRegDate : " + strRegDate);

		// Business Logic
		productService.updateProduct(product);

		product.setRegDate(regDate);

		Map map = new HashMap();
		map.put("product", product);
		return map;
	}

	@RequestMapping(value = "json/listProduct")
	public Map listProduct(@ModelAttribute("search") Search search, HttpServletRequest request)
			throws Exception {

		System.out.println("listProduct request menu : " + request.getParameter("menu"));

		System.out.println("/product/json/listProduct : GET / POST");

		if (search.getCurrentPage() == 0) {
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);

		// Business logic 수행
		Map<String, Object> map = productService.getProductList(search);

		Page resultPage = new Page(search.getCurrentPage(), ((Integer) map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);

		// Model 과 View 연결
		map.put("list", map.get("list"));
		map.put("resultPage", resultPage);
		map.put("search", search);
		
		return map;
	}

}