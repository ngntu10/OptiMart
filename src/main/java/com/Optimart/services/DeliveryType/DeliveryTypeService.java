package com.Optimart.services.DeliveryType;

import com.Optimart.constants.MessageKeys;
import com.Optimart.dto.deliveryType.DeliveryTypeDTO;
import com.Optimart.dto.deliveryType.DeliveryTypeMutilDeleteDTO;
import com.Optimart.dto.deliveryType.DeliveryTypeSearchDTO;
import com.Optimart.models.DeliveryType;
import com.Optimart.repositories.DeliveryTypeRepository;
import com.Optimart.responses.APIResponse;
import com.Optimart.utils.LocalizationUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryTypeService implements IDeliveryTypeService{
    private final DeliveryTypeRepository deliveryTypeRepository;
    private final ModelMapper mapper;
    private final LocalizationUtils localizationUtils;

    @Override
    public APIResponse<DeliveryType> createType(DeliveryTypeDTO createDeliveryTypeDTO) {
        DeliveryType deliveryType = new DeliveryType();
        deliveryType.setName(createDeliveryTypeDTO.getName());
        deliveryType.setPrice(createDeliveryTypeDTO.getPrice());
        deliveryTypeRepository.save(deliveryType);
        return new APIResponse<>(deliveryType, localizationUtils.getLocalizedMessage(MessageKeys.DELIVERY_TYPE_CREATE_SUCCESS));
    }

    @Override
    public DeliveryType getDeliveryType(String id) {
        return deliveryTypeRepository.findById(UUID.fromString(id)).get();
    }

    @Override
    public APIResponse<DeliveryType> editDeliveryType(DeliveryTypeDTO deliveryTypeDTO) {
        DeliveryType deliveryType = deliveryTypeRepository.findByName(deliveryTypeDTO.getName()).get();
        mapper.map(deliveryTypeDTO, deliveryType);
        deliveryTypeRepository.save(deliveryType);
        return new APIResponse<>(deliveryType, localizationUtils.getLocalizedMessage(MessageKeys.DELIVERY_TYPE_UPDATE_SUCCESS));
    }

    @Override
    public APIResponse<Boolean> deleteDeliveryType(String id) {
        DeliveryType deliveryType = deliveryTypeRepository.findById(UUID.fromString(id)).get();
        deliveryTypeRepository.delete(deliveryType);
        return new APIResponse<>(true, localizationUtils.getLocalizedMessage(MessageKeys.DELIVERY_TYPE_DELETE_SUCCESS));
    }

    @Override
    public APIResponse<Boolean> deleteMutilDeliveryType(DeliveryTypeMutilDeleteDTO deliveryTypeMutilDeleteDTO) {
        List<String> deliveryTypeIds = deliveryTypeMutilDeleteDTO.getDeliveryTypeIds();
        deliveryTypeIds.forEach(item -> {
            deliveryTypeRepository.deleteById(UUID.fromString(item));
        });
        return new APIResponse<>(true, localizationUtils.getLocalizedMessage(MessageKeys.DELIVERY_TYPE_DELETE_SUCCESS));
    }

    @Override
    public List<DeliveryType> findAll( DeliveryTypeSearchDTO deliveryTypeSearchDTO) {
        return deliveryTypeRepository.findAll();
    }
}
