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
        Genius genius = new Genius();
        System.out.println(genius.findTopHitTitle("Kendrick"));
    }

}
