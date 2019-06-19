package com.analizador;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TratarJSON {

	private String contenido;
	public TratarJSON (String resultado) {
		this.contenido=resultado;
	}
	public List<JSONArray> tratarJSONPoS() throws ParseException{
		List<JSONObject> frasesPorParrafos=new ArrayList<JSONObject>();
		List<JSONArray> tokensPorFrase=new ArrayList<JSONArray>();
		JSONParser parser = new JSONParser();
		JSONObject obj = (JSONObject) parser.parse(this.contenido);
		int numParrafos=((JSONArray)obj.get("paragraphs")).size();//numero de parrafos del texto que hemos mandado al servidor

		for(int i=0; i< numParrafos; i++){
			frasesPorParrafos.add((JSONObject)((JSONArray) obj.get("paragraphs") ).get(i));//Obtenemos las frases
			//Necesitamos guardar en una lista una lista de tokens por frase

			JSONArray aux = (JSONArray) frasesPorParrafos.get(i).get("sentences");
			for(int j=0; j< aux.size();j++){
				tokensPorFrase.add((JSONArray) ((JSONObject) aux.get(j)).get("tokens"));


			}

		}	
		return tokensPorFrase;

	}
	public List<JSONArray> tratarJSONDependencias() throws ParseException{
		JSONParser parser = new JSONParser();
		JSONObject resultadoConsulta = (JSONObject) parser.parse(this.contenido);
		JSONArray parrafos = (JSONArray) resultadoConsulta.get("paragraphs");
		//System.out.println("parrafos:" + parrafos);
		List<JSONArray> hijos =new ArrayList<JSONArray>();
		//System.out.println("parrafos tama�:" + parrafos.size());
		for(int i=0; i<parrafos.size();i++){
			
			JSONObject aux=(JSONObject) parrafos.get(0);
			//System.out.println("aux:" + aux);//Esto maybe con un for porque podemos tener mas de una oraci�n
			JSONArray oraciones=(JSONArray) aux.get("sentences");
			//System.out.println("oraciones:" + oraciones);//Esto maybe con un for porque podemos tener mas de una oraci�n
			for(int j=0; j< oraciones.size(); j++){
				aux=(JSONObject)oraciones.get(j);
				JSONArray dependencias= (JSONArray) aux.get("dependencies");
				aux= (JSONObject) dependencias.get(0);
				hijos.add((JSONArray) aux.get("children"));	
			}
		}		
		return hijos;

	}
	public JSONObject tratarJSONEntidades() throws ParseException{
		JSONParser parser = new JSONParser();
		JSONObject resultadoConsulta = (JSONObject) parser.parse(this.contenido);
		JSONArray parrafos=(JSONArray) resultadoConsulta.get("paragraphs");
		JSONObject aux = (JSONObject) parrafos.get(0);
		JSONArray oraciones= (JSONArray) aux.get("sentences");
		aux = (JSONObject) oraciones.get(0);
		JSONArray tokens = (JSONArray) aux.get("tokens");
		JSONObject entidad=(JSONObject) tokens.get(0);
		return entidad;
	}
	public String getContenido() {
		return contenido;
	}
	public void setContenido(String contenido) {
		this.contenido = contenido;
	}
}
