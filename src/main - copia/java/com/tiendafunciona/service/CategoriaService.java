package com.tiendafunciona.service;

import com.tiendafunciona.domain.Categoria;
import com.tiendafunciona.repository.CategoriaRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CategoriaService {
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    // Ruta donde se guardarán las imágenes
    private final String UPLOAD_DIR = "src/main/resources/static/images/categorias/";
    
    @Transactional(readOnly = true)
    public List<Categoria> getCategorias(boolean activo) {
        if (activo) {
            return categoriaRepository.findByActivoTrue();
        }
        return categoriaRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Categoria getCategoria(Categoria categoria) {
        return categoriaRepository.findById(categoria.getIdCategoria()).orElse(null);
    }
    
    @Transactional
    public void save(Categoria categoria) {
        categoriaRepository.save(categoria);
    }
    
    // MÉTODO NUEVO para guardar con imagen
    @Transactional
    public void save(Categoria categoria, MultipartFile imagenFile) {
        if (imagenFile != null && !imagenFile.isEmpty()) {
            try {
                // Crear directorio si no existe
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                
                // Generar nombre único para la imagen
                String nombreArchivo = System.currentTimeMillis() + "_" + imagenFile.getOriginalFilename();
                Path rutaArchivo = Paths.get(UPLOAD_DIR + nombreArchivo);
                
                // Guardar archivo
                Files.write(rutaArchivo, imagenFile.getBytes());
                
                // Guardar solo la ruta en la base de datos
                categoria.setRutaImagen("/images/categorias/" + nombreArchivo);
                
            } catch (IOException e) {
                System.err.println("Error al guardar la imagen: " + e.getMessage());
                e.printStackTrace();
            }
        }
        categoriaRepository.save(categoria);
    }
    
    @Transactional
    public boolean delete(Categoria categoria) {
        try {
            categoriaRepository.delete(categoria);
            categoriaRepository.flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}