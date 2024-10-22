package com.fptaptech.s4.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "PaymentMethod")
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int paymentMethodID;

    @Column(nullable = false)
    private String methodName;

    @Column(nullable = false)
    private String description;

    public PaymentMethod() {
    }

    public int getPaymentMethodID() {
        return paymentMethodID;
    }

    public void setPaymentMethodID(int paymentMethodID) {
        this.paymentMethodID = paymentMethodID;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
