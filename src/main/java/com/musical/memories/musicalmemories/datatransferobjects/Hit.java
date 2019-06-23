package com.musical.memories.musicalmemories.datatransferobjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Hit {
    private Result result;
}
