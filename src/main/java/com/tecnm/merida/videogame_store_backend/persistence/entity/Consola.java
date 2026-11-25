package com.tecnm.merida.videogame_store_backend.persistence.entity;

import jakarta.persistence.*;
import java.util.List;
@Entity
@Table(name = "consolas")
public class Consola {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id_consola")
    private Integer idConsola;
    @Column(nullable = false, length = 100)
    private String nombre;
    @Column(nullable = false, length = 100)
    private String fabricante;
    @Column(nullable = false)
    private Boolean estado;
    @OneToMany(mappedBy = "consola")
    private List<Videojuego> videojuegos;

    public Integer getIdConsola() { return idConsola; }
    public void setIdConsola(Integer idConsola) { this.idConsola = idConsola; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getFabricante() { return fabricante; }
    public void setFabricante(String fabricante) { this.fabricante = fabricante; }

    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }

    public List<Videojuego> getVideojuegos() { return videojuegos; }
    public void setVideojuegos(List<Videojuego> videojuegos) { this.videojuegos = videojuegos; }
}
