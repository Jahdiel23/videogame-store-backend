package com.tecnm.merida.videogame_store_backend.persistence.mapper;

import com.tecnm.merida.videogame_store_backend.domain.PurchaseItem;
import com.tecnm.merida.videogame_store_backend.persistence.entity.CompraProducto;
import org.mapstruct.*;
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PurchaseItemMapper {
    @Mapping(source = "id.idProducto", target = "videogameId")
    @Mapping(source = "cantidad", target = "quantity")
    @Mapping(source = "precioUnitario", target = "unitPrice")
    @Mapping(source = "estado", target = "active")
    @Mapping(source = "fecha", target = "date")
    PurchaseItem toPurchaseItem(CompraProducto entity);
    @InheritInverseConfiguration
    @Mapping(target = "id.idCompra", ignore = true)
    @Mapping(target = "compra", ignore = true)
    @Mapping(target = "producto", ignore = true)
    CompraProducto toCompraProducto(PurchaseItem value);
}
