package com.tiendafunciona.service;
import com.tiendafunciona.domain.Categoria;
import com.tiendafunciona.repository.CategoriaRepository;
import com.tiendafunciona.service.FirebaseStorageService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CategoriaService {
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Autowired
    private FirebaseStorageService firebaseStorageService;
    
    @Transactional(readOnly = true)
    public List<Categoria> getCategorias(boolean activo) {
        if (activo) {
            return categoriaRepository.findByActivoTrue();
        }
        return categoriaRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Optional<Categoria> getCategoria(Long idCategoria) {
        return categoriaRepository.findById(idCategoria);
    }
    
    @Transactional
    public void save(Categoria categoria, MultipartFile imagenFile) {
        categoria = categoriaRepository.save(categoria);
        if (!imagenFile.isEmpty()) { //Si no está vacío... pasaron una imagen...
            String rutaImagen = firebaseStorageService.cargaImagen(
                imagenFile, "categoria",
                categoria.getIdCategoria());
            categoria.setRutaImagen(rutaImagen);
            categoriaRepository.save(categoria);
        }
    }
    
    @Transactional
    public void delete(Long idCategoria) {
        // Verifica si la categoria existe antes de intentar eliminarla
        if (!categoriaRepository.existsById(idCategoria)) {
            // Lanza una excepción para indicar que la categoria no fue encontrada
            throw new IllegalArgumentException("La categoria con ID " + idCategoria + " no existe.");
        }
        try {
            categoriaRepository.deleteById(idCategoria);
        } catch (DataIntegrityViolationException e) {
            // Lanza una nueva excepción para encapsular el problema de integridad de datos
            throw new IllegalStateException("No se puede eliminar la categoria. Tiene productos asociados.", e);
        }
    }
}