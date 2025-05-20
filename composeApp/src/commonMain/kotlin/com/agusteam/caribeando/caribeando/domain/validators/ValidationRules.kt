package com.agusteam.caribeando.domain.validators

const val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-zA-Z]{2,}$"
const val PHONE_REGEX = "^\\d{10,15}$"
const val PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}$"
