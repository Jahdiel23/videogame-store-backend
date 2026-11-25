package com.tecnm.merida.videogame_store_backend.persistence.mapper;

import com.tecnm.merida.videogame_store_backend.domain.Purchase;
import com.tecnm.merida.videogame_store_backend.domain.PurchaseItem;
import com.tecnm.merida.videogame_store_backend.web.controller.dto.PurchaseRequest;
import com.tecnm.merida.videogame_store_backend.web.controller.dto.PurchaseItemRequest;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PurchaseRequestMapper {

    @Mapping(source = "idCliente", target = "clientId")
    @Mapping(source = "medioPago", target = "paymentMethod")
    @Mapping(source = "comentario", target = "comment")
    @Mapping(source = "productosAdquiridos", target = "items")
    Purchase toPurchase(PurchaseRequest request);

    @Mapping(source = "idProducto", target = "productoId")
    @Mapping(source = "cantidad", target = "quantity")
    PurchaseItem toPurchaseItem(PurchaseItemRequest request);
}