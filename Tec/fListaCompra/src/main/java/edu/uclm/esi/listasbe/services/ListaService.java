package edu.uclm.esi.listasbe.services;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.listasbe.dao.ListaDao;
import edu.uclm.esi.listasbe.dao.ProductoDao;
import edu.uclm.esi.listasbe.model.Lista;
import edu.uclm.esi.listasbe.model.Producto;
import edu.uclm.esi.listasbe.ws.WSListas;

@Service
public class ListaService {

	@Autowired
	private ListaDao listaDao;
	@Autowired
	private ProductoDao productoDao;
	@Autowired
	private ProxyBEU proxy;
	@Autowired
	private WSListas wsListas;

	public Lista crearLista(String nombre, String token) {
	    String email = this.proxy.validar(token);
	    int propietario = 0;
	    List<String> listas = this.listaDao.getListasDe(email);
	    for (String listaId : listas) {
	        List<String> emails = this.listaDao.getEmailsDeLista(listaId);
	        if (emails.get(0).equals(email)) {
	            propietario++;
	        }
	    }
	    if (!this.proxy.premium(email) && propietario >= 2) {
	        throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, "No es usuario premium");
	    }
	    
	    Lista lista = new Lista();
	    lista.setNombre(nombre);
	    lista.addEmailUsuarioPendiente(email);  // El creador debería estar pendiente
	    this.listaDao.save(lista);
	    
	    // Se confirma al creador
	    lista.confirmarUsuario(email);
	    this.listaDao.save(lista);

	    return lista;
	}

	public Lista addProducto(String idLista, Producto producto, String token) {
		Optional<Lista> optlista = this.listaDao.findById(idLista);
		String email = this.proxy.validar(token);
		if (optlista.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encuentra la lista");
		Lista lista = optlista.get();
		int productosaniadidos = this.productoDao.countProductosByEmailAndIdLista(email, idLista);		if (this.proxy.premium(email)== false && productosaniadidos >= 10) {
			throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, "No es usuario premium");
		}
		lista.add(producto);
		producto.setLista(lista);
		producto.setUsuario(email);
		this.productoDao.save(producto);
		this.wsListas.notificar(idLista, producto,"nuevo",email);
		return lista;
	}

	public List<Lista> getListas(String email) {
	    List<Lista> result = new ArrayList<>();
	    // Imprimir el email recibido como parámetro para verificar cuál estamos usando
	    System.out.println("Recibiendo email para obtener listas: " + email);

	    List<String> ids = this.listaDao.getListasDe(email); // Este método obtiene las listas en las que está el email
	    System.out.println("IDs de las listas asociadas al email: " + ids);

	    // Filtrar las listas con al menos un email confirmado
	    for (String id : ids) {
	        System.out.println("Procesando lista con ID: " + id);
	        Lista lista = this.listaDao.findById(id).orElse(null);
	        
	        if (lista != null) {
	            // Si la lista es válida, mostramos sus detalles
	            System.out.println("Lista encontrada: " + lista.getNombre() + ", ID: " + lista.getId());

	            if (lista.esUsuarioConfirmado(email)) {
	                System.out.println("El email " + email + " está confirmado en la lista " + lista.getNombre());
	                result.add(lista); // Agregamos la lista si el email está confirmado
	            } else {
	                System.out.println("El email " + email + " NO está confirmado en la lista " + lista.getNombre());
	            }
	        } else {
	            System.out.println("Lista con ID " + id + " no encontrada.");
	        }
	    }
	    
	    // Imprimir el número de listas que se van a devolver
	    System.out.println("Número de listas con el email confirmado: " + result.size());
	    
	    return result;
	}



	
	public String addInvitado(String idLista, String email) {
	    Optional<Lista> optLista = this.listaDao.findById(idLista);
	    if (optLista.isEmpty())
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encuentra la lista");
	    Lista lista = optLista.get();
	    List<String> emails = this.listaDao.getEmailsDeLista(idLista);
        System.out.println("El propietario es"+emails.get(0));
        if (emails.size() >= 2 && this.proxy.premium(email) == false) {
            throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, "No eres usuario premium");
        }else {
	    lista.addEmailUsuarioPendiente(email);
	    this.listaDao.save(lista);
        }
	    // Obtener el nombre de la lista y el email del usuario que invita
	    String nombreLista = lista.getNombre();
	    String invitadorEmail = lista.getCreadorEmail(); // Cambiar según tu implementación

	    // Generar la URL para el frontend
	    String url = "https://localhost:4200/invitaciones?email=" + email + 
	                 "&listaId=" + idLista + 
	                 "&listaNombre=" + nombreLista + 
	                 "&invitadorEmail=" + invitadorEmail;
	    return url;
	}

	public void aceptarInvitacion(String idLista, String email) {
	    System.out.println("Recibiendo solicitud de aceptación de invitación:");
	    System.out.println("ID de la lista: " + idLista);
	    System.out.println("Email del usuario: " + email);
	    
	    Optional<Lista> optLista = this.listaDao.findById(idLista);
	    if (optLista.isEmpty()) {
	        System.out.println("Lista con ID " + idLista + " no encontrada.");
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encuentra la lista");
	    }
	    Lista lista = optLista.get();
	    System.out.println("Lista encontrada: " + lista.getNombre());

	    // Verificar si el email está en la lista de pendientes
	    if (lista.esUsuarioPendiente(email)) {
	        System.out.println("El email " + email + " está en la lista de pendientes.");
	    } else {
	        System.out.println("El email " + email + " NO está en la lista de pendientes.");
	    }

	    // Confirmar usuario
	    try {
	        lista.confirmarUsuario(email);
	        this.listaDao.save(lista);  // Guardamos la lista con el email confirmado
	        System.out.println("Email " + email + " confirmado y movido a la lista de confirmados.");
	    } catch (IllegalArgumentException e) {
	        System.out.println("Error al confirmar el email " + email + ": " + e.getMessage());
	    }

	    // Confirmar también en la DAO (base de datos)
	    try {
	        this.listaDao.confirmar(idLista, email);  // Mover el email a confirmados en la base de datos
	        System.out.println("El email " + email + " se ha confirmado en la base de datos.");
	    } catch (Exception e) {
	        System.out.println("Error al confirmar el email en la base de datos: " + e.getMessage());
	    }
	}

	public void eliminarLista(String idLista, String email) {
        Optional<Lista> optlista = this.listaDao.findById(idLista);
        if (optlista.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encuentra la lista");
        Lista lista = optlista.get();
        List<String> emails = this.listaDao.getEmailsDeLista(idLista);
        if (emails.get(0).equals(email)) {
            this.listaDao.delete(lista);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No eres el dueño de la lista");
        }
    }

	public List<Producto> verDetalles(String idLista) {
		return this.productoDao.verDetalles(idLista);
		
	}

}