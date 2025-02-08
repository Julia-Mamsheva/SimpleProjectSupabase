package com.example.supabasesimpleproject.Domain

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest


/**
 * Cинглтон-объект Constant, содержащий предварительно настроенного клиента Supabase
 * */

object Constant {
    val supabase = createSupabaseClient(
        supabaseUrl = "https://ariombcblbsnmbjjhpjk.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImFyaW9tYmNibGJzbm1iampocGprIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzgyMjc4MDEsImV4cCI6MjA1MzgwMzgwMX0.rfhS-gWFyW4GAQHnm77b18sAPTA5xvwCU3W6YhnuP7o"
    ) {
        install(Postgrest)
        install(Auth)
    }
}

