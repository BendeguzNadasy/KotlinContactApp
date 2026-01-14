package io.github.bendeguznadasy.contact.repository

import io.github.bendeguznadasy.contact.model.Contact
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ContactRepository : JpaRepository<Contact, Long>