package com.tecnm.merida.videogame_store_backend.web.controller.dto;

public class PurchaseItemRequest {
    private Integer videogameId;
    private Integer quantity;
    public Integer getVideogameId() { return videogameId; }
    public void setVideogameId(Integer videogameId) { this.videogameId = videogameId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
