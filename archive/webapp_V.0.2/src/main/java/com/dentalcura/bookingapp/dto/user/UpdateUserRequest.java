package com.dentalcura.bookingapp.dto.user;

public record UpdateUserRequest(
        String name,
        String surname,
        String email,
        String password,
        Boolean admin
) { }