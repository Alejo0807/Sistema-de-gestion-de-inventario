package com.example.sw2.controller.gestor;

import com.example.sw2.constantes.AsignadosSedesId;
import com.example.sw2.constantes.VentasId;
import com.example.sw2.entity.AsignadosSedes;
import com.example.sw2.entity.Inventario;
import com.example.sw2.entity.Usuarios;
import com.example.sw2.entity.Ventas;
import com.example.sw2.repository.AsignadosSedesRepository;
import com.example.sw2.repository.InventarioRepository;
import com.example.sw2.repository.UsuariosRepository;
import com.example.sw2.repository.VentasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/gestor/productosDisponibles")

public class ProductosDisponiblesController {

    @Autowired
    InventarioRepository inventarioRepository;
    @Autowired
    VentasRepository ventasRepository;
    @Autowired
    AsignadosSedesRepository asignadosSedesRepository;
    @Autowired
    UsuariosRepository usuariosRepository;

    @GetMapping(value ="")
    public String listarProductosDisponibles(Model model){
        model.addAttribute("listainventario",inventarioRepository.findAll());
        return "gestor/productosDisponibles";
    }


    @GetMapping(value="venta")
    public String ventasDeProductos(@ModelAttribute("ventas") Ventas ventas, Model model, @RequestParam("x") String x){
        Optional<Inventario> optionalInventario = inventarioRepository.findById(x);
        if ( optionalInventario.isPresent()){
            Inventario inventario=optionalInventario.get();
            model.addAttribute("inv", inventario);
            return "gestor/productosDisponiblesForm";
        }
        return "redirect:/gestor/productosDisponibles";
    }

    @PostMapping("/registrarventa")
    public String registrarventa(@RequestParam("tipodocumento") int tipodocumento,
                                 @RequestParam("documento") String documento,
                                 @ModelAttribute("ventas") @Valid Ventas ventas, BindingResult bindingResult,
                                 Model model, RedirectAttributes attributes,
                                 HttpSession session) {
        if ((bindingResult.hasErrors()) || (documento=="") || (ventas.getFecha() == null ) || (ventas.getCantidad() > ventas.getInventario().getCantidadgestor()) ) {
            model.addAttribute("inv", ventas.getInventario());
            if(documento == "") { model.addAttribute("msg", "Debe ingresar un número de documento"); }
            if(ventas.getFecha()==null){model.addAttribute("msg1", "Debe seleccionar una fecha");}
            if(ventas.getCantidad() > ventas.getInventario().getCantidadgestor()){model.addAttribute("msg2", "No hay suficientes productos en stock");}
            return "gestor/productosDisponiblesForm";

        }else {
            Inventario inventario = ventas.getInventario();
            inventario.setCantidadgestor( inventario.getCantidadgestor() - ventas.getCantidad());
            inventario.setCantidadtotal( inventario.getCantidadtotal() - ventas.getCantidad());
            inventarioRepository.save(inventario);
            Usuarios usuarios = (Usuarios) session.getAttribute("usuario");
            ventas.setId(new VentasId(tipodocumento, documento));
            ventas.setVendedor(usuarios);
            ventas.setFechacreacion(LocalDateTime.now());
            ventasRepository.save(ventas);
            attributes.addFlashAttribute("msg", "Venta de producto realizada");
            return "redirect:/gestor/productosDisponibles";
        }
    }


    @GetMapping(value="asignar")
    public String asignarProducto(@ModelAttribute("asignadosSedes") AsignadosSedes asignadosSedes ,Model model, @RequestParam("x") String x){
        Optional<Inventario> optionalInventario = inventarioRepository.findById(x);
        if ( optionalInventario.isPresent()){
            Inventario inventario=optionalInventario.get();
            model.addAttribute("inv", inventario);
            model.addAttribute("listasedes",usuariosRepository.findUsuariosByRoles_idroles(3));
            return "gestor/productosDisponiblesAsignar";
        }
        return "redirect:/gestor/productosDisponibles";
    }



    @PostMapping("/resgistrarasignacion")
    public String registrarAsignacionProducto(@RequestParam("sede") int sede,
                                              @RequestParam("precioventa") String precioventa,
                                              @RequestParam("inventario") String inventario,
                                              @ModelAttribute("asignadosSedes") @Valid AsignadosSedes asignadosSedes, BindingResult bindingResult,
                                              Model model, RedirectAttributes attributes,
                                              HttpSession session) {
        Optional<Inventario> optionalInventario = inventarioRepository.findById(inventario);
        Inventario inv = optionalInventario.get();

        if ((bindingResult.hasErrors()) || (precioventa == "") || (asignadosSedes.getFechaenvio() == null) || (asignadosSedes.getStock() > inv.getCantidadgestor())) {
            model.addAttribute("inv", inv);
            model.addAttribute("listasedes",usuariosRepository.findUsuariosByRoles_idroles(3));
            if(precioventa == "") { model.addAttribute("msg", "Debe ingresar un precio de venta"); }
            if(asignadosSedes.getFechaenvio() == null ){ model.addAttribute("msg1", "Debe seleccionar una fecha"); }
            if(asignadosSedes.getStock() > inv.getCantidadgestor()){ model.addAttribute("msg2", "No hay suficientes productos para asignar");}
            return "gestor/productosDisponiblesAsignar";

        } else {
            inv.setCantidadgestor(inv.getCantidadgestor()-asignadosSedes.getStock());
            inventarioRepository.save(inv);
            Usuarios usuarios = (Usuarios) session.getAttribute("usuario");
            Optional<Usuarios> optionalUsuarios = usuariosRepository.findById(sede);
            Usuarios sedes = optionalUsuarios.get();
            Float precio = Float.parseFloat(precioventa);
            asignadosSedes.setId(new AsignadosSedesId(usuarios, sedes, inv,1,precio));
            asignadosSedes.setCantidadactual(asignadosSedes.getStock());
            asignadosSedes.setFechacreacion(LocalDateTime.now());
            System.out.println( "La puta fecha sin asignar es " + asignadosSedes.getFechaenvio());
            asignadosSedesRepository.save(asignadosSedes);

            attributes.addFlashAttribute("msg", "Producto asignado exitosamente");
            return "redirect:/gestor/productosDisponibles";
        }
    }


}
