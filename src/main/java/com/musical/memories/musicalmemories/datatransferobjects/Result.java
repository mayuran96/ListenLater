package com.musical.memories.musicalmemories.datatransferobjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Result {
    private String title;
    private PrimaryArtist primary_artist;
}
