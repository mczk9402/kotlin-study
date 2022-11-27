package com.example.app.bbs.app.controller

import com.example.app.bbs.app.domain.entity.Article
import com.example.app.bbs.app.domain.repository.ArticleRepository
import com.example.app.bbs.app.request.ArticleRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.util.*

@Controller
class ArticleController {
    @Autowired
    lateinit var articleRepository: ArticleRepository
    val MESSAGE_REGISTER_NORMAL = "正常に投稿できました"
    val MESSAGE_UPDATE_NORMAL = "正常に更新できました"
    val MESSAGE_ARTICLE_DOES_NOT_EXISTS = "対象の記事が見つかりませんでした"
    val MESSAGE_ARTICLE_KEY_UNMATCH = "投稿KEYが一致しません"
    val MESSAGE_DELETE_NORMAL= "正常に削除できました"

    val ALERT_CLASS_ERROR = "alert-error"

    @PostMapping("/")
    fun registerArticle(
        @ModelAttribute @Validated articleRequest: ArticleRequest,
        result: BindingResult,
        redirectAttributes: RedirectAttributes,
    ):String {
        if(result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", result)
            redirectAttributes.addFlashAttribute("request", articleRequest)
            return "redirect:/"
        }

        articleRepository.save(
            Article(
                articleRequest.id,
                articleRequest.name,
                articleRequest.title,
                articleRequest.contents,
                articleRequest.articleKey
            )
        )

        redirectAttributes.addFlashAttribute("message", MESSAGE_REGISTER_NORMAL)

        return "redirect:/"
    }

    @GetMapping("/")
    fun getArticleList(
        @ModelAttribute articleRequest: ArticleRequest,
        model: Model
    ): String {
        if(model.containsAttribute("errors")) {
            val key: String = BindingResult.MODEL_KEY_PREFIX + "articleRequest"
            model.addAttribute(key,model.asMap()["errors"])
        }

        if(model.containsAttribute("request")) {
            model.addAttribute("articleRequest", model.asMap()["request"])
        }

        model.addAttribute("articles", articleRepository.findAll())
        return "index"
    }

    @GetMapping("/edit/{id}")
    fun getArticleEdit(
        @PathVariable id: Int, model: Model,
        redirectAttributes: RedirectAttributes
    ): String {
        return if (articleRepository.existsById(id)){
            model.addAttribute("article", articleRepository.findById(id))
            "edit"
        } else {
            redirectAttributes.addFlashAttribute("message", MESSAGE_ARTICLE_DOES_NOT_EXISTS)
            redirectAttributes.addFlashAttribute("alert_class", ALERT_CLASS_ERROR)
            "redirect:/"
        }
    }

    @PostMapping("/update")
    fun updateArticle(
        articleRequest: ArticleRequest,
        redirectAttributes: RedirectAttributes
    ): String {
        if (!articleRepository.existsById(articleRequest.id)) {
            redirectAttributes.addFlashAttribute("message", MESSAGE_ARTICLE_DOES_NOT_EXISTS)
            redirectAttributes.addFlashAttribute("alert_class", ALERT_CLASS_ERROR)
            return "redirect:/"
        }

        val article: Article = articleRepository.findById(articleRequest.id).get()

        //　パスが違うから元のページに戻す
        if (articleRequest.articleKey != article.articleKey) {
            redirectAttributes.addFlashAttribute("message", MESSAGE_ARTICLE_KEY_UNMATCH)
            redirectAttributes.addFlashAttribute("alert_class", ALERT_CLASS_ERROR)
            return "redirect:/edit/${articleRequest.id}"
        }

        article.name = articleRequest.name
        article.title = articleRequest.title
        article.contents = articleRequest.contents
        article.updateAt = Date()
        articleRepository.save(article)
        redirectAttributes.addFlashAttribute("message", MESSAGE_UPDATE_NORMAL)

        // 変更してトップページに遷移
        return "redirect:/"
    }

    @GetMapping("/delete/confirm/{id}")
    fun getDeleteConfirm(
        @PathVariable id: Int,
        model: Model,
        redirectAttributes: RedirectAttributes
    ): String {
        if(!articleRepository.existsById(id)) {
            redirectAttributes.addFlashAttribute("message", MESSAGE_ARTICLE_DOES_NOT_EXISTS)
            redirectAttributes.addFlashAttribute("alert_class", ALERT_CLASS_ERROR)
            return "redirect:/"
        }

        model.addAttribute("article", articleRepository.findById(id).get())
        return "delete_confirm"
    }

    @PostMapping("delete")
    fun deleteArticle(
        @ModelAttribute articleRequest: ArticleRequest,
        redirectAttributes: RedirectAttributes
    ):String {
        if(!articleRepository.existsById(articleRequest.id)) {
            redirectAttributes.addFlashAttribute("message", MESSAGE_ARTICLE_DOES_NOT_EXISTS)
            redirectAttributes.addFlashAttribute("alert_class", ALERT_CLASS_ERROR)
            return "redirect:/"
        }

        val article: Article = articleRepository.findById(articleRequest.id).get()

        if(article.articleKey != articleRequest.articleKey) {
            redirectAttributes.addFlashAttribute("message", MESSAGE_ARTICLE_DOES_NOT_EXISTS)
            redirectAttributes.addFlashAttribute("alert_class", ALERT_CLASS_ERROR)
            return "redirect:/delete/confirm/${article.id}"
        }

        articleRepository.deleteById(articleRequest.id)
        redirectAttributes.addFlashAttribute("message", MESSAGE_DELETE_NORMAL)
        return "redirect:/"
    }
}