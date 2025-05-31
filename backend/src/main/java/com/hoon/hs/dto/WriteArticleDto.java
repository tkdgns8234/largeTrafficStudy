package com.hoon.hs.dto;

import lombok.Getter;

@Getter
public class WriteArticleDto {
    Long boardId;
    String title;
    String content;
}