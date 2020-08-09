package main.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data

@Entity
@Table(name = "captcha_codes")
public class CaptchaCode {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Basic
    @NotNull
    private long timestamp;

    @NotNull
    @Column(length = 5000)
    private String code;

    @NotNull
    @Column(name = "secret_code", length = 5000)
    private String secretCode;
}
