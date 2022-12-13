package com.example.marvel.model.test

data class Data(
    var count: Int,
    var limit: Int,
    var offset: Int,
    var results: List<Result>,
    var total: Int
)