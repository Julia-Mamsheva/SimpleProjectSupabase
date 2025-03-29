package com.example.supabasesimpleproject.Domain.State

data class BookState (
    var id :String = "",
    var title:String = "",
    var category:Int = -1,
    val description: String = "",
    val genre: String = ""
)