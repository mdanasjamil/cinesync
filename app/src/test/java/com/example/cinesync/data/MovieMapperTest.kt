package com.example.cinesync.data

import com.example.cinesync.data.Dto.MovieDto
import com.example.cinesync.domain.Entity.Movie
import org.junit.Test
import kotlin.test.assertEquals

class MovieMapperTest {

    @Test
    fun `MovieDto toEntity maps all fields correctly`() {
        val movieDto = MovieDto(
            id = 1,
            title = "Test Movie",
            vote_average = 8.5,
            poster_path = "/test.jpg"
        )

        val movieEntity:Movie = movieDto.toEntity()

        assertEquals(1, movieEntity.id,message="Id should be 1 but is ${movieEntity.id}")
        assertEquals("Test Movie", movieEntity.title)
        assertEquals(8.5, movieEntity.voteAverage)
        assertEquals("/test.jpg", movieEntity.posterPath)
    }

    @Test
    fun `MovieDto toEntity handles null values correctly`() {
        val movieDtoWithNulls = MovieDto(
            id = 2,
            title = null,
            vote_average = null,
            poster_path = null
        )

        val movieEntity = movieDtoWithNulls.toEntity()

        assertEquals(2, movieEntity.id)
        assertEquals("", movieEntity.title)
        assertEquals(0.0, movieEntity.voteAverage)
        assertEquals("", movieEntity.posterPath)
    }

    @Test
    fun `Movie toDto maps all fields correctly`() {
        val movieEntity = Movie(
            id = 3,
            title = "Domain Movie",
            voteAverage = 7.2,
            posterPath = "/domain.jpg",
        )

        val movieDto = movieEntity.toDto()

        assertEquals(3, movieDto.id)
        assertEquals("Domain Movie", movieDto.title)
        assertEquals(7.2, movieDto.vote_average)
        assertEquals("/domain.jpg", movieDto.poster_path)
    }
}