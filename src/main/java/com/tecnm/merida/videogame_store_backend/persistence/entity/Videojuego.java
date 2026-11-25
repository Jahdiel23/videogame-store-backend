package com.tecnm.merida.videogame_store_backend.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
@Entity
@Table(name = "videojuegos")
public class Videojuego {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id_videojuego")
    private Integer idVideojuego;
    @Column(nullable = false, length = 150)
    private String titulo;
    @Column(name = "id_consola", nullable = false)
    private Integer idConsola;
    @Column(nullable = false, length = 60)
    private String genero;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;
    @Column(nullable = false)
    private Integer stock;
    @Column(nullable = false)
    private Boolean estado;
    @ManyToOne
    @JoinColumn(name = "id_consola", insertable = false, updatable = false)
    private Consola consola;

    public Integer getIdVideojuego() { return idVideojuego; }
    public void setIdVideojuego(Integer idVideojuego) { this.idVideojuego = idVideojuego; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public Integer getIdConsola() { return idConsola; }
    public void setIdConsola(Integer idConsola) { this.idConsola = idConsola; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }

    public Consola getConsola() { return consola; }
    public void setConsola(Consola consola) { this.consola = consola; }
}
