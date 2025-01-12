package edu.uclm.esi.fakeaccountsbe.http;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import jakarta.servlet.http.Cookie;
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

import java.util.UUID;

import jakarta.servlet.http.Cookie;
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
@RequestMapping("users")
@CrossOrigin(origins = { "https://localhost:4200" }, allowCredentials = "true" )
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	private UserDao userDao;
	
	@GetMapping("/checkCookie")
	public String checkCookie(HttpServletRequest request) {
		String fakeUserId = this.findCookie(request, "fakeUserId");
		if (fakeUserId!=null) {
			User user = this.userDao.findByCookie(fakeUserId);
			if (user!=null) {
				user.setToken(UUID.randomUUID().toString());
				this.userDao.save(user);
				return user.getToken();
			}
		}
		return null;
	}

	
	@PostMapping("/registrar1")
	public ResponseEntity<String> registrar1(HttpServletRequest req, @RequestBody CredencialesRegistro cr) {
		cr.comprobar();
		User user = new User();
		user.setEmail(cr.getEmail());
		user.setPwd(cr.getPwd1());
		
		return this.userService.registrar(req.getRemoteAddr(), user);
	}
	
	@GetMapping("/registrar2")
	public void registrar2(HttpServletRequest req, @RequestParam String email, @RequestParam String pwd1, @RequestParam String pwd2) {
		CredencialesRegistro cr = new CredencialesRegistro();
		cr.setEmail(email);
		cr.setPwd1(pwd1);
		cr.setPwd2(pwd2);
		cr.comprobar();
		User user = new User();
		user.setEmail(cr.getEmail());
		user.setPwd(cr.getPwd1());
		
		this.userService.registrar(req.getRemoteAddr(), user);
	}
	
	@GetMapping("/registrarMuchos")
	public void registrarMuchos(HttpServletRequest req, @RequestParam String name, @RequestParam Integer n) {
		for (int i=0; i<n; i++)
			this.registrar2(req, name + i + "@pepe.com", "Pepe1234", "Pepe1234");
	}
	
	@PutMapping("/login1")
	public String login1(HttpServletResponse response, HttpServletRequest request, @RequestBody User user) {
		String fakeUserId=this.findCookie(request,"fakeUser");
		
		if(fakeUserId==null) {
			user=this.userService.find(user.getEmail(), user.getPwd());
			fakeUserId=UUID.randomUUID().toString();
			Cookie cookie = new Cookie("fakeUserId", fakeUserId);
			cookie.setMaxAge(3600*24*365);
			cookie.setPath("/");
			cookie.setAttribute("SameSite", "None");
			cookie.setSecure(true);
			response.addCookie(cookie);

			user.setCookie(fakeUserId);
			user.setToken(UUID.randomUUID().toString());
			this.userDao.save(user);
		}else {
			user=this.userDao.findByCookie(fakeUserId);
			if(user!=null) {
				user.setToken(UUID.randomUUID().toString());
				this.userDao.save(user);
			}else {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Cookie caducada");
			}
		}
		return user.getToken();
	}
	
	
	private String findCookie(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		if (cookies== null) {
			return null;
		}
		for (int i=0; i<cookies.length; i++) {
			if(cookies[i].getName().equals(cookieName))
				return cookies[i].getValue();
		}
		return null;
	}
	
	@GetMapping("/login2")
	public User login2(HttpServletResponse response, @RequestParam String email, @RequestParam String pwd) {
		User user = this.userService.find(email, pwd);
		user.setToken(UUID.randomUUID().toString());
		response.setHeader("token", user.getToken());
		return user;
	}
	
	@GetMapping("/login3/{email}")
	public User login3(HttpServletResponse response, @PathVariable String email, @RequestParam String pwd) {
		return this.login2(response, email, pwd);
	}
	
	@GetMapping("/getAllUsers")
	public Iterable<User>  getAllUsers() {
		return this.userService.getAllUsers();
	}
	
	@DeleteMapping("/delete")
	public void delete(HttpServletRequest request, @RequestParam String email, @RequestParam String pwd) {
		User user = this.userService.find(email, pwd);
		
		String token = request.getHeader("token");
		if (!token.equals(user.getToken()))
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Token " + token + " inválido");
		
		this.userService.delete(email);
	}
	
	@DeleteMapping("/clearAll")
	public void clearAll(HttpServletRequest request) {
		String sToken = request.getHeader("prime");
		Integer token = Integer.parseInt(sToken);
		if (!isPrime(token.intValue()))
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Debes pasar un número primo en la cabecera");
		if (sToken.length()!=3)
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El nº primo debe tener tres cifras");
		this.userService.clearAll();
	}
	
	@PutMapping("/premium")
	public boolean comprobarPremium(@RequestBody String email) {
	    // Llamar al servicio para verificar si el usuario es premium
	    boolean esPremium = this.userService.comprobarPremium(email);
	    
	    // Si el usuario es premium, devolver un 200 OK
	    if (esPremium) {
	        return true;
	    } else {
	        // Si no es premium, devolver un 403 Forbidden
	        return false;
	    }
	}
	

	@PutMapping("/restablecer-password")
	public ResponseEntity<String> restablecerContraseña(
	    @RequestParam String email, 
	    @RequestParam String nuevaContraseña,
	    @RequestParam String confirmarContraseña) {

	    // Buscar el usuario por email
	    Optional<User> optUser = userDao.findById(email);
	    
	    if (!optUser.isPresent()) {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
	    }

	    User user = optUser.get();

	    // Verificar que las contraseñas coincidan
	    if (!nuevaContraseña.equals(confirmarContraseña)) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las contraseñas no coinciden");
	    }

	    // Actualizar la contraseña
	    user.setPwd(nuevaContraseña);
	    userDao.save(user);

	    return ResponseEntity.ok("Contraseña actualizada correctamente");
	}

	@GetMapping("/confirmar-cuenta")
	public ResponseEntity<String> confirmarCuenta(@RequestParam String token) {
        User user = userDao.findByToken(token); // Buscamos por el identificador de cookie (token)

	    user.setConfirmado(true); // Marcar la cuenta como confirmada
	    user.setToken(null); // Eliminar el token de confirmación para evitar reuso
	    userDao.save(user);

	    return ResponseEntity.ok("Cuenta confirmada exitosamente");
	}

	
	
	private boolean isPrime(int n) {
	    if (n <= 1) return false;
	    for (int i = 2; i <= Math.sqrt(n); i++) {
	        if (n % i == 0) return false;
	    }
	    return true;
	}
}















