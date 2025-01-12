package edu.uclm.esi.listasbe.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration; 
import org.springframework.web.socket.config.annotation.EnableWebSocket; 
import org.springframework.web.socket.config.annotation.WebSocketConfigurer; 
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry; 
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor; 
 
@Configuration 
@EnableWebSocket 
public class WSConfigurer implements WebSocketConfigurer {
@Autowired 
private WSListas wsListas;
 @Override 
 public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) { 
  registry. 
   addHandler(new WSChat(), "/wsChat").addHandler( wsListas ,"/wsListas").
   setAllowedOrigins("*"). 
   addInterceptors(new HttpSessionHandshakeInterceptor()); 
 } 
}