package edu.uclm.esi.listasbe.services;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.protocol.BasicHttpContext;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.springframework.stereotype.Service;

@Service
public class ProxyBEU {
		public String validar(String token) {
			String url = "http://localhost:8000/tokens/validar";
			try (CloseableHttpClient httpClient = HttpClients.createDefault()){
				HttpPut httpPut = new HttpPut(url);
				httpPut.setEntity(new StringEntity(token));
				httpPut.setHeader("Content-Type", "text/plain");
				HttpContext context = new BasicHttpContext();
				try (CloseableHttpResponse response = httpClient.execute(httpPut,context)) {
	                String email = EntityUtils.toString(response.getEntity());
	                return email;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		public boolean premium(String email) {
		    String url = "http://localhost:8000/users/premium";
		    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
		        HttpPut httpPut = new HttpPut(url); // Cambié a PUT
		        httpPut.setEntity(new StringEntity(email));
		        httpPut.setHeader("Content-Type", "text/plain");
		        HttpContext context = new BasicHttpContext();
		        
		        try (CloseableHttpResponse response = httpClient.execute(httpPut, context)) {
		            // Leer el cuerpo de la respuesta
		            String responseBody = EntityUtils.toString(response.getEntity());

		            // Convertir la respuesta a un valor booleano (suponiendo que el servidor responde "true" o "false" como texto)
		            return Boolean.parseBoolean(responseBody); // Devuelve el valor booleano según la respuesta del servidor
		        }
		    } catch (Exception e) {
		        throw new RuntimeException("Error al conectar con el servicio externo: " + e.getMessage());
		    }
		}

}
