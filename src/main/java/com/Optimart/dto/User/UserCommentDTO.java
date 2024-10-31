package com.Optimart.dto.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCommentDTO {
        private String firstName;
        private String lastName;
        private String middleName;
        private String avatar;
        private String id;
}
