package edu.uclm.esi.listasbe.http;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.listasbe.model.Lista;
import edu.uclm.esi.listasbe.model.Producto;
import edu.uclm.esi.listasbe.services.ListaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("listas")
@CrossOrigin(origins = "https://localhost:4200", allowCredentials = "true")
public class ListaController {
	@Autowired
	private ListaService listaService;

	@PostMapping("/crearLista")
	public Lista crearLista(@RequestBody String nombre, @RequestHeader("Authorization") String authorizationHeader) {
		nombre = nombre.trim();

		if (nombre.isEmpty())
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre no puede estar vacio");
		if (nombre.length() > 80)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"El nombre de la lista esta limitado a 80 caracteres");

		String token = null;
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			token = authorizationHeader.substring(7);
		}
		return this.listaService.crearLista(nombre, token);
	}

	@PostMapping("/eliminarLista")
    public void eliminarLista(HttpServletRequest request, @RequestBody String email) {
        String idLista = request.getHeader("idLista");
        this.listaService.eliminarLista(idLista, email);
    }

	@PostMapping("/addProducto")
	public Lista addProducto(HttpServletRequest request, @RequestBody Producto producto,
			@RequestHeader("Authorization") String authorizationHeader) {
		if (producto.getNombre().isEmpty())
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre no puede estar vacio");

		if (producto.getNombre().length() > 80)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"El nombre de la lista esta limitado a 80 caracteres");

		String idLista = request.getHeader("idLista");
		String token = null;
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			token = authorizationHeader.substring(7);
		}
		return this.listaService.addProducto(idLista, producto, token);
	}

	@GetMapping("/getListas")
	public List<Lista> getListas(@RequestParam String email) {
		return this.listaService.getListas(email);
	}

	@PostMapping("/addInvitado")
	public String addInvitado(HttpServletRequest request, @RequestBody String email) {
		String idLista = request.getHeader("idLista");
		return this.listaService.addInvitado(idLista, email);
	}

	@PostMapping("/aceptarInvitacion")
	public ResponseEntity<String> aceptarInvitacion(@RequestBody Map<String, String> datos) {
	    try {
	        String idLista = datos.get("idLista");
	        String email = datos.get("email");

	        // Lógica del servicio
	        this.listaService.aceptarInvitacion(idLista, email);

	        // Respuesta exitosa con un mensaje claro
	        return ResponseEntity
	                .ok()
	                .header("Content-Type", "application/json")
	                .body("{\"message\": \"Invitación aceptada exitosamente\"}");
	    } catch (Exception e) {
	        // Si ocurre un error
	        e.printStackTrace();
	        return ResponseEntity
	                .status(500)
	                .header("Content-Type", "application/json")
	                .body("{\"message\": \"Error al procesar la invitación\"}");
	    }
	}



	@GetMapping("/verDetalles")
	public List<Producto> verDetalles(HttpServletRequest request) {
		String idLista = request.getHeader("idLista");
		System.out.println(idLista);
		return this.listaService.verDetalles(idLista);
	}

}