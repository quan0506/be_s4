package com.fptaptech.s4.service.interfaces;

import com.fptaptech.s4.dto.SpaDTO;
import com.fptaptech.s4.response.Response;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface ISpaService {
    Response addNewSpaServiceName(Long branchId, MultipartFile photo, String spaServiceName, BigDecimal spaServicePrice, String spaDescription);

    List<SpaDTO> getAllSpaServices();

    Response deleteSpaServiceName(Long spaId);

    Response updateSpaServiceName(Long spaId, MultipartFile newSpaPhoto, String newSpaServiceName, BigDecimal newSpaServicePrice, String newSpaDescription);

    Response getSpaServiceNameById(Long spaId);

    Response getSpaServiceByName(String spaServiceName);
}
