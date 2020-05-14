package com.example.sw2.controller.gestor;


import com.example.sw2.constantes.CustomConstants;
import com.example.sw2.entity.Productos;
import com.example.sw2.repository.ProductosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/gestor/producto")
public class ProductoController {


    @Autowired
    ProductosRepository productosRepository;


    @GetMapping(value = {""})
    public String listar(@ModelAttribute("producto") Productos productos, Model model) {
        model.addAttribute("lista", productosRepository.findAll());
        model.addAttribute("lineas", CustomConstants.getLineas());
        return "gestor/productos";
    }

    @PostMapping("/save")
    public String editar(@ModelAttribute("producto") @Valid Productos productos,
                          BindingResult bindingResult, RedirectAttributes attr, Model model) {
        if(bindingResult.hasErrors()){
            model.addAttribute("lista", productosRepository.findAll());
            model.addAttribute("msg", "ERROR");
            model.addAttribute("lineas", CustomConstants.getLineas());
            return "gestor/productos";
        }
        else {
            Optional<Productos> optionalProductos = productosRepository.findById(productos.getCodigonom());
            if (optionalProductos.isPresent()) {
                attr.addFlashAttribute("msg", "Categoría actualizada exitosamente");
            }
            else {
                attr.addFlashAttribute("msg", "Categoría creada exitosamente");
            }
            productosRepository.save(productos);
            return "redirect:/gestor/producto";
        }
    }

    @GetMapping("/delete")
    public String borrar(Model model,
                            @RequestParam("codigonom") String id,
                            RedirectAttributes attr) {
        Optional<Productos> c = productosRepository.findById(id);
        if (c.isPresent()) {
            productosRepository.deleteById(id);
            attr.addFlashAttribute("msg","Categoría borrada exitosamente");
        }
        return "redirect:/gestor/producto";
    }

    //Web service
    @ResponseBody
    @GetMapping(value = "/get",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Optional<Productos>> getCat(@RequestParam(value = "id") String id){
        return new ResponseEntity<>(productosRepository.findById(id), HttpStatus.OK);
    }

}
