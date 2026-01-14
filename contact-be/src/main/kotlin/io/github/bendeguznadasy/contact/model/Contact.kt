package io.github.bendeguznadasy.contact.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "contacts")
class Contact(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var name: String?,

    var email: String? = null,

    var phone: String? = null,

    var address: String? = null,

    var imagePath: String? = null
)