package com.neflis.twitchbot;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.api.domain.IDisposable;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.helix.domain.FollowList;
import com.github.twitch4j.kraken.domain.KrakenUserList;

@SpringBootApplication
public class TwitchbotApplication {

	static Properties prop = new Properties();
	static String cuenta = "neflisBot";
	static String canal = "neflis";
	static int contador = 0;
	
	public static void main(String[] args) {

		
		try {
			prop.load(new FileInputStream("twitch.properties"));
		} catch(IOException e) {
			System.out.println(e.toString());
		}

		String username = prop.getProperty("twitch."+cuenta+".username");
		String token = prop.getProperty("twitch."+cuenta+".token");
		String refreshToken = prop.getProperty("twitch"+cuenta+".refreshtoken");
		String appClientId = prop.getProperty("twitch.app.clientid");
		String appSecreto = prop.getProperty("twitch.app.secreto");
				
		OAuth2Credential credential = new OAuth2Credential("twitch", token);
		
		TwitchClient twitchClient = initTwitchClient(credential);
		
		KrakenUserList misDatos = twitchClient.getKraken().getUsersByLogin(Arrays.asList(username)).execute();
		KrakenUserList datosDelCanal = twitchClient.getKraken().getUsersByLogin(Arrays.asList(canal)).execute();
		
		String myId = misDatos.getUsers().get(0).getId();
		String idCanal = datosDelCanal.getUsers().get(0).getId();
		
		//OAuth2Credential credential = new OAuth2Credential("twitch", token, refreshToken, null, username, null, null);

		/*CredentialManager credentialManager = CredentialManagerBuilder.builder().build();
		credentialManager.registerIdentityProvider(new TwitchIdentityProvider(appClientId, appSecreto, "http://localhost"));
		//credentialManager.addCredential("neflis", credential);

		System.out.println(credentialManager.getCredentials().get(0).getUserId());*/
    	
    	//twitchClient.getChat().joinChannel(canal);
    	
    	/*twitchClient.getPubSub().listenForFollowingEvents(credential, idCanal);
    	twitchClient.getEventManager().onEvent(FollowingEvent.class, System.out::println);*/
    	    
    	/*twitchClient.getPubSub().listenForChannelPointsRedemptionEvents(credential, idCanal);
    	twitchClient.getEventManager().onEvent(RewardRedeemedEvent.class, System.out::println);*/
    	
    	//listaDeSeguidores(token, idCanal, twitchClient);
    	//leerChat(twitchClient, canal);    	
    	//twitchClient.getHelix().createFollow(token, myId, idCanal, true).execute();
		//twitchClient.getChat().sendMessage(canal, "BOOOM! Baneaso");
    	    	
    	//twitchClient.getChat().sendPrivateMessage(canal, "holi2");
    	AmbotgUs partida = new AmbotgUs();
    	partida.init(twitchClient, canal);
    	System.out.println("fin");
	}

	private static void leerChat(TwitchClient twitchClient, String canal) {
		
    	twitchClient.getChat().joinChannel(canal);
    	twitchClient.getChat().connect();
    	
    	SimpleEventHandler eventHandler = twitchClient.getEventManager().getEventHandler(SimpleEventHandler.class);
    	
    	eventHandler.onEvent(ChannelMessageEvent.class, event -> {
        	String nombre = event.getUser().getName();
        	String mensaje = event.getMessage();
        	/*if(mensaje.contains("https://clck")) {
        		twitchClient.getChat().ban(canal, nombre, "Bot de spam a mi...");
        		twitchClient.getChat().sendMessage(canal, "BOOOM! Baneaso");
        	}
        	if(mensaje.equals("!muerte +")) {
        		contador += 1;
        		twitchClient.getChat().sendMessage(canal, "Sefi ha muerto una vez mas, van: "+ contador);
        	}
        	if(mensaje.equals("!muerte -")) {
        		contador -= 1;
        		twitchClient.getChat().sendMessage(canal, "Me he flipao, ahora no ha muerto joe... Van: "+ contador);
        	}
        	if(mensaje.equals("!muertes")) {
        		twitchClient.getChat().sendMessage(canal, "Sefi ha manqueado "+ contador+" veces");
        	}
        	if(mensaje.equals("!reset")) {
        		contador = 0;
        		twitchClient.getChat().sendMessage(canal, "Pa ke toka illo, ahora esta el contador a 0 picha...");
        	}*/
    		System.out.println("[" + event.getChannel().getName() + "] " + event.getUser().getName() + ": " + event.getMessage());
    	});
		
	}

	private static void listaDeSeguidores(String token, String idCanal, TwitchClient twitchClient) {
    	
		FollowList resultList = null;
		resultList = twitchClient.getHelix().getFollowers(token, null, idCanal, null, 25).execute();
			//System.out.println(resultList.getPagination());
		while(resultList != null){
			
	    	resultList.getFollows().forEach(follow -> {
	    	    System.out.println(follow);
	    	});	
	    	resultList = twitchClient.getHelix().getFollowers(token, null, idCanal, resultList.getPagination().getCursor(), 25).execute();
		}
			
    	/*KrakenFollowList resultList = twitchClient.getKraken().getChannelFollowers(idCanal, 25, 0, null, null).execute();
    	resultList.getTotal().toString();
    	resultList.getFollows().forEach(sub -> { 
    		System.out.println(sub.toString());
    	});*/
    	
    	//System.out.println(resultList.getFollows().get(1));		
	}
	
	private static TwitchClient initTwitchClient(OAuth2Credential credential) {
		
		TwitchClient twitchClient = TwitchClientBuilder.builder()
	            .withEnableChat(true)
	            .withEnableHelix(true)
	            .withEnableKraken(true)
	            //.withEnablePubSub(true)
	            .withChatAccount(credential)
	            .withDefaultEventHandler(SimpleEventHandler.class)
	            .build();
		
		return twitchClient;
	}

}

