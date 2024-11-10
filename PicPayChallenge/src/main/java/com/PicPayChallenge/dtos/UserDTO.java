package com.PicPayChallenge.dtos;

import com.PicPayChallenge.domain.User.UserType;
import java.math.BigDecimal;

//define o dto do usuario
public record UserDTO (String firstName, String lastName, String document, BigDecimal balance, String email, String password, UserType userType){
}
