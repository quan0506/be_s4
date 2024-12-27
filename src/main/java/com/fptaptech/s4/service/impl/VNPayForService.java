package com.fptaptech.s4.service.impl;

import com.fptaptech.s4.config.VNPayConfig;
import com.fptaptech.s4.dto.Response;
import com.fptaptech.s4.dto.ServicePaymentDTO;
import com.fptaptech.s4.entity.Payment;
import com.fptaptech.s4.entity.ServicePayment;
import com.fptaptech.s4.exception.OurException;
import com.fptaptech.s4.repository.RestaurantBookingRepository;
import com.fptaptech.s4.repository.SpaBookingRepository;
import com.fptaptech.s4.repository.VNPayForServiceRepository;
import com.fptaptech.s4.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class VNPayForService {

    private final VNPayForServiceRepository servicePaymentRepository;
    private final SpaBookingRepository spaBookingRepository;
    private final RestaurantBookingRepository restaurantBookingRepository;

    public String createOrder(int total, String orderInfo, String urlReturn) {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        String vnp_IpAddr = "127.0.0.1";
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;
        String orderType = "order-type";

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(total * 1));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", orderInfo);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", urlReturn);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII)).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (!fieldName.equals(fieldNames.get(fieldNames.size() - 1))) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        return VNPayConfig.vnp_PayUrl + "?" + queryUrl;
    }

    public int orderReturn(HttpServletRequest request) {
        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        if (fields.containsKey("vnp_SecureHashType")) {
            fields.remove("vnp_SecureHashType");
        }
        if (fields.containsKey("vnp_SecureHash")) {
            fields.remove("vnp_SecureHash");
        }

        String signValue = VNPayConfig.hashAllFields(fields);

        // Debug logs
        System.out.println("Received Fields: " + fields);
        System.out.println("Received Secure Hash: " + vnp_SecureHash);
        System.out.println("Computed Sign Value: " + signValue);
        System.out.println("Transaction Status: " + request.getParameter("vnp_TransactionStatus"));

        if (signValue.equals(vnp_SecureHash)) {
            if ("00".equals(request.getParameter("vnp_TransactionStatus"))) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return -1;
        }
    }

    // Get all service payments
    public Response getAllServicePayments() {
        Response response = new Response();
        try {
            List<ServicePayment> servicePayments = servicePaymentRepository.findAll();
            List<ServicePaymentDTO> paymentDTOs = Utils.mapServicePaymentListToDTOList(servicePayments);
            response.setStatusCode(200);
            response.setMessage("Service payments retrieved successfully.");
            response.setData(paymentDTOs);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching service payments: " + e.getMessage());
        }
        return response;
    }

    // Update a service payment
    public Response updateServicePayment(Long id, ServicePaymentDTO paymentDetails) {
        Response response = new Response();
        try {
            ServicePayment servicePayment = servicePaymentRepository.findById(id)
                    .orElseThrow(() -> new OurException("Service Payment Not Found"));

            // Update only the provided fields
            if (paymentDetails.getAmount() != null) {
                servicePayment.setAmount(paymentDetails.getAmount());
            }
            if (paymentDetails.getChooseMethod() != null) {
                servicePayment.setChooseMethod(paymentDetails.getChooseMethod());
            }
            if (paymentDetails.getPaymentMethodID() != null) {
                servicePayment.setPaymentMethodID(paymentDetails.getPaymentMethodID());
            }
            if (paymentDetails.getPaymentDate() != null) {
                servicePayment.setPaymentDate(paymentDetails.getPaymentDate());
            }
            if (paymentDetails.getPaymentStatus() != null) {
                servicePayment.setPaymentStatus(paymentDetails.getPaymentStatus());
            }
            if (paymentDetails.getTransactionCode() != null) {
                servicePayment.setTransactionCode(paymentDetails.getTransactionCode());
            }
            if (paymentDetails.getCurrency() != null) {
                servicePayment.setCurrency(paymentDetails.getCurrency());
            }
            if (paymentDetails.getDescription() != null) {
                servicePayment.setDescription(paymentDetails.getDescription());
            }

            ServicePayment updatedPayment = servicePaymentRepository.save(servicePayment);
            ServicePaymentDTO updatedPaymentDTO = Utils.mapServicePaymentEntityToServicePaymentDTO(updatedPayment);
            response.setStatusCode(200);
            response.setMessage("Service payment updated successfully.");
            response.setData(updatedPaymentDTO);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating service payment: " + e.getMessage());
        }
        return response;
    }

    // Delete a service payment
    public Response deleteServicePayment(Long id) {
        Response response = new Response();
        try {
            ServicePayment servicePayment = servicePaymentRepository.findById(id)
                    .orElseThrow(() -> new OurException("Service Payment Not Found"));
            servicePaymentRepository.delete(servicePayment);
            response.setStatusCode(200);
            response.setMessage("Service payment deleted successfully.");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting service payment: " + e.getMessage());
        }
        return response;
    }

    // Get a service payment by ID
    public Response getServicePaymentById(Long id) {
        Response response = new Response();
        try {
            ServicePayment servicePayment = servicePaymentRepository.findById(id)
                    .orElseThrow(() -> new OurException("Service Payment Not Found"));
            ServicePaymentDTO paymentDTO = Utils.mapServicePaymentEntityToServicePaymentDTO(servicePayment);
            response.setStatusCode(200);
            response.setMessage("Service payment retrieved successfully.");
            response.setData(paymentDTO);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching service payment: " + e.getMessage());
        }
        return response;
    }

    // Get all shuttle payments
    public Response getAllShuttlePayments() {
        Response response = new Response();
        try {
            List<ServicePayment> shuttlePayments = servicePaymentRepository.findByShuttleBookingIsNotNull();
            List<ServicePaymentDTO> paymentDTOs = Utils.mapServicePaymentListToDTOList(shuttlePayments);
            response.setStatusCode(200);
            response.setMessage("Shuttle payments retrieved successfully.");
            response.setData(paymentDTOs);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching shuttle payments: " + e.getMessage());
        }
        return response;
    }


    // Get all restaurant payments
    public Response getAllRestaurantPayments() {
        Response response = new Response();
        try {
            List<ServicePayment> restaurantPayments = servicePaymentRepository.findByRestaurantBookingIsNotNull();
            List<ServicePaymentDTO> paymentDTOs = Utils.mapServicePaymentListToDTOList(restaurantPayments);
            response.setStatusCode(200);
            response.setMessage("Restaurant payments retrieved successfully.");
            response.setData(paymentDTOs);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching restaurant payments: " + e.getMessage());
        }
        return response;
    }

    public Response getAllSpaPayments() {
        Response response = new Response();
        try {
            List<ServicePayment> spaPayments = servicePaymentRepository.findBySpaBookingIsNotNull();
            List<ServicePaymentDTO> paymentDTOs = Utils.mapServicePaymentListToDTOList(spaPayments);
            response.setStatusCode(200);
            response.setMessage("Spa payments retrieved successfully.");
            response.setData(paymentDTOs);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching spa payments: " + e.getMessage());
        }
        return response;
    }

    public Map<String, BigDecimal> calculateTotalPriceForEachMonth(int year, String bookingType) {
        List<ServicePayment> payments = servicePaymentRepository.findAllByYearAndBookingType(year, bookingType);
        Map<String, BigDecimal> monthlyTotalPrice = new TreeMap<>();

        // Initialize all months with 0
        for (int month = 1; month <= 12; month++) {
            String monthKey = String.format("%02d", month);
            monthlyTotalPrice.put(monthKey, BigDecimal.ZERO);
        }

        // Calculate total for each month
        for (ServicePayment payment : payments) {
            if ("Approved".equalsIgnoreCase(payment.getPaymentStatus())) {
                int month = payment.getPaymentDate().getMonthValue();
                String monthKey = String.format("%02d", month);

                monthlyTotalPrice.put(monthKey, monthlyTotalPrice.get(monthKey).add(payment.getAmount()));
            }
        }

        return monthlyTotalPrice;
    }

    public Map<Integer, BigDecimal> calculateTotalPriceForEachYear(String bookingType) {
        List<ServicePayment> payments = servicePaymentRepository.findAllByBookingType(bookingType);
        Map<Integer, BigDecimal> yearlyTotalPrice = new TreeMap<>();

        for (ServicePayment payment : payments) {
            if ("Approved".equalsIgnoreCase(payment.getPaymentStatus())) {
                int year = payment.getPaymentDate().getYear();

                yearlyTotalPrice.putIfAbsent(year, BigDecimal.ZERO);
                yearlyTotalPrice.put(year, yearlyTotalPrice.get(year).add(payment.getAmount()));
            }
        }

        return yearlyTotalPrice;
    }


}

