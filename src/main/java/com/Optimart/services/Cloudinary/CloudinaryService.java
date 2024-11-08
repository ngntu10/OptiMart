package com.Optimart.services.Cloudinary;

import com.Optimart.exceptions.FileUploadException;
import com.Optimart.responses.CloudinaryResponse;
import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary;
    @Value("${cloudinary.api_key}")
    private String API_KEY;
    public CloudinaryResponse uploadFile(final MultipartFile file, final String fileName) {
        try {
            final Map result   = cloudinary.uploader()
                    .upload(file.getBytes(),
                            Map.of("public_id",
                                    "ngntu10/"
                                            + fileName));
            final String url      = (String) result.get("secure_url");
            final String publicId = (String) result.get("public_id");
            return CloudinaryResponse.builder().publicId(publicId).url(url)
                    .build();

        } catch (Exception e) {
            throw new FileUploadException(e.getMessage());
        }
    }
}
