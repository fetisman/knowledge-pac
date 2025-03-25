package com.fetisman.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Kpac {
    private int id;
    private String title;
    private String description;
    private LocalDate creationDate;
    private boolean last;
}

