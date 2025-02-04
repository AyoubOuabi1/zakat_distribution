package org.zakat.distribution.dtos;

import lombok.Getter;
import lombok.Setter;
import org.zakat.distribution.entities.User;
import org.zakat.distribution.entities.Zakat;

import java.time.LocalDate;
@Getter
@Setter
public class ZakatDTO {
    private Double amountReceived;
    private LocalDate dateReceived;

    public ZakatDTO(Double amountReceived, LocalDate dateReceived) {
        this.amountReceived = amountReceived;
        this.dateReceived = dateReceived;
    }

    public static ZakatDTO fromEntity(Zakat zakat) {
        return new ZakatDTO(
                zakat.getAmountReceived(),
                zakat.getDateReceived()
        );
    }

    public static Zakat toEntity(ZakatDTO zakatDTO, User receiver) {
        Zakat zakat = new Zakat();
        zakat.setAmountReceived(zakatDTO.getAmountReceived());
        zakat.setDateReceived(LocalDate.now());
        zakat.setReceiver(receiver);
        return zakat;
    }

}

