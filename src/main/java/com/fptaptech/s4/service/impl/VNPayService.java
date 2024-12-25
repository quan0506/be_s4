package com.fptaptech.s4.service.impl;


import com.fptaptech.s4.config.VNPayConfig;
import com.fptaptech.s4.dto.Response;
import com.fptaptech.s4.dto.RoomPaymentDTO;
import com.fptaptech.s4.entity.Booking;
import com.fptaptech.s4.entity.Payment;
import com.fptaptech.s4.exception.OurException;
import com.fptaptech.s4.repository.BookingRepository;
import com.fptaptech.s4.repository.PaymentRepository;
import com.fptaptech.s4.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
@Service
@RequiredArgsConstructor
public class VNPayService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;


    public String createOrder(int total, String orderInfor, String urlReturn){
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
        vnp_Params.put("vnp_Amount", String.valueOf(total * 100));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", orderInfor);
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_Returnurl);
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



    // getAll, delete, update
    public Response getAllPayments() {
        Response response = new Response();
        try {
            List<Payment> payments = paymentRepository.findAll();
            List<RoomPaymentDTO> paymentDTOs = Utils.mapPaymentListEntityToPaymentListDTO(payments);
            response.setStatusCode(200);
            response.setMessage("Payments retrieved successfully.");
            response.setData(paymentDTOs);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching payments: " + e.getMessage());
        }
        return response;
    }

    public Response updatePayment(Long id, RoomPaymentDTO paymentDetails) {
        Response response = new Response();
        try {
            Payment payment = paymentRepository.findById(id)
                    .orElseThrow(() -> new OurException("Payment Not Found"));

            // Update only the provided fields
            if (paymentDetails.getAmount() != null) {
                payment.setAmount(paymentDetails.getAmount());
            }
            if (paymentDetails.getTransactionCode() != null) {
                payment.setTransactionCode(paymentDetails.getTransactionCode());
            }
            if (paymentDetails.getPaymentMethodId() != null) {
                payment.setPaymentMethodID(paymentDetails.getPaymentMethodId());
            }
            if (paymentDetails.getPaymentDate() != null) {
                payment.setPaymentDate(paymentDetails.getPaymentDate());
            }
            if (paymentDetails.getPaymentStatus() != null) {
                payment.setPaymentStatus(paymentDetails.getPaymentStatus());
            }
            if (paymentDetails.getModeOfPayment() != null) {
                payment.setModeOfPayment(paymentDetails.getModeOfPayment());
            }
            if (paymentDetails.getCurrency() != null) {
                payment.setCurrency(paymentDetails.getCurrency());
            }
            if (paymentDetails.getChooseMethod() != null) {
                payment.setChooseMethod(paymentDetails.getChooseMethod());
            }
            if (paymentDetails.getDescription() != null) {
                payment.setDescription(paymentDetails.getDescription());
            }

            Payment updatedPayment = paymentRepository.save(payment);
            RoomPaymentDTO updatedPaymentDTO = Utils.mapPaymentEntityToPaymentDTO(updatedPayment);
            response.setStatusCode(200);
            response.setMessage("Payment updated successfully.");
            response.setData(updatedPaymentDTO);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating payment: " + e.getMessage());
        }
        return response;
    }

    public Response deletePayment(Long id) {
        Response response = new Response();
        try {
            Payment payment = paymentRepository.findById(id)
                    .orElseThrow(() -> new OurException("Payment Not Found"));
            paymentRepository.delete(payment);
            response.setStatusCode(200);
            response.setMessage("Payment deleted successfully.");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting payment: " + e.getMessage());
        }
        return response;
    }

    public Response getPaymentById(Long id) {
        Response response = new Response();
        try {
            Payment payment = paymentRepository.findById(id)
                    .orElseThrow(() -> new OurException("Payment Not Found"));
            RoomPaymentDTO paymentDTO = Utils.mapPaymentEntityToPaymentDTO(payment);
            response.setStatusCode(200);
            response.setMessage("Payment retrieved successfully.");
            response.setData(paymentDTO);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching payment: " + e.getMessage());
        }
        return response;
    }


    public Map<String, BigDecimal> calculateTotalPriceForEachMonth(int year) {
        List<Payment> payments = paymentRepository.findAllByYear(year);
        Map<String, BigDecimal> monthlyTotalPrice = new TreeMap<>();

        // Initialize all months with 0
        for (int month = 1; month <= 12; month++) {
            String monthKey = String.format("%02d", month);
            monthlyTotalPrice.put(monthKey, BigDecimal.ZERO);
        }

        // Calculate total for each month
        for (Payment payment : payments) {
            int month = payment.getPaymentDate().getMonthValue();
            String monthKey = String.format("%02d", month);

            monthlyTotalPrice.put(monthKey, monthlyTotalPrice.get(monthKey).add(payment.getAmount()));
        }

        return monthlyTotalPrice;
    }

    public Map<Integer, BigDecimal> calculateTotalPriceForEachYear() {
        List<Payment> payments = paymentRepository.findAll();
        Map<Integer, BigDecimal> yearlyTotalPrice = new TreeMap<>();

        for (Payment payment : payments) {
            int year = payment.getPaymentDate().getYear();

            yearlyTotalPrice.putIfAbsent(year, BigDecimal.ZERO);
            yearlyTotalPrice.put(year, yearlyTotalPrice.get(year).add(payment.getAmount()));
        }

        return yearlyTotalPrice;
    }
}

