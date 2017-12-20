package cn.riversky.data;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * 基本的订单数据信息
 *@author  Created by admin on 2017/12/13.
 */
public class OrderInfo {
    /**
     * 订单编号
     */
    private String orderId;
    /**
     * 订单创建时间
     */
    private Date createOrderTime;
    /**
     * 支付编号
     */
    private String paymentId;
    /**
     *支付时间
     */
    private Date paymentTime;
    /**
     * 商品编号
     */
    private String productId;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 商品价格
     */
    private long productPrice;
    /**
     * 促销价格
     */
    private long promotionPrice;
    /**
     * 商铺编号
     */
    private String shopId;
    /**
     * 商铺名称
     */
    private String shopName;
    /**
     * 商品电话
     */
    private String shopMobile;
    /**
     * 订单支付价格
     */
    private long payPrice;
    /**
     * 订单数量
     */
    private int num;

    @Override
    public String toString() {
        return "OrderInfo{" +
                "orderId='" + orderId + '\'' +
                ", createOrderTime=" + createOrderTime +
                ", paymentId='" + paymentId + '\'' +
                ", paymentTime=" + paymentTime +
                ", productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", promotionPrice=" + promotionPrice +
                ", shopId='" + shopId + '\'' +
                ", shopName='" + shopName + '\'' +
                ", shopMobile='" + shopMobile + '\'' +
                ", payPrice=" + payPrice +
                ", num=" + num +
                '}';
    }

    public OrderInfo() {
    }

    public OrderInfo(String orderId, Date createOrderTime, String paymentId, Date paymentTime, String productId, String productName, long productPrice, long promotionPrice, String shopId, String shopName, String shopMobile, long payPrice, int num) {
        this.orderId = orderId;
        this.createOrderTime = createOrderTime;
        this.paymentId = paymentId;
        this.paymentTime = paymentTime;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.promotionPrice = promotionPrice;
        this.shopId = shopId;
        this.shopName = shopName;
        this.shopMobile = shopMobile;
        this.payPrice = payPrice;
        this.num = num;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getCreateOrderTime() {
        return createOrderTime;
    }

    public void setCreateOrderTime(Date createOrderTime) {
        this.createOrderTime = createOrderTime;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public long getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(long productPrice) {
        this.productPrice = productPrice;
    }

    public long getPromotionPrice() {
        return promotionPrice;
    }

    public void setPromotionPrice(long promotionPrice) {
        this.promotionPrice = promotionPrice;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopMobile() {
        return shopMobile;
    }

    public void setShopMobile(String shopMobile) {
        this.shopMobile = shopMobile;
    }

    public long getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(long payPrice) {
        this.payPrice = payPrice;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    /**
     * 对订单号，产品号码，支付id,产品价格，促销价格，支付价格
     * 订单产生时间进行生成，并转为JSON串
     * @return
     */
    public String random(){
        this.productId="12121212";
        this.orderId= UUID.randomUUID().toString().replace("-","");
        this.paymentId=UUID.randomUUID().toString().replace("-","");
        this.productPrice=new Random().nextInt(1000);
        this.promotionPrice=new Random().nextInt(500);
        this.payPrice=new Random().nextInt(480);
        String data="2015-11-11 12:22:12";
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            this.createOrderTime=simpleDateFormat.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Gson().toJson(this);
    }
}
