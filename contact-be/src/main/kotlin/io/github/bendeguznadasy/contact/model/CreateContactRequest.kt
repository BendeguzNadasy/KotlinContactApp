package io.github.bendeguznadasy.contact.model

import io.github.bendeguznadasy.contact.model.dto.ContactDto
import org.springframework.web.multipart.MultipartFile

data class CreateContactRequest(
    var name: String,
    var email: String? = null,
    var phone: String? = null,
    var address: String? = null,


    var image: MultipartFile? = null
) {
    fun toContactDto() = ContactDto(name, email, phone, address)
}