package com.ga.waslah.dto;

import com.ga.waslah.model.EmployerRequestStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployerRequestResponse {

    private String companyName;
    private String companyDescription;
    private String jobTypes;
    private String commercialRegistration;
    private EmployerRequestStatus status;
}