package com.fetisman.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class KpacSet {
    private long id;
    @NotBlank private String title;
    private List<Kpac> kpacs;
}

