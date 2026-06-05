package com.contactmanagementsystem.dto.contact;

import com.contactmanagementsystem.entity.ContactCategory;
import com.contactmanagementsystem.entity.DetailType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactDetailRequest {

    @NotNull
    private DetailType type;

    @NotNull
    @Size(min = 3, max = 150)
    private String value;

    @NotNull
    private ContactCategory category;
}