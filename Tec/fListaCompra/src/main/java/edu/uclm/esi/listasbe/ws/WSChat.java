package edu.uclm.esi.listasbe.ws;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class WSChat extends TextWebSocketHandler {

    private Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private Map<String, String> listaIds = new ConcurrentHashMap<>(); // Asociar sesión con idLista
    private Map<String, WebSocketSession> sessionsByNombre = new ConcurrentHashMap<>();

    public void difundir(JSONObject json, String idLista, String senderSessionId) throws IOException {
        TextMessage message = new TextMessage(json.toString());
        for (WebSocketSession target : this.sessions.values()) {
            String targetIdLista = this.listaIds.get(target.getId());

            // Verificar que el usuario está en la misma lista y que no es el remitente
            if (idLista.equals(targetIdLista) && !target.getId().equals(senderSessionId)) {
                new Thread(() -> {
                    try {
                        target.sendMessage(message);
                    } catch (IOException e) {
                        WSChat.this.sessions.remove(target.getId());
                    }
                }).start();
            }
        }
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Conexión establecida: " + session.getId());

        String nombreUsuario = this.getQueryParameter(session, "nombre");
        String idLista = this.getQueryParameter(session, "idLista");

        if (nombreUsuario == null || idLista == null) {
            session.close(CloseStatus.BAD_DATA);
            return;
        }

        this.sessions.put(session.getId(), session);
        this.sessionsByNombre.put(nombreUsuario, session);
        this.listaIds.put(session.getId(), idLista);

        JSONObject jso = new JSONObject();
        jso.put("tipo", "llegadaDelUsuario");
        jso.put("contenido", nombreUsuario);
        this.difundir(jso, idLista, session.getId());
    }

    private String getQueryParameter(WebSocketSession session, String parameterName) {
        URI uri = session.getUri();
        String query = uri.getQuery();
        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            if (pair.length > 1 && parameterName.equals(pair[0])) {
                return pair[1];
            }
        }
        return null;
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        this.sessions.remove(session.getId());
        this.listaIds.remove(session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JSONObject jso = new JSONObject(message.getPayload());
        String idLista = this.listaIds.get(session.getId());

        // Verificar el tipo de mensaje
        if (jso.getString("tipo").equalsIgnoreCase("difusion")) {
            // Obtener el sessionId del remitente
            String senderSessionId = session.getId();

            // Preparar el mensaje de difusión
            jso.put("tipo", "mensajeDeTexto");
            jso.put("contenido", jso.getString("contenido"));

            // Llamar a difundir, pasando el senderSessionId para evitar que reciba su propio mensaje
            this.difundir(jso, idLista, senderSessionId);
        } else if (jso.getString("tipo").equalsIgnoreCase("mensajeParticular")) {
            String destinatario = jso.getString("destinatario");
            WebSocketSession wsDestinatario = this.sessionsByNombre.get(destinatario);
            String destinatarioIdLista = this.listaIds.get(wsDestinatario.getId());

            if (wsDestinatario != null && idLista.equals(destinatarioIdLista)) {
                wsDestinatario.sendMessage(message);
            }
        }
    }


    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
    }
}