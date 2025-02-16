package org.petproject.logisticsprocessmanagementsystem.kafkacore;


public class OrderEvent {

    private Long orderId;
    private ProductDetailsEvent productDetailsEvent;
    private Long supplierId;
    private int internal_execution_code;
    //look
    // internal codes
    // 1-ответ об отсутствии товара;
    // 2-нехватка товара;
    // 3-нет курьеров;
    // 4-товар доступен и перенесен в доставку;
    // 5-товар доставлен


    public int getInternal_execution_code() {
        return internal_execution_code;
    }

    public void setInternal_execution_code(int internal_execution_code) {
        this.internal_execution_code = internal_execution_code;
    }
    public OrderEvent() {

    }

    public OrderEvent(Long orderId, ProductDetailsEvent productDetailsEvent) {
        this.orderId = orderId;
        this.productDetailsEvent = productDetailsEvent;
    }

    public OrderEvent(Long orderId, ProductDetailsEvent productDetailsEvent, Long supplierId) {
        this.orderId = orderId;
        this.productDetailsEvent = productDetailsEvent;
        this.supplierId = supplierId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public ProductDetailsEvent getProductDetailsEvent() {
        return productDetailsEvent;
    }

    public void setProductDetailsEvent(ProductDetailsEvent productDetailsEvent) {
        this.productDetailsEvent = productDetailsEvent;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }
}
