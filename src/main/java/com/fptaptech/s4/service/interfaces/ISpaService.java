package com.fptaptech.s4.service.interfaces;


import com.fptaptech.s4.dto.Response;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface ISpaService {
    Response addNewSpa(Long branchId, List<MultipartFile> photos, String spaServiceName, BigDecimal spaServicePrice, String spaDescription);

    Response getAllSpaServices(Long branchId);

    Response deleteSpaServiceName(Long spaId);

    Response updateSpa(Long branchId, Long spaId, String spaServiceName, BigDecimal spaServicePrice, String spaDescription, List<MultipartFile> photos);

    Response getSpaServiceNameById(Long spaId);

    Response getSpaServiceByName(String spaServiceName);

    Response getAllSpas();
}
