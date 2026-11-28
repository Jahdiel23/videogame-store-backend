package com.tecnm.merida.videogame_store_backend.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
public class PurchaseItem {
    private Integer videogameId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal total;
    private Boolean active;
    private LocalDateTime date;
    public Integer getVideogameId() { return videogameId; }
    public void setVideogameId(Integer videogameId) { this.videogameId = videogameId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
}
