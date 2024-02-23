package com.example.kamil.user.utils.converter;

import com.example.kamil.user.model.dto.LoggedInUserDetailsDTO;
import com.example.kamil.user.model.entity.security.LoggedInUserDetails;

public class LoggedInUserDetailsDTOConverter {

    public static LoggedInUserDetailsDTO convert(LoggedInUserDetails userDetails){

        return LoggedInUserDetailsDTO.builder()
                .address(userDetails.getAddress())
                .city(userDetails.getCity())
                .country(userDetails.getCountry())
                .phoneNumber(userDetails.getPhoneNumber())
                .postCode(userDetails.getPostCode())
                .build();
    }
}
