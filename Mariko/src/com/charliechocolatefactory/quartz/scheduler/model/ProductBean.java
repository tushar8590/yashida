package com.charliechocolatefactory.quartz.scheduler.model;


public class ProductBean {

	String ProductID;
	String ProductSKU;
	String ProductName;
	String ProductDescription;
	String ProductPrice;
	String ProductPriceCurrency;
	String WasPrice;
	String DiscountedPrice;
	String ProductURL;
	String PID;
	String MID;
	String ProductImageSmallURL;
	String ProductImageMediumURL;
	String ProductImageLargeURL;
	String MPN;
	String StockAvailability;
	String Brand ="";
	String Location;
	String Colour;
	String custom1;
	String custom2;
	String custom3;
	String custom4;
	String custom5;
	String CategoryName;
	String CategoryPathAsString;
	String size;
	String colorVaraiants;
	String sizeVariants;
	String offers;
	String styleCode;
	
	
	public String getStyleCode() {
		return styleCode;
	}
	public void setStyleCode(String styleCode) {
		this.styleCode = styleCode;
	}
	public String getOffers() {
		return offers;
	}
	public void setOffers(String offers) {
		this.offers = offers;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getColorVaraiants() {
		return colorVaraiants;
	}
	public void setColorVaraiants(String colorVaraiants) {
		this.colorVaraiants = colorVaraiants;
	}
	public String getSizeVariants() {
		return sizeVariants;
	}
	public void setSizeVariants(String sizeVariants) {
		this.sizeVariants = sizeVariants;
	}

	
	public String getProductID() {
		return ProductID;
	}
	public void setProductID(String productID) {
		ProductID = productID;
	}
	public String getProductSKU() {
		return ProductSKU;
	}
	public void setProductSKU(String productSKU) {
		ProductSKU = productSKU;
	}
	public String getProductName() {
		return ProductName;
	}
	public void setProductName(String productName) {
		ProductName = productName;
	}
	public String getProductDescription() {
		return ProductDescription;
	}
	public void setProductDescription(String productDescription) {
		ProductDescription = productDescription;
	}
	public String getProductPrice() {
		return ProductPrice;
	}
	public void setProductPrice(String productPrice) {
		ProductPrice = productPrice;
	}
	public String getProductPriceCurrency() {
		return ProductPriceCurrency;
	}
	public void setProductPriceCurrency(String productPriceCurrency) {
		ProductPriceCurrency = productPriceCurrency;
	}
	public String getWasPrice() {
		return WasPrice;
	}
	public void setWasPrice(String wasPrice) {
		WasPrice = wasPrice;
	}
	public String getDiscountedPrice() {
		return DiscountedPrice;
	}
	public void setDiscountedPrice(String discountedPrice) {
		DiscountedPrice = discountedPrice;
	}
	public String getProductURL() {
		return ProductURL;
	}
	public void setProductURL(String productURL) {
		ProductURL = productURL;
	}
	public String getPID() {
		return PID;
	}
	public void setPID(String pID) {
		PID = pID;
	}
	public String getMID() {
		return MID;
	}
	public void setMID(String mID) {
		MID = mID;
	}
	public String getProductImageSmallURL() {
		return ProductImageSmallURL;
	}
	public void setProductImageSmallURL(String productImageSmallURL) {
		ProductImageSmallURL = productImageSmallURL;
	}
	public String getProductImageMediumURL() {
		return ProductImageMediumURL;
	}
	public void setProductImageMediumURL(String productImageMediumURL) {
		ProductImageMediumURL = productImageMediumURL;
	}
	public String getProductImageLargeURL() {
		return ProductImageLargeURL;
	}
	public void setProductImageLargeURL(String productImageLargeURL) {
		ProductImageLargeURL = productImageLargeURL;
	}
	public String getMPN() {
		return MPN;
	}
	public void setMPN(String mPN) {
		MPN = mPN;
	}
	public String getStockAvailability() {
		return StockAvailability;
	}
	public void setStockAvailability(String stockAvailability) {
		StockAvailability = stockAvailability;
	}
	public String getBrand() {
		return Brand;
	}
	public void setBrand(String brand) {
		Brand = brand;
	}
	public String getLocation() {
		return Location;
	}
	public void setLocation(String location) {
		Location = location;
	}
	public String getColour() {
		return Colour;
	}
	public void setColour(String colour) {
		Colour = colour;
	}
	public String getCustom1() {
		return custom1;
	}
	public void setCustom1(String custom1) {
		this.custom1 = custom1;
	}
	public String getCustom2() {
		return custom2;
	}
	public void setCustom2(String custom2) {
		this.custom2 = custom2;
	}
	public String getCustom3() {
		return custom3;
	}
	public void setCustom3(String custom3) {
		this.custom3 = custom3;
	}
	public String getCustom4() {
		return custom4;
	}
	public void setCustom4(String custom4) {
		this.custom4 = custom4;
	}
	public String getCustom5() {
		return custom5;
	}
	public void setCustom5(String custom5) {
		this.custom5 = custom5;
	}
	public String getCategoryName() {
		return CategoryName;
	}
	public void setCategoryName(String categoryName) {
		CategoryName = categoryName;
	}
	public String getCategoryPathAsString() {
		return CategoryPathAsString;
	}
	public void setCategoryPathAsString(String categoryPathAsString) {
		CategoryPathAsString = categoryPathAsString;
	}
	@Override
	public String toString() {
		return "ProductBean [ProductID=" + ProductID + ", ProductSKU="
				+ ProductSKU + ", ProductName=" + ProductName
				+ ", ProductDescription=" + ProductDescription
				+ ", ProductPrice=" + ProductPrice + ", ProductPriceCurrency="
				+ ProductPriceCurrency + ", WasPrice=" + WasPrice
				+ ", DiscountedPrice=" + DiscountedPrice + ", ProductURL="
				+ ProductURL + ", PID=" + PID + ", MID=" + MID
				+ ", ProductImageSmallURL=" + ProductImageSmallURL
				+ ", ProductImageMediumURL=" + ProductImageMediumURL
				+ ", ProductImageLargeURL=" + ProductImageLargeURL + ", MPN="
				+ MPN + ", StockAvailability=" + StockAvailability + ", Brand="
				+ Brand + ", Location=" + Location + ", Colour=" + Colour
				+ ", custom1=" + custom1 + ", custom2=" + custom2
				+ ", custom3=" + custom3 + ", custom4=" + custom4
				+ ", custom5=" + custom5 + ", CategoryName=" + CategoryName
				+ ", CategoryPathAsString=" + CategoryPathAsString + ", size="
				+ size + ", colorVaraiants=" + colorVaraiants
				+ ", sizeVariants=" + sizeVariants + ", offers=" + offers
				+ ", styleCode=" + styleCode + "]";
	}
	
}
