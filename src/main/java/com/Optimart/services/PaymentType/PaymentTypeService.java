package com.Optimart.services.PaymentType;

import com.Optimart.constants.MessageKeys;
import com.Optimart.dto.PaymentType.PaymentTypeDTO;
import com.Optimart.dto.PaymentType.PaymentTypeMutiDeleteDTO;
import com.Optimart.dto.PaymentType.PaymentTypeSearchDTO;
import com.Optimart.models.Paymenttype;
import com.Optimart.repositories.PaymentTypeRepository;
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
public class PaymentTypeService implements IPaymentTypeService {
    private final PaymentTypeRepository paymentTypeRepository;
    private final ModelMapper mapper;
    private final LocalizationUtils localizationUtils;
    @Override
    public PagingResponse<List<Paymenttype>> findAll(PaymentTypeSearchDTO paymentTypeSearchDTO) {
        Pageable pageable = PageRequest.of(paymentTypeSearchDTO.getPage() - 1, paymentTypeSearchDTO.getLimit(), Sort.by("createdAt").descending());
        if (StringUtils.hasText(paymentTypeSearchDTO.getOrder())) {
            String order = paymentTypeSearchDTO.getOrder();
            String[] orderParams = order.split("-");
            if (orderParams.length == 2) {
                Sort.Direction direction = orderParams[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
                pageable = PageRequest.of(paymentTypeSearchDTO.getPage() - 1, paymentTypeSearchDTO.getLimit(), Sort.by(new Sort.Order(direction, orderParams[0])));
            }
        }
        Page<Paymenttype> paymenttypes = StringUtils.hasText(paymentTypeSearchDTO.getSearch()) ? paymentTypeRepository.findByNameContainingIgnoreCase(paymentTypeSearchDTO.getSearch(), pageable) : paymentTypeRepository.findAll(pageable);
        return new PagingResponse<>(paymenttypes.getContent(), localizationUtils.getLocalizedMessage(MessageKeys.PAYMENT_TYPE_GET_SUCCESS), paymenttypes.getTotalPages(), paymenttypes.getTotalElements());
    }

    @Override
    @Transactional
    public APIResponse<Paymenttype> createType(PaymentTypeDTO paymentTypeDTO) {
        Paymenttype paymentType = new Paymenttype();
        paymentType.setName(paymentTypeDTO.getName());
        paymentType.setType(paymentTypeDTO.getType());
        paymentTypeRepository.save(paymentType);
        return new APIResponse<>(paymentType, localizationUtils.getLocalizedMessage(MessageKeys.PAYMENT_TYPE_CREATE_SUCCESS));
    }

    @Override
    public Paymenttype getPaymentType(String id) {
        return paymentTypeRepository.findById(UUID.fromString(id)).get();
    }

    @Override
    @Transactional
    public APIResponse<Paymenttype> editPaymentType(PaymentTypeDTO paymentTypeDTO, String paymentTypeId) {
        Paymenttype paymenttype = paymentTypeRepository.findById(UUID.fromString(paymentTypeId)).get();
        paymenttype.setName(paymentTypeDTO.getName());
        paymenttype.setType(paymentTypeDTO.getType());
        paymentTypeRepository.save(paymenttype);
        return new APIResponse<>(paymenttype, localizationUtils.getLocalizedMessage(MessageKeys.PAYMENT_TYPE_UPDATE_SUCCESS));
    }

    @Override
    @Transactional
    public APIResponse<Boolean> deletePaymentType(String id) {
        Paymenttype paymenttype = paymentTypeRepository.findById(UUID.fromString(id)).get();
        paymentTypeRepository.delete(paymenttype);
        return new APIResponse<>(true, localizationUtils.getLocalizedMessage(MessageKeys.PAYMENT_TYPE_DELETE_SUCCESS));
    }

    @Override
    @Transactional
    public APIResponse<Boolean> deleteMutilPaymentType(PaymentTypeMutiDeleteDTO paymentTypeMutiDeleteDTO) {
        List<String> paymentTypeIds = paymentTypeMutiDeleteDTO.getPaymentTypeIds();
        paymentTypeIds.forEach(item -> {
            paymentTypeRepository.deleteById(UUID.fromString(item));
        });
        return new APIResponse<>(true, localizationUtils.getLocalizedMessage(MessageKeys.PAYMENT_TYPE_DELETE_SUCCESS));
    }
}
