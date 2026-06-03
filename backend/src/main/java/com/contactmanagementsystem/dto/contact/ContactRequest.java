package com.contactmanagementsystem.dto.contact;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ContactRequest {

    @NotNull
    @Size(min = 1, max = 100)
    private String firstName;

    @NotNull
    @Size(min = 1, max = 100)
    private String lastName;

    @Size(max = 100)
    private String title;

    @NotNull
    private Long userId;

    @Valid
    private List<ContactDetailRequest> contactDetails = new ArrayList<>();
}