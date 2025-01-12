package edu.uclm.esi.listasbe.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;

@Entity
public class Lista {
	@Id @Column(length = 36)
	private String id;
	@Column(length = 80)
	private String nombre;
	
	@OneToMany (mappedBy = "lista")
	private List<Producto> productos;
	
	
	@ElementCollection
	private List<String> emailsUsuarios;
	
    @ElementCollection
    private List<String> emailsUsuariosPendientes;

    @ElementCollection
    private List<String> emailsUsuariosConfirmados;
	
	
	public Lista() {
		this.id = UUID.randomUUID().toString();
		this.productos = new ArrayList<>();
		this.emailsUsuarios = new ArrayList<>();
        this.emailsUsuariosPendientes = new ArrayList<>();
        this.emailsUsuariosConfirmados = new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void add(Producto producto) {
		this.productos.add(producto);
	}
	
	public List<Producto> getProductos() {
		return productos;
	}
	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}

	public List<String> getEmailsUsuarios() {
		return emailsUsuarios;
	}

	public void setEmailsUsuarios(List<String> emailsUsuarios) {
		this.emailsUsuarios = emailsUsuarios;
	}


	public void addEmailUsuario(String email) {
		this.emailsUsuarios.add(email);
		
	}

    public boolean esUsuarioPendiente(String email) {
        return this.emailsUsuariosPendientes.contains(email);
    }

    public boolean esUsuarioConfirmado(String email) {
        return this.emailsUsuariosConfirmados.contains(email);
    }
    
    public List<String> getEmailsUsuariosPendientes() {
        return emailsUsuariosPendientes;
    }

    public List<String> getEmailsUsuariosConfirmados() {
        return emailsUsuariosConfirmados;
    }
    
    
    
    public void addEmailUsuarioPendiente(String email) {
        if (!this.emailsUsuarios.contains(email)) {
            this.emailsUsuariosPendientes.add(email);
            this.emailsUsuarios.add(email);
        } else {
            throw new IllegalArgumentException("El usuario ya está asociado a la lista.");
        }
    }
	
    public void confirmarUsuario(String email) {
        if (this.emailsUsuariosPendientes.contains(email)) {
            this.emailsUsuariosPendientes.remove(email);  // Eliminas el email de pendientes
            this.emailsUsuariosConfirmados.add(email);  // Y lo agregas a confirmados
        } else {
            throw new IllegalArgumentException("El email proporcionado no está en la lista de pendientes.");
        }
    }

    
	public void confirmar(String idLista, String email) {
	    if (this.id.equals(idLista)) {
	        if (this.emailsUsuarios.contains(email)) {
	            return;
	        } else {
	            throw new IllegalArgumentException("El email proporcionado no está asociado a esta lista.");
	        }
	    } else {
	        throw new IllegalArgumentException("El ID de la lista no coincide con el ID de esta lista.");
	    }
	}

	public String getCreadorEmail() {
	    if (emailsUsuarios != null && !emailsUsuarios.isEmpty()) {
	        return emailsUsuarios.get(0); // Retorna el primer correo en la lista
	    }
	    return null; // Si la lista está vacía o es nula, devuelve null
	}

	
}
