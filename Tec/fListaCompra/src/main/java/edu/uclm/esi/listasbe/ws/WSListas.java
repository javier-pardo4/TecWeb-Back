package edu.uclm.esi.listasbe.ws;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import edu.uclm.esi.listasbe.model.Producto;

@Component
public class WSListas extends TextWebSocketHandler {

	private Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
	private Map<String, List<WebSocketSession>> sessionsByIdLista = new ConcurrentHashMap<>();

	public void notificar(String idLista, Producto producto, String accion, String email) {
		JSONObject jso = new JSONObject();
		jso.put("tipo", "actualizacionDeLista");
		jso.put("idLista", idLista);
		jso.put("accion", accion);

		if (!accion.equals("eliminar")) {
			jso.put("idProducto", producto.getId());
			jso.put("unidadesCompradas", producto.getUnidadesCompradas());
			jso.put("unidadesPedidas", producto.getUnidadesPedidas());
			if (accion.equals("nuevo")) {
				jso.put("usuario",email);
				jso.put("nombre",producto.getNombre());
			}
		} else {
			jso.put("idProducto", producto.getId());
		}
		TextMessage message = new TextMessage(jso.toString());
		List<WebSocketSession> interesados = this.sessionsByIdLista.get(idLista);

		if (interesados != null) {
			for (WebSocketSession session : interesados) {
				String emailSession = this.getParameter(session, "email");
				if (!emailSession.equals(email)) {
					try {
						session.sendMessage(message);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		String idLista = this.getParameter(session, "idLista");
		List<WebSocketSession> auxi = this.sessionsByIdLista.get(idLista);
		if (auxi == null) {
			auxi = new ArrayList<>();
			auxi.add(session);
		} else {
			auxi.add(session);
		}
		this.sessionsByIdLista.put(idLista, auxi);
	}

	private String getParameter(WebSocketSession session, String parName) {
		URI uri = session.getUri();
		String query = uri.getQuery();
		for (String param : query.split("&")) {
			String[] pair = param.split("=");
			if (pair.length > 1 && parName.equals(pair[0])) {
				return pair[1];
			}
		}
		return null;
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		this.sessions.remove(session.getId());
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
	}

}