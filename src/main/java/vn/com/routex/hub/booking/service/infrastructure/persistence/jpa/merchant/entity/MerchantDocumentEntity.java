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
import vn.com.routex.hub.booking.service.domain.merchant.MerchantDocumentType;
import vn.com.routex.hub.booking.service.domain.merchant.MerchantVerifiedStatus;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.entity.AbstractAuditingEntity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "MERCHANT_DOCUMENTS")
public class MerchantDocumentEntity extends AbstractAuditingEntity {

    @Id
    private String id;

    @Column(name = "MERCHANT_ID", nullable = false)
    private String merchantId;

    @Column(name = "DOCUMENT_TYPE")
    @Enumerated(EnumType.STRING)
    private MerchantDocumentType documentType;

    @Column(name = "FILE_URL")
    private String fileUrl;

    @Column(name = "FILE_NAME")
    private String fileName;

    @Column(name = "VERIFIED_STATUS")
    @Enumerated(EnumType.STRING)
    private MerchantVerifiedStatus verifiedStatus;

    @Column(name = "VERIFIED_NOTE")
    private String verifiedNote;
}
