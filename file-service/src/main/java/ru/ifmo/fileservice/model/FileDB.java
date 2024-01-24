package ru.ifmo.fileservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@Table(name = "files")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FileDB {
    
    @Id
    private Long id;

    @NotBlank
    private String uuid;

    @NotBlank
    private String filename;

    private Long userId;

    @NotNull
    private byte[] data;
}
