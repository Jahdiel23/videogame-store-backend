package com.tecnm.merida.videogame_store_backend.domain;

public class GameConsole {
    private Integer consoleId;
    private String name;
    private String manufacturer;
    private Boolean active;

    public Integer getConsoleId() { return consoleId; }
    public void setConsoleId(Integer consoleId) { this.consoleId = consoleId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
