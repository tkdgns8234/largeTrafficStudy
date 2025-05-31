package com.hoon.hs.repository;

import com.hoon.hs.entity.Article;
import com.hoon.hs.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
}