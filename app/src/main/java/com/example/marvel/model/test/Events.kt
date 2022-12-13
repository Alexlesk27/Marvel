package com.example.marvel.model.test

data class Events(
    var available: Int,
    var collectionURI: String,
    var items: List<Item>,
    var returned: Int
)