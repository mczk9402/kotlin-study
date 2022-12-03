package com.book.manager.infrastructure.database.mapper

import java.time.LocalDateTime

data class BookWithRentalRecord(
    var id: Long? = null,
    var title: String? = null,
    var author: String? = null,
    var releaseData: LocalDateTime? = null,
    var userId: Long? = null,
    var rentalDateTime: LocalDateTime? = null,
    var returnDeadline: LocalDateTime? = null
)