package com.musical.memories.musicalmemories;

import com.musical.memories.musicalmemories.api.Genius;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MusicalMemoriesApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(MusicalMemoriesApplication.class, args);
    }
    @Override
    public void run(String... args) {
//		OMDBService movieService = new OMDBService();
//		Movie movie = movieService.findMovie("Avengers: Endgame");
//		System.out.println(movie.toString());
        //		GoogleBooksService s = new GoogleBooksService();
        //		s.findBook("scary");
        Genius genius = new Genius();
        genius.findSong("Kendrick");
    }

}
