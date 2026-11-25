package com.tecnm.merida.videogame_store_backend.domain.repository;

import com.tecnm.merida.videogame_store_backend.domain.Client;
import java.util.List;
import java.util.Optional;

public interface ClientRepository {

    List<Client> getAll();

    Optional<Client> getClient(String clientId);

    Client save(Client client);

    void delete(String clientId);
}