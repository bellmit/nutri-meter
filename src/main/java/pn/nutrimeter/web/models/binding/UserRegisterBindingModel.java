package pn.nutrimeter.web.models.binding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import pn.nutrimeter.data.models.enums.ActivityLevel;
import pn.nutrimeter.data.models.enums.AgeCategory;
import pn.nutrimeter.data.models.enums.Sex;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class UserRegisterBindingModel {

    private String username;

    private String email;

    private String password;

    private String confirmPassword;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    private Double weight;

    private Double targetWeight;

    private Double height;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    private AgeCategory ageCategory;

    private ActivityLevel activityLevel;

    private Double bmr;

    private Double bmi;

    private Double bodyFat;

//    private Double proteinTarget;
//
//    private Double cysteineRDA;
//
//    private Double histidineRDA;
//
//    private Double isoleucineRDA;
//
//    private Double leucineRDA;
//
//    private Double lysineRDA;
//
//    private Double methionineRDA;
//
//    private Double phenylalineRDA;
//
//    private Double threonineRDA;
//
//    private Double tryptophanRDA;
//
//    private Double tyrosineRDA;
//
//    private Double valineRDA;
//
//    private Double carbohydrateTarget;
//
//    private Double fiberRDA;
//
//    private Double lipidTarget;
//
//    private Double omega3RDA;
//
//    private Double omega6RDA;
//
//    private Double saturatedRDA;
//
//    private Double transFatsRDA;
}