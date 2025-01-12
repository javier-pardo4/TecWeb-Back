package edu.uclm.esi.listasbe.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import edu.uclm.esi.listasbe.model.Lista;
import jakarta.transaction.Transactional;

public interface ListaDao extends CrudRepository<Lista, String>{
	
	@Query(value="SELECT lista_id from lista_emails_usuarios WHERE emails_usuarios=:email", nativeQuery=true)
	List<String> getListasDe(String email);
	
	@Query(value="SELECT emails_usuarios from lista_emails_usuarios WHERE lista_id = :listaId", nativeQuery=true)
	List<String> getEmailsDeLista(String listaId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE lista_emails_usuarios SET confirmado = TRUE WHERE lista_id = :idLista AND emails_usuarios = :email", nativeQuery = true)
    void confirmar(String idLista, String email);

}