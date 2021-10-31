package com.model2.mvc.service.product.test;

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
import com.model2.mvc.service.product.ProductService;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "classpath:config/commonservice.xml" })
@ContextConfiguration(locations = { "classpath:config/context-common.xml",
		"classpath:config/context-aspect.xml",
		"classpath:config/context-mybatis.xml",
		"classpath:config/context-transaction.xml"
})
//@ContextConfiguration(locations = { "classpath:config/context-common.xml" })
public class ProductServiceTest {
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	//@Test
	public void testAddProduct() throws Exception {
		Product product = new Product();
		product.setProdName("ÇØ¹Ù¶ó±â");
		product.setProdDetail("¾¾¾Ñ");
		product.setManuDate("20120212");
		product.setPrice(10000);
		product.setFileName("flower.jpg");
		
		productService.addProduct(product);
		
		System.out.println(product);
		
		Assert.assertEquals("ÇØ¹Ù¶ó±â", product.getProdName());
		Assert.assertEquals("¾¾¾Ñ", product.getProdDetail());
		Assert.assertEquals("20120212", product.getManuDate());
		Assert.assertEquals(10000, product.getPrice());
		Assert.assertEquals("flower.jpg", product.getFileName());
	}
	
	//@Test
	public void testGetProduct() throws Exception {
		
		Product product = new Product();
		
		product = productService.getProduct(10060);
		
		System.out.println(product);
		
		Assert.assertEquals(10060, product.getProdNo());
		Assert.assertEquals("ÇØ¹Ù¶ó±â", product.getProdName());
		Assert.assertEquals("¾¾¾Ñ", product.getProdDetail());
		Assert.assertEquals("20120212", product.getManuDate());
		Assert.assertEquals(10000, product.getPrice());
		Assert.assertEquals("flower.jpg", product.getFileName());
		
		Assert.assertNotNull(productService.getProduct(10060));
	}
	
	//@Test
	public void testUpdateProduct() throws Exception{
		
		Product product = productService.getProduct(10060);
		Assert.assertNotNull(product);
		
		Assert.assertEquals("ÇØ¹Ù¶ó±â", product.getProdName());
		Assert.assertEquals("¾¾¾Ñ", product.getProdDetail());
		Assert.assertEquals(10000, product.getPrice());
		Assert.assertEquals("20120212", product.getManuDate());
		Assert.assertEquals("flower.jpg", product.getFileName());
		
		product.setProdName("¾È°³²É");
		product.setProdDetail("²É´Ù¹ß");
		product.setPrice(20000);
		product.setManuDate("20211006");
		product.setFileName("ggot.jpg");
		
		productService.updateProduct(product);
		
		product = productService.getProduct(10060);
		
		Assert.assertNotNull(product);
		
		System.out.println(product);
		
		Assert.assertEquals("¾È°³²É", product.getProdName());
		Assert.assertEquals("²É´Ù¹ß", product.getProdDetail());
		Assert.assertEquals("20211006", product.getManuDate());
		Assert.assertEquals(20000, product.getPrice());
		Assert.assertEquals("ggot.jpg", product.getFileName());
	}
	
	@Test
	public void testGetProductListAll() throws Exception {
		
		System.out.println("°¡³ª´Ù¶ó");
		
		Search search = new Search();
		search.setCurrentPage(1);
		search.setPageSize(3);
		Map<String,Object> map = productService.getProductList(search);
		
		System.out.println("testGetProductListAll map : " + map);
		
		List<Object> list = (List<Object>)map.get("list");
		Assert.assertEquals(3, list.size());
		
		System.out.println("testGetProductListAll List : " + list);
		
		Integer totalCount = (Integer)map.get("totalCount");
		System.out.println(totalCount);
		
		System.out.println("=========================================");
		
		search.setCurrentPage(1);
		search.setPageSize(3);
		search.setSearchCondition("0");
		search.setSearchKeyword("");
		map = productService.getProductList(search);
		
		list = (List<Object>)map.get("list");
		Assert.assertEquals(3, list.size());
		
		totalCount = (Integer)map.get("totalCount");
		System.out.println(totalCount);
	}
	
	//@Test
	public void testGetProductListByProductNo() throws Exception {
		Search search = new Search();
		search.setCurrentPage(1);
		search.setPageSize(3);
		search.setSearchCondition("0");
		search.setSearchKeyword("10040");
		Map<String,Object> map = productService.getProductList(search);
		
		List<Object> list = (List<Object>)map.get("list");
		Assert.assertEquals(1, list.size());
		
		System.out.println(list);
		
		Integer totalCount = (Integer)map.get("totalCount");
		System.out.println(totalCount);
		
		System.out.println("========================================");
		
		search.setSearchCondition("0");
		search.setSearchKeyword(""+System.currentTimeMillis());
		map = productService.getProductList(search);
		
		list = (List<Object>)map.get("list");
		Assert.assertEquals(0, list.size());
		
		System.out.println(list);
		
		totalCount = (Integer)map.get("totalCount");
		System.out.println(totalCount);
		
	}
	
	//@Test
	public void testGetProductListByProductName() throws Exception {
		Search search = new Search();
		search.setCurrentPage(1);
		search.setPageSize(3);
		search.setSearchCondition("1");
		search.setSearchKeyword("¾È°³²É");
		Map<String,Object> map = productService.getProductList(search);
		
		List<Object> list = (List<Object>)map.get("list");
		Assert.assertEquals(1, list.size());
		
		System.out.println(list);
		
		Integer totalCount = (Integer)map.get("totalCount");
		System.out.println(totalCount);
		
		System.out.println("========================================");
		
		search.setSearchCondition("1");
		search.setSearchKeyword(""+System.currentTimeMillis());
		map = productService.getProductList(search);
		
		list = (List<Object>)map.get("list");
		Assert.assertEquals(0, list.size());
		
		System.out.println(list);
		
		totalCount = (Integer)map.get("totalCount");
		System.out.println(totalCount);
		
	}
	
	//@Test
	public void testGetProductListByProductPrice() throws Exception {
		Search search = new Search();
		search.setCurrentPage(1);
		search.setPageSize(10);
		search.setSearchCondition("2");
		search.setSearchKeyword("2000");
		Map<String,Object> map = productService.getProductList(search);
		
		List<Object> list = (List<Object>)map.get("list");
		//Assert.assertEquals(1, list.size());
		
		System.out.println(list);
		
		Integer totalCount = (Integer)map.get("totalCount");
		System.out.println(totalCount);
		
		System.out.println("========================================");
		
		search.setSearchCondition("2");
		search.setSearchKeyword("200");
		map = productService.getProductList(search);
		
		list = (List<Object>)map.get("list");
		//Assert.assertEquals(0, list.size());
		
		System.out.println(list);
		
		totalCount = (Integer)map.get("totalCount");
		System.out.println(totalCount);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
