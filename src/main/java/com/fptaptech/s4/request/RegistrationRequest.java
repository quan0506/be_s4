package com.fptaptech.s4.request;

public record RegistrationRequest(String firstName,
                                  String lastName,
                                  String email,
                                  String password,
                                  String phone,
                                  String role) {
}
