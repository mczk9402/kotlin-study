package com.book.manager.infrastructure.database.mapper

import org.apache.ibatis.annotations.Result
import org.apache.ibatis.annotations.Results
import org.apache.ibatis.annotations.SelectProvider
import org.apache.ibatis.type.JdbcType
import org.mybatis.dynamic.sql.util.SqlProviderAdapter
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider

abstract class BookWithRentalMapper {
    @SelectProvider(type = SqlProviderAdapter::class, method = "select")
    @Results(
        id = "BookWithRentalRecordResult", value = [
            Result(column = "id", property = "id", jdbcType = JdbcType.BIGINT, id = true),
            Result(column = "title", property = "title", jdbcType = JdbcType.VARCHAR),
            Result(column = "author", property = "author", jdbcType = JdbcType.VARCHAR),
            Result(column = "release_date", property = "releaseDate", jdbcType = JdbcType.DATE),
            Result(column = "user_id", property = "userId", jdbcType = JdbcType.BIGINT),
            Result(column = "rental_datetime", property = "rentalDatetime", jdbcType = JdbcType.TIMESTAMP),
            Result(column = "return_deadline", property = "rentalDeadline", jdbcType = JdbcType.TIMESTAMP),
        ]
    )

    abstract fun selectMany(selectStatement: SelectStatementProvider): List<BookWithRentalRecord>
}