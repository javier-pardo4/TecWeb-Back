package edu.uclm.esi.listasbe.dao;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import edu.uclm.esi.listasbe.model.Lista;
import edu.uclm.esi.listasbe.model.Producto;

public interface ProductoDao extends CrudRepository<Producto,String>{
	@Query(value = "SELECT COUNT(*) FROM producto WHERE usuario = :email AND lista_id = :idLista", nativeQuery = true)
    int countProductosByEmailAndIdLista( String email, String idLista);

    @Query(value = "SELECT * FROM producto WHERE lista_id = :idLista", nativeQuery = true)
    List<Producto> verDetalles(String idLista);

    @Modifying
    @Transactional
    @Query(value = "UPDATE producto SET unidades_compradas = :unidadesCompradas WHERE id = :idProducto", nativeQuery = true)
    void comprar(String idProducto,float unidadesCompradas);

    @Query(value = "SELECT * FROM producto WHERE id = :idProducto", nativeQuery = true)
    Producto obtenerProductoPorId(String idProducto);

    @Modifying
    @Transactional
    @Query(value = "UPDATE producto SET unidades_compradas = :unidadesCompradas, unidades_pedidas = :unidadesPedidas WHERE id = :idProducto", nativeQuery = true)
    void actualizar(String idProducto,float unidadesCompradas, float unidadesPedidas);


    @Modifying
    @Transactional
    @Query(value = "UPDATE producto SET  unidades_pedidas = :unidadesPedidas WHERE id = :idProducto", nativeQuery = true)
    void pedir(String idProducto, float unidadesPedidas);


    @Query(value = "SELECT lista_id FROM producto WHERE id = :idProducto", nativeQuery = true)
    String obtenerListaIdPorProductoId(String idProducto);

    @Query(value = "SELECT usuario FROM producto WHERE id = :idProducto", nativeQuery = true)
    String obtenerEmailPorId(String idProducto);

}