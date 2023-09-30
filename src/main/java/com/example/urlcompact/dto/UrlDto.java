package com.example.urlcompact.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class UrlDto implements Serializable {
    @NotEmpty
    String fullUrl;
    String compactUrl;
}
