package com.model2.mvc.service.domain;

import java.sql.Date;


public class Product {
	
	///Field
	private int prodNo;
	private String prodName;
	private String prodDetail;
	private int price;
	private String manuDate;
	private Date regDate;
	private String fileName;
	private String proTranCode;
	
	
	///Constructor
	public Product(){
	}
	
	///Method 
	public int getProdNo() {
		return prodNo;
	}

	public void setProdNo(int prodNo) {
		this.prodNo = prodNo;
	}

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public String getProdDetail() {
		return prodDetail;
	}

	public void setProdDetail(String prodDetail) {
		this.prodDetail = prodDetail;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getManuDate() {
		return manuDate;
	}

	public void setManuDate(String manuDate) {
		this.manuDate = manuDate;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getProTranCode() {
		return proTranCode;
	}

	public void setProTranCode(String proTranCode) {
		this.proTranCode = proTranCode;
	}

	@Override
	public String toString() {
		return "Product : [prodNo] "+prodNo+" [prodName] "+prodName+" [prodDetail] "+prodDetail+" [price] "+ price+" [manuDate] "+manuDate+" [regDate] "+regDate+" [fileName] "+fileName+" [proTranCode] "+proTranCode;
	}
}