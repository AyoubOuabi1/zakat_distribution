package org.zakat.distribution.entities;

import jakarta.persistence.*;



@Entity
public class ReceiverDetails {
    @Id
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getBankDetailsImage() {
        return bankDetailsImage;
    }

    public void setBankDetailsImage(String bankDetailsImage) {
        this.bankDetailsImage = bankDetailsImage;
    }

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private String bankDetailsImage;

    public void setUser(User user) {
        this.user = user;
        user.setReceiverDetails(this);
    }
}

