package com.tiendafunciona.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "categoria")
public class Categoria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Long idCategoria; // Usamos Long, consistente con el repositorio

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(max = 50, message = "La descripción no puede exceder los 50 caracteres")
    @Column(length = 50, nullable = false, unique = true)
    private String descripcion;

    @Size(max = 1024, message = "La ruta de la imagen no puede exceder los 1024 caracteres")
    @Column(name = "ruta_imagen", length = 1024)
    private String rutaImagen;

    @Column(nullable = false)
    private boolean activo;

    @Column(name = "fecha_creacion", updatable = false, insertable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion", insertable = false, updatable = false)
    private LocalDateTime fechaModificacion;

    public Categoria() {}

    public Categoria(String descripcion, boolean activo) {
        this.descripcion = descripcion;
        this.activo = activo;
    }
}
