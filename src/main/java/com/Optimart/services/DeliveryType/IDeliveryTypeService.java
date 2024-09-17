package com.Optimart.services.DeliveryType;

import com.Optimart.dto.deliveryType.DeliveryTypeDTO;
import com.Optimart.dto.deliveryType.DeliveryTypeMutilDeleteDTO;
import com.Optimart.dto.deliveryType.DeliveryTypeSearchDTO;
import com.Optimart.models.DeliveryType;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.PagingResponse;

import java.util.List;

public interface IDeliveryTypeService {
    PagingResponse<List<DeliveryType>> findAll(DeliveryTypeSearchDTO deliveryTypeSearchDTO);
    APIResponse<DeliveryType> createType(DeliveryTypeDTO createDeliveryTypeDTO);
    DeliveryType getDeliveryType(String id);
    APIResponse<DeliveryType> editDeliveryType(DeliveryTypeDTO deliveryTypeDTO);
    APIResponse<Boolean> deleteDeliveryType(String id);
    APIResponse<Boolean> deleteMutilDeliveryType(DeliveryTypeMutilDeleteDTO deliveryTypeMutilDeleteDTO);

}
