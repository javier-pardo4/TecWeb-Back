package edu.uclm.esi.fakeaccountsbe.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.uclm.esi.fakeaccountsbe.config.ErrorResponse;
import edu.uclm.esi.fakeaccountsbe.dao.UserDao;
import edu.uclm.esi.fakeaccountsbe.model.User;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.fakeaccountsbe.dao.UserDao;
import edu.uclm.esi.fakeaccountsbe.model.User;

import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class UserService {
		
	@Autowired //incluimos esto para los DAO siempre
	private UserDao userDao;
	
	@Autowired
	private EmailService emailService;
	
	//private Map<String, User> users = new ConcurrentHashMap<>();
	//private Map<String, List<User>> usersByIp = new ConcurrentHashMap<>();

	public ResponseEntity<String> registrar(String ip, User user) {
	    if (this.userDao.findById(user.getEmail()).isPresent()) {
	        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ya existe un usuario con ese correo electrónico");
	    }

	    user.setIp(ip);
	    user.setCreationTime(System.currentTimeMillis());
	    user.setConfirmado(false); // Inicialmente no está confirmado
	    user.setToken(UUID.randomUUID().toString()); // Generar un token único para confirmación
	    this.userDao.save(user);

	    // Lógica para enviar el correo de confirmación
	    String linkConfirmacion = "https://localhost:4200/confirm-account?token=" + user.getToken();
	    String mensaje = "Por favor, confirma tu cuenta haciendo clic en el siguiente enlace:\n\n" + linkConfirmacion;
	    emailService.enviarCorreo(user.getEmail(), "Confirmación de cuenta", mensaje);
	    
	    return ResponseEntity.ok()
	            .contentType(MediaType.TEXT_PLAIN)
	            .body(linkConfirmacion);
	}


	public void login(User tryingUser) {
		this.find(tryingUser.getEmail(), tryingUser.getPwd());
	}

	public void clearAll() {
		//this.usersByIp.clear();
		//this.users.clear();
		this.userDao.deleteAll();
	}

	
	public Iterable<User> getAllUsers() {
		//return this.users.values();
		return this.userDao.findAll();
	}

	public User find(String email, String pwd) {
	    Optional<User> optUser = this.userDao.findById(email);

	    if (!optUser.isPresent()) {
	        // Usuario no encontrado, enviamos un error detallado
	        try {
	            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN.value(), "Credenciales incorrectas");
	            String jsonError = new ObjectMapper().writeValueAsString(errorResponse);
	            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Credenciales incorrectas", new Throwable(jsonError));
	        } catch (JsonProcessingException e) {
	            // Manejo de la excepción de serialización
	            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al procesar el mensaje de error", e);
	        }
	    }

	    User user = optUser.get();
	    if (!user.getPwd().equals(pwd)) {
	        // Contraseña incorrecta, enviamos un error detallado
	        try {
	            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN.value(), "Credenciales incorrectas");
	            String jsonError = new ObjectMapper().writeValueAsString(errorResponse);
	            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Credenciales incorrectas", new Throwable(jsonError));
	        } catch (JsonProcessingException e) {
	            // Manejo de la excepción de serialización
	            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al procesar el mensaje de error", e);
	        }
	    }

	    if (!user.isConfirmado()) {
	        // Cuenta no confirmada, enviamos un error detallado
	        try {
	            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN.value(), "La cuenta no está confirmada");
	            String jsonError = new ObjectMapper().writeValueAsString(errorResponse);
	            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La cuenta no está confirmada", new Throwable(jsonError));
	        } catch (JsonProcessingException e) {
	            // Manejo de la excepción de serialización
	            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al procesar el mensaje de error", e);
	        }
	    }

	    return user;
	}

	public void delete(String email) {
		//User user = this.users.remove(email);
		//List<User> users = this.usersByIp.get(user.getIp());
		//users.remove(user);
		//if (users.isEmpty())
		//	this.usersByIp.remove(user.getIp());
		
		this.userDao.deleteById(email);
	}

	public boolean comprobarPremium(String email) {
	    // Buscar al usuario por su email
	    Optional<User> optUser = this.userDao.findById(email);

	    // Si no se encuentra al usuario, devolver false
	    if (!optUser.isPresent()) {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
	    }

	    // Obtener el usuario y verificar si es premium
	    User user = optUser.get();
	    return user.isPagado(); // Devuelve true si es premium, false si no lo es
	}

	
	
	public synchronized void clearOld() {
		//long time = System.currentTimeMillis();
		//for (User user : this.users.values())
		//	if (time> 600_000 + user.getCreationTime())
		//		this.delete(user.getEmail());
	}
	
}













