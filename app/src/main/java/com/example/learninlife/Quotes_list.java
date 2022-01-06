package com.example.learninlife;

public class Quotes_list {
    int quote_id;
    int category_id;
    String content;
    int favourite;
    public Quotes_list(int quote_id, int category_id, String content, int favourite) {
        this.category_id = category_id;
        this.content = content;
        this.favourite = favourite;
        this.quote_id=quote_id;
    }

    public int getQuote_id() {
        return quote_id;
    }

    public void setQuote_id(int quote_id) {
        this.quote_id = quote_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getFavourite() {
        return favourite;
    }

    public void setFavourite(int favourite) {
        this.favourite = favourite;
    }
}
