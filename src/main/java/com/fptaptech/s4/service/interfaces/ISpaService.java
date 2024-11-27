package com.fptaptech.s4.service.interfaces;

import com.fptaptech.s4.response.Response;

import java.util.List;

public interface ISpaService {
    Response addNewSpaServiceName(Long branchId, String spaServiceName);
    List<String> getAllSpaServiceNames();
    Response deleteSpaServiceName(Long spaId);
    Response updateSpaServiceName(Long spaId, String newSpaServiceName);
    Response getSpaServiceNameById(Long spaId);
    Response getSpaServiceByName(String spaServiceName);
}
