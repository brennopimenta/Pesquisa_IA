package com.example.pesquisa_ia.dto;

import lombok.Data;

@Data
public class ClassificationMetadata {

    private String provider;
    private String model;
    private String modelVersion;
    private String promptVersion;
    private Double temperature;
}
