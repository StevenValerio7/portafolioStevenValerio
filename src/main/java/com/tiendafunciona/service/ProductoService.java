package com.tiendafunciona.service;
import com.tiendafunciona.domain.Producto;
import com.tiendafunciona.repository.ProductoRepository;
import com.tiendafunciona.service.FirebaseStorageService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductoService {
    //Permite crear una única instancia de ProductoRepository, y la crea automáticamente
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private FirebaseStorageService firebaseStorageService;
    
    @Transactional(readOnly = true)
    public List<Producto> getProductos(boolean activo) {
        if (activo) { //Sólo activos...
            return productoRepository.findByActivoTrue();
        }
        return productoRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Optional<Producto> getProducto(Integer idProducto) {
        return productoRepository.findById(idProducto);
    }
    
    @Transactional
    public void save(Producto producto, MultipartFile imagenFile) {
        producto = productoRepository.save(producto);
        if (!imagenFile.isEmpty()) { //Si no está vacío... pasaron una imagen...
            String rutaImagen = firebaseStorageService.cargaImagen(
                imagenFile, "producto",
                producto.getIdProducto().longValue());
            producto.setRutaImagen(rutaImagen);
            productoRepository.save(producto);
        }
    }
    
    @Transactional
    public void delete(Integer idProducto) {
        // Verifica si el producto existe antes de intentar eliminarlo
        if (!productoRepository.existsById(idProducto)) {
            // Lanza una excepción para indicar que el producto no fue encontrado
            throw new IllegalArgumentException("El producto con ID " + idProducto + " no existe.");
        }
        try {
            productoRepository.deleteById(idProducto);
        } catch (DataIntegrityViolationException e) {
            // Lanza una nueva excepción para encapsular el problema de integridad de datos
            throw new IllegalStateException("No se puede eliminar el producto. Tiene datos asociados.", e);
        }
    }
}