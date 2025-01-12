package edu.uclm.esi.listasbe.services;
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
public class ProductoService {

	@Autowired
	private ProductoDao productoDao;	
	@Autowired
	private WSListas wsListas;
	
	public Producto comprar(String idProducto, float unidadesCompradas, String email) {
		String idLista = this.productoDao.obtenerListaIdPorProductoId(idProducto);
		this.productoDao.comprar(idProducto, unidadesCompradas);
		Producto producto = this.productoDao.obtenerProductoPorId(idProducto);
		this.wsListas.notificar(idLista, producto , "actualizar",email);
		return producto;
	}

	public void eliminarProducto(String idProducto, String email) {
		Producto producto = this.productoDao.obtenerProductoPorId(idProducto);
		String idLista = this.productoDao.obtenerListaIdPorProductoId(idProducto);
		this.productoDao.delete(producto);
		this.wsListas.notificar(idLista, producto, "eliminar",email);
	}
	
	public Producto crearProducto(String nombre, float unidadesCompradas, float unidadesPedidas, String usuario) {
	    Producto producto = new Producto();
	    producto.setNombre(nombre);
	    producto.setUnidadesCompradas(unidadesCompradas);
	    producto.setUnidadesPedidas(unidadesPedidas);
	    producto.setUsuario(usuario);
	    this.productoDao.save(producto);
	    return producto;
	}

	public Producto actualizar(String idProducto, float unidadesCompradas, float unidadesPedidas, String email) {
		String idLista = this.productoDao.obtenerListaIdPorProductoId(idProducto);
		this.productoDao.actualizar(idProducto,unidadesCompradas,unidadesPedidas);
		Producto producto = this.productoDao.obtenerProductoPorId(idProducto);
		this.wsListas.notificar(idLista, producto, "actualizar", email);
		return null;
	}

	public Producto pedir(String idProducto, float unidadesPedidas, String email) {
		String idLista = this.productoDao.obtenerListaIdPorProductoId(idProducto);
		this.productoDao.pedir(idProducto,unidadesPedidas);
		Producto actualizado = this.productoDao.obtenerProductoPorId(idProducto);
		this.wsListas.notificar(idLista, actualizado, "actualizar",email);
		return null;
	}

}