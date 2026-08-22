package com.tncv.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Le username est obligatoire")
    @Size(
            min = 3,
            max = 30,
            message = "Le username doit contenir entre 3 et 30 caractères"
    )
    private String username;

    @NotBlank(message = "Le prénom est obligatoire")
    private String firstName;

    @NotBlank(message = "Le nom est obligatoire")
    private String lastName;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email est invalide")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(
            min = 8,
            message = "Le mot de passe doit contenir au moins 8 caractères"
    )
    private String password;

    private String phone;

    private String profileImage;

    private String profession;
}