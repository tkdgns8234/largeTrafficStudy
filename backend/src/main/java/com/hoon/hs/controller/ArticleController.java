package com.hoon.hs.controller;

import com.hoon.hs.dto.WriteArticleDto;
import com.hoon.hs.entity.Article;
import com.hoon.hs.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class ArticleController {
    private final AuthenticationManager authenticationManager;
    private final ArticleService articleService;

    @Autowired
    public ArticleController(AuthenticationManager authenticationManager, ArticleService articleService) {
        this.authenticationManager = authenticationManager;
        this.articleService = articleService;
    }

    @PostMapping("/{boardId}/articles")
    public ResponseEntity<Article> writeArticle(@PathVariable Long boardId,
            @RequestBody WriteArticleDto writeArticleDto) {
        return ResponseEntity.ok(articleService.writeArticle(boardId, writeArticleDto));
    }

    @PutMapping("/{boardId}/articles/{articleId}")
    public ResponseEntity<Article> editArticle(@PathVariable Long boardId, @PathVariable Long articleId,
                                               @RequestBody EditArticleDto editArticleDto) {
        return ResponseEntity.ok(articleService.editArticle(boardId, articleId, editArticleDto));
    }

    @GetMapping("/{boardId}/articles")
    public ResponseEntity<List<Article>> getArticle(@PathVariable Long boardId,
                                                    @RequestParam(required = false) Long lastId,
                                                    @RequestParam(required = false) Long firstId) {
        if (lastId != null) {
            return ResponseEntity.ok(articleService.getOldArticle(boardId, lastId));
        }
        if (firstId != null) {
            return ResponseEntity.ok(articleService.getNewArticle(boardId, firstId));
        }
        return ResponseEntity.ok(articleService.firstGetArticle(boardId));
    }
}