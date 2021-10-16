package com.example.exhibitions.data;

import com.example.exhibitions.constraint.FieldMatch;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
@Setter
@FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match")
public class UserDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String username;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String confirmPassword;
    @AssertTrue
    private Boolean terms;
}



