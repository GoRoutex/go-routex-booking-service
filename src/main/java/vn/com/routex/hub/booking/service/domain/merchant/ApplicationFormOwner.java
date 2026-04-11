package vn.com.routex.hub.booking.service.domain.merchant;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationFormOwner {
    private String ownerEmail;
    private String ownerFullName;
    private String ownerName;
    private String ownerPhone;
}
