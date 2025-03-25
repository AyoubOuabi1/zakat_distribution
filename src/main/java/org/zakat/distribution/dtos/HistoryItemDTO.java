package org.zakat.distribution.dtos;
import java.time.LocalDate;

public class HistoryItemDTO {
    private Long id;
    private String type; // "ZAKAT" or "DONATION"
    private String fullName;
    private Double amount;
    private LocalDate date;
    private String paymentMethod;

    // Constructors
    public HistoryItemDTO() {
    }

    public HistoryItemDTO(Long id, String type, String fullName, Double amount, LocalDate date, String paymentMethod) {
        this.id = id;
        this.type = type;
        this.fullName = fullName;
        this.amount = amount;
        this.date = date;
        this.paymentMethod = paymentMethod;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
