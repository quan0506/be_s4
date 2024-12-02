package com.fptaptech.s4.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpaDTO {
    private Long id;
    private String spaServiceName;
    private BigDecimal spaServicePrice;
    private String spaPhotoUrl;
    private String spaDescription;
    private Long branchId;

}
