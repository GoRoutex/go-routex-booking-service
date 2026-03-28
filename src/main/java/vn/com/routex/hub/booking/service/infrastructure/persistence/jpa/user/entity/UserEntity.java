package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.user.entity;

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
import vn.com.routex.hub.booking.service.domain.user.UserStatus;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.entity.AuditingEntity;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "SYS_USER")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserEntity extends AuditingEntity {

    @Id
    private String id;

    @Column(name = "USER_NAME", nullable = false)
    private String username;

    @Column(name = "FULL_NAME", nullable = false)
    private String fullName;

    @Column(name = "PASSWORD_HASH", nullable = false)
    private String passwordHash;

    @Column(name = "DATE_OF_BIRTH", nullable = false)
    private LocalDate dob;

    @Column(name = "PHONE_NUMBER", nullable = false)
    private String phoneNumber;

    @Column(name = "PHONE_VERIFIED")
    private Boolean phoneVerified = false;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "EMAIL_VERIFIED")
    private Boolean emailVerified = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @Column(name = "TENANT_ID")
    private String tenantId;

    @Column(name = "LANGUAGE", nullable = false)
    private String language;

    @Column(name = "TIME_ZONE", nullable = false)
    private String timezone;

    @Column(name = "FAIL_LOGIN_COUNT")
    private Integer failLoginCount = 0;

    @Column(name = "LAST_LOGIN_AT")
    private OffsetDateTime lastLoginAt;

    @Column(name = "LOCKED_UNTIL")
    private OffsetDateTime lockedUntil;
}
