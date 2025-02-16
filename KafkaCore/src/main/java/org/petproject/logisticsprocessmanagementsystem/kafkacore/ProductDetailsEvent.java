package org.petproject.logisticsprocessmanagementsystem.kafkacore;


public class ProductDetailsEvent {

    private Long productId;
    private Long quantity;
    private Long orderId;

    public ProductDetailsEvent(Long productId, Long quantity, Long orderId) {
        this.productId = productId;
        this.quantity = quantity;
        this.orderId = orderId;
    }
    public ProductDetailsEvent() {

    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
