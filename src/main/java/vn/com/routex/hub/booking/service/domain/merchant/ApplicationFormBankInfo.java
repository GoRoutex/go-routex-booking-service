package vn.com.routex.hub.booking.service.domain.merchant;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ApplicationFormBankInfo {
    private String bankAccountName;
    private String bankAccountNumber;
    private String bankBranch;
    private String bankName;
}
