package edu.uclm.esi.listasbe.http;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.listasbe.model.Lista;
import edu.uclm.esi.listasbe.model.Producto;
import edu.uclm.esi.listasbe.services.ProductoService;
import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("productos")
@CrossOrigin("*")	
public class ProductoController {	
	
		@Autowired
		private ProductoService productoService;
		
		
		@PutMapping("/actualizar")
		public Producto actualizar(@RequestBody Map<String, Object> compra) {
		    String idProducto = (String) compra.get("idProducto");
		    float unidadesCompradas = Float.parseFloat(compra.get("unidadesCompradas").toString());
		    float unidadesPedidas = Float.parseFloat(compra.get("unidadesPedidas").toString());
		    String email = (String) compra.get("user");
		    return this.productoService.actualizar(idProducto, unidadesCompradas, unidadesPedidas, email);
		}
		
		
		@PutMapping("/pedir")
		public Producto pedir(@RequestBody Map<String, Object> compra) {
		    String idProducto = (String) compra.get("idProducto");
		    float unidadesPedidas = Float.parseFloat(compra.get("unidadesPedidas").toString());
		    String email = (String) compra.get("user");
		    return this.productoService.pedir(idProducto, unidadesPedidas, email);
		}
		
		@PutMapping("/comprar")
		public Producto comprar(@RequestBody Map<String, Object> compra) {
		    String idProducto = (String) compra.get("idProducto");
		    float unidadesCompradas = Float.parseFloat(compra.get("unidadesCompradas").toString());
		    String email = (String) compra.get("user");
		    return this.productoService.comprar(idProducto, unidadesCompradas, email);
		}
		
		@PostMapping("/eliminarproducto")
		public void eliminarProducto(HttpServletRequest request, @RequestBody String email) {
			String idProducto = request.getHeader("idProducto");
			System.out.println(idProducto);
			this.productoService.eliminarProducto(idProducto, email);
		}
		
		@PostMapping("/crearProducto")
		public Producto crearProducto(@RequestBody Map<String, Object> payload) {
		    String nombre = ((String) payload.get("nombre")).trim();
		    float unidadesCompradas = Float.parseFloat(payload.get("unidadesCompradas").toString());
		    float unidadesPedidas = Float.parseFloat(payload.get("unidadesPedidas").toString());
		    String usuario = ((String) payload.get("usuario")).trim();
		    if (nombre.isEmpty()) {
		        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre no puede estar vacío");
		    }
		    if (nombre.length() > 80) {
		        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del producto está limitado a 80 caracteres");
		    }

		    return this.productoService.crearProducto(nombre, unidadesCompradas, unidadesPedidas, usuario);
		}

	}
