package com.Optimart.services.PaymentType;

import com.Optimart.dto.PaymentType.PaymentTypeDTO;
import com.Optimart.dto.PaymentType.PaymentTypeMutiDeleteDTO;
import com.Optimart.dto.PaymentType.PaymentTypeSearchDTO;
import com.Optimart.models.Paymenttype;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.PagingResponse;

import java.util.List;

public interface IPaymentTypeService {
    PagingResponse<List<Paymenttype>> findAll(PaymentTypeSearchDTO paymentTypeSearchDTO);
    APIResponse<Paymenttype> createType(PaymentTypeDTO paymentTypeDTO);
    Paymenttype getPaymentType(String id);
    APIResponse<Paymenttype> editPaymentType(PaymentTypeDTO paymentTypeDTO, String paymentTypeId);
    APIResponse<Boolean> deletePaymentType(String id);
    APIResponse<Boolean> deleteMutilPaymentType(PaymentTypeMutiDeleteDTO paymentTypeMutiDeleteDTO);
}
