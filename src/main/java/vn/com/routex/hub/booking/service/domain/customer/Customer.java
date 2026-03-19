package vn.com.routex.hub.booking.service.domain.customer;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.booking.service.domain.auditing.AbstractAuditingEntity;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "CUSTOMER")
public class Customer extends AbstractAuditingEntity {

    @Id
    private String id;

    @Column(name = "USER_ID", nullable = false)
    private String userId;
    
    @Column(name = "FIRST_NAME", nullable = false)
    private String firstName;
    
    @Column(name = "PHONE_NUMBER", nullable = false)
    private String phoneNumber;
    
    @Column(name = "EMAIL")
    @Email
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private CustomerStatus status;

    @Column(name = "TOTAL_TRIPS")
    @Builder.Default
    private Integer totalTrips = 0;

    @Column(name = "TOTAL_SPENT")
    @Builder.Default
    private BigDecimal totalSpent = BigDecimal.ZERO;

    @Column(name = "TRIP_POINTS")
    @Builder.Default
    private BigDecimal tripPoints = BigDecimal.ZERO;






}
