package com.alioth.server.domain.member.domain;

import com.alioth.server.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesMembers extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false, unique = true)
    private Long salesMemberCode;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 8)
    private String password;

    @Column(nullable = false)
    private String birthDay;

    @Column(nullable = false)
    private String address;

    @Builder.Default
    @Column(nullable = false)
    private String quit = "N";

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SalesMemberType rank;


    public void updatePassword(String updatePassword) {
        this.password = updatePassword;
    }
}
