package com.Optimart.services.DeliveryType;

import com.Optimart.constants.MessageKeys;
import com.Optimart.dto.deliveryType.DeliveryTypeDTO;
import com.Optimart.dto.deliveryType.DeliveryTypeMutilDeleteDTO;
import com.Optimart.dto.deliveryType.DeliveryTypeSearchDTO;
import com.Optimart.models.DeliveryType;
import com.Optimart.models.Role;
import com.Optimart.models.User;
import com.Optimart.repositories.DeliveryTypeRepository;
import com.Optimart.repositories.Specification.UserSpecification;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.PagingResponse;
import com.Optimart.utils.LocalizationUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryTypeService implements IDeliveryTypeService{
    private final DeliveryTypeRepository deliveryTypeRepository;
    private final ModelMapper mapper;
    private final LocalizationUtils localizationUtils;

    @Override
    @Transactional
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
    @Transactional
    public APIResponse<DeliveryType> editDeliveryType(DeliveryTypeDTO deliveryTypeDTO, String deliveryTypeId) {
        DeliveryType deliveryType = deliveryTypeRepository.findById(UUID.fromString(deliveryTypeId)).get();
        mapper.map(deliveryTypeDTO, deliveryType);
        deliveryTypeRepository.save(deliveryType);
        return new APIResponse<>(deliveryType, localizationUtils.getLocalizedMessage(MessageKeys.DELIVERY_TYPE_UPDATE_SUCCESS));
    }

    @Override
    @Transactional
    public APIResponse<Boolean> deleteDeliveryType(String id) {
        DeliveryType deliveryType = deliveryTypeRepository.findById(UUID.fromString(id)).get();
        deliveryTypeRepository.delete(deliveryType);
        return new APIResponse<>(true, localizationUtils.getLocalizedMessage(MessageKeys.DELIVERY_TYPE_DELETE_SUCCESS));
    }

    @Override
    @Transactional
    public APIResponse<Boolean> deleteMutilDeliveryType(DeliveryTypeMutilDeleteDTO deliveryTypeMutilDeleteDTO) {
        List<String> deliveryTypeIds = deliveryTypeMutilDeleteDTO.getDeliveryTypeIds();
        deliveryTypeIds.forEach(item -> {
            deliveryTypeRepository.deleteById(UUID.fromString(item));
        });
        return new APIResponse<>(true, localizationUtils.getLocalizedMessage(MessageKeys.DELIVERY_TYPE_DELETE_SUCCESS));
    }

    @Override
    public PagingResponse<List<DeliveryType>> findAll(DeliveryTypeSearchDTO deliveryTypeSearchDTO) {
        Pageable pageable = PageRequest.of(deliveryTypeSearchDTO.getPage() - 1, deliveryTypeSearchDTO.getLimit(), Sort.by("createdAt").descending());
        if (StringUtils.hasText(deliveryTypeSearchDTO.getOrder())) {
            String order = deliveryTypeSearchDTO.getOrder();
            String[] orderParams = order.split("-");
            if (orderParams.length == 2) {
                Sort.Direction direction = orderParams[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
                pageable = PageRequest.of(deliveryTypeSearchDTO.getPage() - 1, deliveryTypeSearchDTO.getLimit(), Sort.by(new Sort.Order(direction, orderParams[0])));
            }
        }
        Page<DeliveryType> deliveryTypePage = StringUtils.hasText(deliveryTypeSearchDTO.getSearch()) ? deliveryTypeRepository.findByNameContainingIgnoreCase(deliveryTypeSearchDTO.getSearch(), pageable) : deliveryTypeRepository.findAll(pageable);
        return new PagingResponse<>(deliveryTypePage.getContent(), localizationUtils.getLocalizedMessage(MessageKeys.DELIVERY_TYPE_GET_SUCCESS), deliveryTypePage.getTotalPages(), deliveryTypePage.getTotalElements());
    }

}
