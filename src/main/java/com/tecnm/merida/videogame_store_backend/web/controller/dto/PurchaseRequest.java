package com.tecnm.merida.videogame_store_backend.web.controller.dto;

import java.util.List;
public class PurchaseRequest {
    private String clientId;
    private String paymentMethod;
    private String comment;
    private List<PurchaseItemRequest> items;
    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public List<PurchaseItemRequest> getItems() { return items; }
    public void setItems(List<PurchaseItemRequest> items) { this.items = items; }
}
