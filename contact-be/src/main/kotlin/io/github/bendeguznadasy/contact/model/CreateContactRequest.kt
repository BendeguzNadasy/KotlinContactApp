package io.github.bendeguznadasy.contact.model

import io.github.bendeguznadasy.contact.model.dto.ContactDto
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.web.multipart.MultipartFile

data class CreateContactRequest(
    @Schema(
        description = "Full name of the contact.",
        example = "Gipsz Jakab",
        required = true
    )
    var name: String,

    @Schema(
        description = "Email address of the contact.",
        example = "john.doe@example.com"
    )
    var email: String? = null,

    @Schema(
        description = "Phone number.",
        example = "+36 30 123 4567"
    )
    var phone: String?,

    @Schema(
        description = "Physical address.",
        example = "1234 Budapest, Dob utca 3."
    )
    var address: String?,

    @Schema(
        description = "Profile image of the contact. Turn OFF the 'Send empty value' if you not attach image.",
        required = false,
    )
    var image: MultipartFile? = null
) {
    fun toContactDto() = ContactDto(name, email, phone, address)
}