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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class CategoriaController {
    
    @Autowired
    private CategoriaService categoriaService;
    
    @GetMapping("/categoria/listado")
    public String listar(Model model) {
        List<Categoria> lista = categoriaService.getCategorias(false);
        model.addAttribute("categorias", lista);
        model.addAttribute("totalCategorias", lista.size());
        return "categoria/listado";
    }
    
    @GetMapping("/categoria/nuevo")
    public String nuevaCategoria(Categoria categoria) {
        return "categoria/modificar";
    }
    
    @PostMapping("/categoria/guardar")
    public String guardarCategoria(Categoria categoria, 
                                  @RequestParam(value = "imagenFile", required = false) MultipartFile imagenFile) {
        categoriaService.save(categoria, imagenFile);
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
