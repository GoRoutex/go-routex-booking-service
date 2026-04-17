package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.merchant.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.booking.service.domain.merchant.MerchantUserStatus;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.entity.AbstractAuditingEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "MERCHANT_USERS")
@Entity
public class MerchantUserEntity extends AbstractAuditingEntity {

    @Id
    private String id;

    @Column(name = "MERCHANT_ID", nullable = false, unique = true)
    private String merchantId;

    @Column(name = "USER_ID", nullable = false, unique = true)
    private String userId;

    @Column(name = "ROLE_CODE", nullable = false)
    private String roleCode;

    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private MerchantUserStatus status;

}
