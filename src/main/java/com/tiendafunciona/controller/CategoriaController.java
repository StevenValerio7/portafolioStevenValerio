package com.tiendafunciona.controller;
import com.tiendafunciona.domain.Categoria;
import com.tiendafunciona.service.CategoriaService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String modificarCategoria(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Categoria> categoriaOpt = categoriaService.getCategoria(id);
        if (categoriaOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "La categoría no existe.");
            return "redirect:/categoria/listado";
        }
        model.addAttribute("categoria", categoriaOpt.get());
        return "categoria/modificar";
    }
    
    @PostMapping("/categoria/eliminar")
    public String eliminarCategoria(@RequestParam("idCategoria") Long idCategoria, RedirectAttributes redirectAttributes) {
        try {
            categoriaService.delete(idCategoria);
            redirectAttributes.addFlashAttribute("todoOk", "Categoría eliminada correctamente.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "La categoría no existe.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", "No se puede eliminar la categoría. Tiene productos asociados.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la categoría.");
        }
        return "redirect:/categoria/listado";
    }
}