package com.tecnm.merida.videogame_store_backend.domain;

import java.math.BigDecimal;

public class Videogame {
    private Integer videogameId;
    private String title;
    private Integer consoleId;
    private String genre;
    private BigDecimal price;
    private Integer stock;
    private Boolean active;
    private GameConsole gameConsole;

    public Integer getVideogameId() { return videogameId; }
    public void setVideogameId(Integer videogameId) { this.videogameId = videogameId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getConsoleId() { return consoleId; }
    public void setConsoleId(Integer consoleId) { this.consoleId = consoleId; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public GameConsole getGameConsole() { return gameConsole; }
    public void setGameConsole(GameConsole gameConsole) { this.gameConsole = gameConsole; }
}
