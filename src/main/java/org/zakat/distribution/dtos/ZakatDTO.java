package org.zakat.distribution.dtos;

import lombok.Getter;
import lombok.Setter;
import org.zakat.distribution.entities.User;
import org.zakat.distribution.entities.Zakat;

import java.time.LocalDate;

public class ZakatDTO {
    private Long id;
    private Double amountReceived;
    private LocalDate dateReceived;

    public ZakatDTO(Long id, Double amountReceived, LocalDate dateReceived) {
        this.id = id;
        this.amountReceived = amountReceived;
        this.dateReceived = dateReceived;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static ZakatDTO fromEntity(Zakat zakat) {
        return new ZakatDTO(
                zakat.getId(),
                zakat.getAmountReceived(),
                zakat.getDateReceived()
        );
    }

    public static Zakat toEntity(ZakatDTO zakatDTO, User receiver) {
        Zakat zakat = new Zakat();
        zakat.setAmountReceived(zakatDTO.getAmountReceived());
        zakat.setDateReceived(zakatDTO.getDateReceived());
        zakat.setReceiver(receiver);
        return zakat;
    }

    public Double getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(Double amountReceived) {
        this.amountReceived = amountReceived;
    }

    public LocalDate getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(LocalDate dateReceived) {
        this.dateReceived = dateReceived;
    }
}

