package com.example.newsapi.api;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\bf\u0018\u00002\u00020\u0001J5\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0003\u0010\u0005\u001a\u00020\u00062\b\b\u0003\u0010\u0007\u001a\u00020\b2\b\b\u0003\u0010\t\u001a\u00020\u0006H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\nJ5\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\f\u001a\u00020\u00062\b\b\u0003\u0010\u0007\u001a\u00020\b2\b\b\u0003\u0010\t\u001a\u00020\u0006H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\n\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\r"}, d2 = {"Lcom/example/newsapi/api/NewsAPI;", "", "getBreakingNews", "Lretrofit2/Response;", "Lcom/example/newsapi/ui/NewsResponse;", "countryCode", "", "pageNumber", "", "apiKey", "(Ljava/lang/String;ILjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "searchForNews", "searchQuery", "app_debug"})
public abstract interface NewsAPI {
    
    @org.jetbrains.annotations.Nullable
    @retrofit2.http.GET(value = "v2/top-headlines")
    public abstract java.lang.Object getBreakingNews(@org.jetbrains.annotations.NotNull
    @retrofit2.http.Query(value = "country")
    java.lang.String countryCode, @retrofit2.http.Query(value = "page")
    int pageNumber, @org.jetbrains.annotations.NotNull
    @retrofit2.http.Query(value = "apiKey")
    java.lang.String apiKey, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.example.newsapi.ui.NewsResponse>> continuation);
    
    @org.jetbrains.annotations.Nullable
    @retrofit2.http.GET(value = "v2/everything")
    public abstract java.lang.Object searchForNews(@org.jetbrains.annotations.NotNull
    @retrofit2.http.Query(value = "q")
    java.lang.String searchQuery, @retrofit2.http.Query(value = "page")
    int pageNumber, @org.jetbrains.annotations.NotNull
    @retrofit2.http.Query(value = "apiKey")
    java.lang.String apiKey, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.example.newsapi.ui.NewsResponse>> continuation);
    
    @kotlin.Metadata(mv = {1, 6, 0}, k = 3)
    public final class DefaultImpls {
    }
}