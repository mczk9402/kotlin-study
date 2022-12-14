package com.example.app.bbs.app.request

import org.springframework.web.bind.annotation.RequestParam
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size


data class ArticleRequest (
   var id: Int = 0,
   @field:NotBlank
   @field:Size(min = 1, max = 50)
   var name: String = "",
   @field:NotBlank
   @field:Size(min = 1, max = 50)
   var title: String = "",
   @field:NotBlank
   @field:Size(min = 1, max = 500)
   var contents: String = "",
   @field:NotBlank
   @field:Size(min = 1, max = 4)
   var articleKey: String = ""
)