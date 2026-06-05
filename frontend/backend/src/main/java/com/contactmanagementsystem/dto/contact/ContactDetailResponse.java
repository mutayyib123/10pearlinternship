package com.contactmanagementsystem.dto.contact;

import com.contactmanagementsystem.entity.ContactCategory;
import com.contactmanagementsystem.entity.DetailType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactDetailResponse {

    private Long id;
    private DetailType type;
    private String value;
    private ContactCategory category;
}