package com.neflis.twitchbot;

import java.util.ArrayList;
import java.util.List;

import com.github.philippheuer.events4j.api.domain.IDisposable;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;

public class AmbotgUs {

	private String estado;
	private List<String> jugadores;
	
	public AmbotgUs() {
		this.estado = "inicial";
		this.jugadores = new ArrayList<String>();
		
	}
	
	public void init (TwitchClient twitchClient, String canal) {
    			
    	if(!twitchClient.getChat().isChannelJoined(canal)) {
    		twitchClient.getChat().joinChannel(canal);
    	}

		System.out.println("inicio");
		IDisposable eventHandler = twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class).onEvent(ChannelMessageEvent.class, event -> {
        	String nombre = event.getUser().getName();
        	String mensaje = event.getMessage();
        	if(mensaje.equals("!unirse")) {
        		if(!jugadores.contains(nombre)) {
	        		jugadores.add(nombre);
	        		twitchClient.getChat().sendMessage(canal, nombre + " se ha unido a la partida");
        		}else {
        			twitchClient.getChat().sendMessage(canal, nombre + " ya estas unido");
        		}
        	}
    	});
    	try {
    		twitchClient.getChat().sendMessage(canal, "Empieza la partida, unanse a la cola para jugar usando\n '!unirse' (tiempo 1 minuto)");
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		twitchClient.getChat().sendMessage(canal, "Cerramos las inscripciones.");
		eventHandler.dispose();
	}
	
	
	
	
	
	/**
	 * GETTERS Y SETTERS
	 */
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public List<String> getJugadores() {
		return jugadores;
	}
	public void setJugadores(List<String> jugadores) {
		this.jugadores = jugadores;
	}
	
	
	
	
}
