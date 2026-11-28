package com.tecnm.merida.videogame_store_backend.persistence.mapper;

import com.tecnm.merida.videogame_store_backend.domain.Purchase;
import com.tecnm.merida.videogame_store_backend.domain.PurchaseItem;
import com.tecnm.merida.videogame_store_backend.web.controller.dto.PurchaseRequest;
import com.tecnm.merida.videogame_store_backend.web.controller.dto.PurchaseItemRequest;
import org.mapstruct.*;
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface PurchaseRequestMapper {
    @Mapping(target = "purchaseId", ignore = true)
    @Mapping(target = "date", ignore = true)
    @Mapping(target = "state", ignore = true)
    Purchase toPurchase(PurchaseRequest request);
    @Mapping(target = "unitPrice", ignore = true)
    @Mapping(target = "total", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "date", ignore = true)
    PurchaseItem toPurchaseItem(PurchaseItemRequest request);
}
