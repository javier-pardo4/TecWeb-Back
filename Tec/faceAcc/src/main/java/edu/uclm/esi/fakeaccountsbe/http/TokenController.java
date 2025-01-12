package edu.uclm.esi.fakeaccountsbe.http;

import java.util.Collection;
import java.util.Random;
import java.util.UUID;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.fakeaccountsbe.dao.UserDao;
import edu.uclm.esi.fakeaccountsbe.model.CredencialesRegistro;
import edu.uclm.esi.fakeaccountsbe.model.User;
import edu.uclm.esi.fakeaccountsbe.services.UserService;


@RestController
@RequestMapping("tokens")
@CrossOrigin("*")
public class TokenController {
	
	@Autowired
	private UserDao userDao;
	
    @PutMapping("/validar")
    public ResponseEntity<String> validar(@RequestBody String token) {
        // Imprimir el token recibido
        System.out.println("Token recibido: " + token);

        // Buscar el usuario en la base de datos usando el token
        User user = userDao.findByToken(token); // Buscamos por el identificador de cookie (token)

        // Imprimir si el usuario es encontrado o no
        if (user == null) {
            System.out.println("No se encontró usuario para el token: " + token);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Token no válido");
        }

        // Si el usuario existe, devolver el email
        System.out.println("Usuario encontrado: " + user.getEmail());
        
        return ResponseEntity.ok(user.getEmail()); // Responder con el email en el cuerpo de la respuesta
    }
}
















