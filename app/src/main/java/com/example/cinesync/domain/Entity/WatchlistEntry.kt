package com.example.cinesync.domain.Entity

import com.example.cinesync.data.Database.User

class WatchlistEntry(
    val movie: Movie,
    val addedBy: String
) {
}