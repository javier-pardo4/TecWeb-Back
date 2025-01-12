package edu.uclm.esi.fakeaccountsbe.http;

import edu.uclm.esi.fakeaccountsbe.dao.UserDao;
import edu.uclm.esi.fakeaccountsbe.model.User;
import edu.uclm.esi.fakeaccountsbe.services.EmailService;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("email")
@CrossOrigin("*")
public class EmailController {

    @Autowired
    private EmailService emailService;
    
	@Autowired //incluimos esto para los DAO siempre
	private UserDao userDao;
	
    @PostMapping("/confirmar-premium")
    public String enviarCorreoPremium(@RequestParam String email) {
        // Mensaje de ejemplo. Personaliza este mensaje según lo que desees enviar.
        String mensaje = "¡Gracias por suscribirte a la versión premium de nuestra aplicación! "
                + "Ahora tienes acceso a funciones exclusivas.";
        emailService.enviarCorreo(email, "Confirmación de Suscripción Premium", mensaje);
        return "Correo de confirmación premium enviado a: " + email;
    }
    
    @PostMapping("/enviar-invitacion")
    public void enviarCorreoInvitacion(@RequestParam String email, @RequestParam String lista, @RequestParam String url) {
        // Construir el mensaje
        String mensaje = "Has sido invitado a participar en la lista '" + lista + "'.\n\n";
        mensaje += "Haz clic en el siguiente enlace para aceptar la invitación:\n" + url;

        // Llamar a tu servicio de correos (EmailService)
        emailService.enviarCorreo(email, "Invitación a Lista", mensaje);
    }

    @PostMapping("/recuperar-contraseña")
    public String enviarCorreoRecuperarContraseña(@RequestParam String email) {
        // Buscar al usuario en la base de datos por correo electrónico
    	Optional<User> optUser=this.userDao.findById(email);

	    // Si no se encuentra al usuario, devolver false
	    if (!optUser.isPresent()) {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
	    }

	    // Obtener el usuario y verificar si es premium
	    User user = optUser.get();

        // Generar un token único y guardarlo en el usuario
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        userDao.save(user);

        // Crear el enlace de restablecimiento
        String resetLink = "https://localhost:4200/reset-password?token=" + token;

        // Mensaje del correo
        String mensaje = "Hola,\n\nHas solicitado restablecer tu contraseña. Por favor, haz clic en el siguiente enlace para cambiar tu contraseña:\n\n"
                + resetLink + "\n\nSi no solicitaste este cambio, ignora este correo.";

        // Enviar el correo
        emailService.enviarCorreo(email, "Restablecimiento de contraseña", mensaje);

        return "Correo de restablecimiento enviado a: " + email;
    }

}
