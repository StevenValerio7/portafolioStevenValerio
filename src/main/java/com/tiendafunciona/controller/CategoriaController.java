package com.tiendafunciona.controller;

import com.tiendafunciona.domain.Categoria;
import com.tiendafunciona.service.CategoriaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CategoriaController {
    
    @Autowired
    private CategoriaService categoriaService;
    
    @GetMapping("/categoria/listado")
    public String listar(Model model) {
        List<Categoria> lista = categoriaService.getCategorias(false);
        model.addAttribute("categorias", lista);
        return "categoria/listado";
    }
    
    @GetMapping("/categoria/nuevo")
    public String nuevaCategoria(Categoria categoria) {
        return "categoria/modificar";
    }
    
    @PostMapping("/categoria/guardar")
    public String guardarCategoria(Categoria categoria) {
        categoriaService.save(categoria);
        return "redirect:/categoria/listado";
    }
    
    @GetMapping("/categoria/modificar/{id}")
    public String modificarCategoria(@PathVariable("id") Long id, Model model) {
        Categoria categoria = new Categoria();
        categoria.setIdCategoria(id);
        categoria = categoriaService.getCategoria(categoria);
        model.addAttribute("categoria", categoria);
        return "categoria/modificar";
    }
    
    @GetMapping("/categoria/eliminar/{id}")
    public String eliminarCategoria(@PathVariable("id") Long id) {
        Categoria categoria = new Categoria();
        categoria.setIdCategoria(id);
        categoriaService.delete(categoria);
        return "redirect:/categoria/listado";
    }
}