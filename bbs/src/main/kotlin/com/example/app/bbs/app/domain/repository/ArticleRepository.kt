package com.example.app.bbs.app.domain.repository

import com.example.app.bbs.app.domain.entity.Article
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleRepository: JpaRepository<Article, Int>