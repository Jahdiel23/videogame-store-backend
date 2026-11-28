package com.tecnm.merida.videogame_store_backend.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "compras_productos")
public class CompraProducto {

    @EmbeddedId
    private CompraProductoPK id;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "total", precision = 18, scale = 2)
    private BigDecimal total;

    @Column(name = "estado")
    private Boolean estado;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @MapsId("idCompra")
    @ManyToOne
    @JoinColumn(name = "id_compra")
    private Compra compra;

    @ManyToOne
    @JoinColumn(
            name = "id_producto",
            insertable = false,
            updatable = false
    )
    private Videojuego producto;

    @Column(name = "precio_unitario", precision = 12, scale = 2)
    private BigDecimal precioUnitario;

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

    public CompraProducto() {
    }

    public CompraProducto(
            CompraProductoPK id,
            Integer cantidad,
            BigDecimal total,
            Boolean estado,
            LocalDateTime fecha) {
        this.id = id;
        this.cantidad = cantidad;
        this.total = total;
        this.estado = estado;
        this.fecha = fecha;
    }

    public CompraProductoPK getId() {
        return id;
    }

    public void setId(CompraProductoPK id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public Videojuego getProducto() {
        return producto;
    }

    public void setProducto(Videojuego producto) {
        this.producto = producto;
    }
}
