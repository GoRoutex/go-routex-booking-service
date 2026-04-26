package vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.user.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.hub.booking.service.domain.profile.Gender;
import vn.com.routex.hub.booking.service.domain.user.UserStatus;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.entity.AbstractAuditingEntity;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "SYS_USER")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserEntity extends AbstractAuditingEntity {

    @Id
    private String id;

    @Column(name = "PASSWORD_HASH", nullable = false)
    private String passwordHash;

    @Column(name = "DATE_OF_BIRTH", nullable = false)
    private LocalDate dob;

    @Column(name = "PHONE_NUMBER", nullable = false)
    private String phoneNumber;

    @Builder.Default
    @Column(name = "PHONE_VERIFIED")
    private Boolean phoneVerified = false;

    @Column(name = "NATIONAL_ID")
    private String nationalId;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "AVATAR_URL")
    private String avatarUrl;

    @Column(name = "GENDER")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Builder.Default
    @Column(name = "PROFILE_COMPLETED")
    private Boolean profileCompleted = false;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Builder.Default
    @Column(name = "EMAIL_VERIFIED")
    private Boolean emailVerified = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private UserStatus status;

    @Column(name = "LANGUAGE")
    private String language;

    @Column(name = "TIME_ZONE")
    private String timezone;

    @Builder.Default
    @Column(name = "FAIL_LOGIN_COUNT")
    private Integer failLoginCount = 0;

    @Column(name = "LAST_LOGIN_AT")
    private OffsetDateTime lastLoginAt;

    @Column(name = "LOCKED_UNTIL")
    private OffsetDateTime lockedUntil;
}
