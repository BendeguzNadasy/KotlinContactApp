package io.github.bendeguznadasy.contact.model.dto

import io.github.bendeguznadasy.contact.model.Contact

data class ContactDto(
    var name: String,
    var email: String?,
    var phone: String?,
    var address: String?
)

fun ContactDto.toEntity(imagePath: String? = null): Contact {
    return Contact(
        name = this.name,
        email = this.email,
        phone = this.phone,
        address = this.address,
        imagePath = imagePath
    )
}
