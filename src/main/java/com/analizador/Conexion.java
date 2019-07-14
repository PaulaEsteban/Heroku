package com.analizador;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class Conexion {

	// service base URL
	static final String url = "http://frodo.lsi.upc.edu:8080/TextWS/textservlet/ws/processQuery/";
	private String service;
	static final int BUFFERSIZE = 2048;
	private String contenido;
	private String texto;
	private String lang = "es";
	private String out = "json";
	private String user = "paulaesteban";
	private String pwd = "TFGPaula2019!";
	private boolean activo=true;//Si la conexion va bien no se cambiará, sino se pondra a false y se subirá hasta el servicio para dar un error interno 500

	public Conexion(String texto, String servicio) throws IOException {
		this.texto=texto;
		this.service=servicio;
		// Create request, fill query parameters
		HttpPost request = new HttpPost(url+service);
		request.setEntity(MultipartEntityBuilder.create()
				.addTextBody("username", this.user)
				.addTextBody("password", this.pwd)
				.addTextBody("text_input", this.texto)
				.addTextBody("language", this.lang)
				.addTextBody("output", this.out)
				.addTextBody("interactive", "1")
				.build()
				);

		// create client, send request, get response
		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse response = client.execute(request);
		String content = EntityUtils.toString(response.getEntity());

		// handle connection errors
		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
			//System.out.println(response.getStatusLine() + " - " + content);
			//System.exit(1);
			activo=false;
		}else{
		this.setContenido(content);
		}
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}
}


