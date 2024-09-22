package com.Optimart.services.ProductType;

import com.Optimart.dto.ProductType.ProductTypeDTO;
import com.Optimart.dto.ProductType.ProductTypeMutiDeleteDTO;
import com.Optimart.dto.ProductType.ProductTypeSearchDTO;
import com.Optimart.models.ProductType;
import com.Optimart.responses.APIResponse;
import com.Optimart.responses.PagingResponse;

import java.util.List;

public interface IProductTypeService {

    PagingResponse<List<ProductType>> getAllProductType(ProductTypeSearchDTO productTypeSearchDTO);
    APIResponse<ProductType> createType(ProductTypeDTO productTypeDTO);
    ProductType getProductType(String id);
    APIResponse<ProductType> editProductType(ProductTypeDTO productTypeDTO, String productTypeId);
    APIResponse<Boolean> deleteProductType(String id);
    APIResponse<Boolean> deleteMultiProductType(ProductTypeMutiDeleteDTO productTypeMutiDeleteDTO);

}
