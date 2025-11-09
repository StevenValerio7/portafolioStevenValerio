package com.tiendafunciona.service;

import com.tiendafunciona.domain.Categoria;
import com.tiendafunciona.repository.CategoriaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author steve
 */
@Service
public class CategoriaService {
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
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