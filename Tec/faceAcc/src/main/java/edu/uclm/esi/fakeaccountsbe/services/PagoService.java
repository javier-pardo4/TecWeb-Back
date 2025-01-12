package edu.uclm.esi.fakeaccountsbe.services;

import java.time.LocalDate;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import edu.uclm.esi.fakeaccountsbe.dao.UserDao;
import edu.uclm.esi.fakeaccountsbe.model.User;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import edu.uclm.esi.fakeaccountsbe.dao.UserDao;
import edu.uclm.esi.fakeaccountsbe.model.User;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

@Service
public class PagoService {
	
    @Autowired
    private UserDao userDao;

    // Inicializar la clave de Stripe
    static {
        // Leer la clave desde las variables cargadas por EnvLoader
        String stripeSecretKey = System.getProperty("STRIPE_SECRET_KEY");
        if (stripeSecretKey == null || stripeSecretKey.isEmpty()) {
            throw new IllegalStateException("La clave STRIPE_SECRET_KEY no está configurada");
        }
        Stripe.apiKey = stripeSecretKey;
    }
    

    // Método para preparar la transacción (crear el PaymentIntent)
    public String prepararTransaccion(long importe) {
        // Configura los parámetros del PaymentIntent
        PaymentIntentCreateParams params = new PaymentIntentCreateParams.Builder()
                .setCurrency("eur")
                .setAmount(importe) // Monto en centavos
                .build();
        PaymentIntent intent;
        try {
            // Crea el PaymentIntent a través de la API de Stripe
            intent = PaymentIntent.create(params);

            // Obtén el client_secret del PaymentIntent
            JSONObject jso = new JSONObject(intent.toJson());
            String clientSecret = jso.getString("client_secret");

            // Retorna el client_secret necesario para completar el pago en el cliente
            return clientSecret;
        } catch (StripeException e) {
            // Si ocurre un error, lanza una excepción con un código de error HTTP
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error al preparar la transacción", e);
        }
    }
    
    // Método para confirmar la transacción de Stripe y actualizar el estado del usuario
    public void confirmarTransaccion(String paymentIntentId, String email) {
        try {
            // Recuperar el PaymentIntent usando el ID de Stripe
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);

            // Verificar que el pago ha sido exitoso
            if ("succeeded".equals(paymentIntent.getStatus())) {
                // El pago fue exitoso, actualizar el estado del usuario
                User user = userDao.findById(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
                user.setPagado(true); // Marcar al usuario como pagado
                user.setFechaPago(LocalDate.now()); // Establecer la fecha del pago como la fecha actual
                userDao.save(user); // Guardar los cambios en la base de datos
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pago no completado");
            }
        } catch (StripeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error al procesar el pago", e);
        }
    }

}