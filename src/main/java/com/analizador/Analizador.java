package com.analizador;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;

import com.controller.Main;
import com.google.gson.Gson;

public class Analizador {
	private List<String> frases;
	public Analizador(String texto){
		frases= new ArrayList<String>();
		String elemento="";
		for (int i = 0; i <texto.length (); i++) {
			if(texto.charAt(i)!='.'){
				elemento+=texto.charAt(i);
			}else{
				elemento+=texto.charAt(i);
				frases.add(elemento);
				elemento="";
			}
		}
	}
	public List<String> getFrases() {
		return frases;
	}
	public List<Integer> pasiva(String texto) throws IOException, ParseException{
		Conexion contagger= new Conexion(texto,"tagger");
		if(!contagger.isActivo()){
			List<Integer> error=new ArrayList<Integer>();
			error.add(-1);
			return error;
		}
		TratarJSON tratar=new TratarJSON(contagger.getContenido());
		int[] contadorVerbos= new int[tratar.tratarJSONPoS().size()];
		List<Integer> pasivas = new ArrayList<Integer>();
		List<List<JSONObject>> verbos=new ArrayList<List<JSONObject>>();


		for(int i=0; i<tratar.tratarJSONPoS().size(); i++){
			JSONArray fraseComprobando=(JSONArray)tratar.tratarJSONPoS().get(i);
			List<JSONObject> verbosI=new ArrayList<JSONObject>();
			for(int j=0; j< fraseComprobando.size(); j++){
				JSONObject elemento = (JSONObject) fraseComprobando.get(j);
				if(elemento.get("pos")!="null"){
					String aux=(String) elemento.get("pos");
					if(aux.equals("verb")){
						contadorVerbos[i]++;
						verbosI.add(elemento);
					}
				}

			}
			verbos.add(verbosI);
		}

		for(int i=0; i<verbos.size(); i++){
			List<JSONObject> verbosI=verbos.get(i);
			if(contadorVerbos[i]==2){//Podr�a ser una pasiva tenemos que comprabar si los verbos que hay es un verbo ser y un participio
				if(verbosI.get(0).get("lemma").equals("ser")&&
						verbosI.get(1).get("mood").equals("participle")){//Comprobamos que el segundo es un participio
					pasivas.add(i);
					//Aqu� habr�a que devolver algo, maybe un boolean si nos da igual la cantidad de pasivas que hay sino el numero de ellas
					//A lo mejor incluso habr�a que devolver las dos cosas
				}
			}else if(contadorVerbos[i]==3){//Si fuera una pasiva deber�a ser haber+ser+participio
				if(verbosI.get(0).get("lemma").equals("haber")&&verbosI.get(0).get("type").equals("auxiliary")&& 
						verbosI.get(1).get("lemma").equals("ser")&&verbosI.get(2).get("mood").equals("participle")){
					pasivas.add(i);
				}else{//Aqui podr�a estar el caso de ser una pasiva del caso de nverbos=2 pero que algo m�s se haya etiquetado como verbo
					boolean encontrarSer=false;
					while(!encontrarSer & !verbosI.isEmpty()){//Mientras no se encuentre vamos eliminando verbos de la lista que esten por delante del haber
						if(verbosI.get(0).get("lemma").equals("ser")&&verbosI.get(0).get("type").equals("semiauxiliary")){
							encontrarSer=true;
						}else{
							verbosI.remove(0);//No es el verbo ser 
						}
					}
					if(encontrarSer&&verbosI.get(1).get("mood").equals("participle")){//Si hemos encontrado el ser tenemos que ver si el siguiente verbo es un participio, el resto que quede no nos importa
						pasivas.add(i);
					}
				}
			}else if(contadorVerbos[i]>=4){

				//Aqu� podr�a caer alguna pasiva de las de 3 verbos que tenga algo que no es un verbo anotado como tal
				//Lo mismo para las de dos verbos
				boolean encontrarSer=false;
				boolean encontrarHaber=false;
				while(!encontrarSer&&!encontrarHaber){//Mientras no se encuentre vamos eliminando verbos de la lista que esten por delante del ser o haber
					//Lo primero que los podr�amos encontrar de lo que nos intera es el haber 
					if(verbosI.get(0).get("lemma").equals("haber")&&verbosI.get(0).get("type").equals("auxiliary")){
						encontrarHaber=true;

					}else if(verbosI.get(0).get("lemma").equals("ser")&&verbosI.get(0).get("type").equals("semiauxiliary")){
						encontrarSer=true;
					}else{
						verbosI.remove(0);//No es el verbo ser 
					}
				}	
				if(encontrarHaber&&verbosI.get(1).get("lemma").equals("ser")&&verbosI.get(2).get("mood").equals("participle")){
					pasivas.add(i);
				}else if(encontrarSer&&verbosI.get(1).get("mood").equals("participle")){//Si hemos encontrado el ser tenemos que ver si el siguiente verbo es un participio, el resto que quede no nos importa
					pasivas.add(i);
				}

			}
		}
		return pasivas;
	}
	public List<Integer> reflejaSujeto(String texto) throws IOException, ParseException{//El parse exception habria que hacerlo con try catch?
		//Para ver si una oraci�n es pasiva refleja tiene que ser de la forma se + verbo en 3�persona
		//(con haber+ algo puede ser tambi�n y tenemos que que el haber es el que esta en 3�)
		//y que el verbo NO es una persona (como nombre propio, como indefinido S� se puede.
		//ej: Paula no, pero estudiantes s�).
		//String res="";
		List<Integer> reflejas = new ArrayList<Integer>();
		Conexion conexionPoS= new Conexion(texto, "tagger");
		if(!conexionPoS.isActivo()){
			List<Integer> error=new ArrayList<Integer>();
			error.add(-1);
			return error;
		}
		Conexion conexionDependencias= new Conexion(texto,"dependencies");
		if(!conexionDependencias.isActivo()){
			List<Integer> error=new ArrayList<Integer>();
			error.add(-1);
			return error;
		}
		TratarJSON tratarPoS = new TratarJSON(conexionPoS.getContenido());
		int tamanyo=tratarPoS.tratarJSONPoS().size();
		for(int j=0; j<tamanyo;j++){
			JSONArray frase = tratarPoS.tratarJSONPoS().get(j);
			JSONObject verbo=new JSONObject();
			boolean encontradoSe=false;
			for(int i=0; i<frase.size()&&!encontradoSe;i++){
				JSONObject e= (JSONObject) frase.get(i);
				if(e.get("lemma").equals("se")){
					encontradoSe=true;
					verbo=(JSONObject) frase.get(i+1);//La siguiente pos a donde esta el se
				}
			}
			if(verbo.size()!=0&&verbo.get("person").equals("3")){
				TratarJSON tratarDependencias = new TratarJSON(conexionDependencias.getContenido());
				JSONArray hijos= tratarDependencias.tratarJSONDependencias().get(j) ;//Aqui va a hacer falta un for porque estamos pasando el texto completo.
				String comprobar="";
				for(int i=0; i<hijos.size();i++){
					JSONObject palabra=(JSONObject) hijos.get(i);
					if(palabra.get("function").equals("dobj")||palabra.get("function").equals("subj")){
						comprobar=(String) palabra.get("word");
					}
				}
				if(!comprobar.equals("")){
					Conexion conexionEntidad=new Conexion(comprobar, "entities");//En mar�a tiene que ir comprobar
					if(!conexionEntidad.isActivo()){
						List<Integer> error=new ArrayList<Integer>();
						error.add(-1);
						return error;
					}
					TratarJSON tratarEntidad = new TratarJSON(conexionEntidad.getContenido());
					JSONObject entidad=tratarEntidad.tratarJSONEntidades();
					//En entidad tenemos que ver que nec o es null o NO PER
					if(entidad.get("nec")==null||!entidad.get("nec").equals("PER")){
						reflejas.add(j);
					}
				}
			}
		}
		return reflejas;
	}
	public List<Integer> reflejaSinSujeto(String texto) throws IOException, ParseException{//El parse exception habria que hacerlo con try catch?
		//Para ver si una oraci�n es pasiva refleja tiene que ser de la forma se + verbo en 3�persona
		//(con haber+ algo puede ser tambi�n y tenemos que que el haber es el que esta en 3�)
		//y que el verbo NO es una persona (como nombre propio, como indefinido S� se puede.
		//ej: Paula no, pero estudiantes s�).
		//String res="";
		List<Integer> reflejas = new ArrayList<Integer>();
		Conexion conexionPoS= new Conexion(texto, "tagger");
		if(!conexionPoS.isActivo()){
			List<Integer> error=new ArrayList<Integer>();
			error.add(-1);
			return error;
		}
		Conexion conexionDependencias= new Conexion(texto,"dependencies");
		if(!conexionDependencias.isActivo()){
			List<Integer> error=new ArrayList<Integer>();
			error.add(-1);
			return error;
		}
		TratarJSON tratarPoS = new TratarJSON(conexionPoS.getContenido());
		int tamanyo=tratarPoS.tratarJSONPoS().size();
		for(int j=0; j<tamanyo;j++){
			JSONArray frase = tratarPoS.tratarJSONPoS().get(j);
			JSONObject verbo=new JSONObject();
			boolean encontradoSe=false;
			for(int i=0; i<frase.size()&&!encontradoSe;i++){
				JSONObject e= (JSONObject) frase.get(i);
				if(e.get("lemma").equals("se")){
					encontradoSe=true;
					verbo=(JSONObject) frase.get(i+1);//La siguiente pos a donde esta el se
				}
			}
			if(verbo.size()!=0&&verbo.get("person").equals("3")){
				TratarJSON tratarDependencias = new TratarJSON(conexionDependencias.getContenido());
				JSONArray hijos= tratarDependencias.tratarJSONDependencias().get(j) ;//Aqui va a hacer falta un for porque estamos pasando el texto completo.
				String comprobar="";
				for(int i=0; i<hijos.size();i++){
					JSONObject palabra=(JSONObject) hijos.get(i);
					if(palabra.get("function").equals("dobj")||palabra.get("function").equals("subj")){
						comprobar=(String) palabra.get("word");
					}
				}
				if(comprobar.equals("")){
					reflejas.add(j);
				}
			}
		}
		return reflejas;
	}
	public String reglaPasiva(String texto) throws IOException, ParseException{
		List<Integer> resultado=pasiva(texto);
		List<Integer> resultadoReflejaSujeto=reflejaSujeto(texto);
		for(int i=0;i<resultadoReflejaSujeto.size();i++){
			resultado.add(resultadoReflejaSujeto.get(i));
		}
		List<Integer> resultadoReflejaSinSujeto=reflejaSinSujeto(texto);
		for(int i=0;i<resultadoReflejaSinSujeto.size();i++){
			resultado.add(resultadoReflejaSinSujeto.get(i));
		}
		if(resultado.contains(-1)){
			return "Se ha producido un error en el proceso de validación";
		}
		Rule regla=new Rule();
		regla.setId(1);
		regla.setName("Regla - Forma Pasiva");
		regla.setDescription("No se permite el uso de la forma pasiva");
		if(!resultado.isEmpty()){
			regla.setPass(false);
			String reason="El documento tiene la siguientes frases en forma pasiva: ";
			List<String> frases= getFrases();
			List<String> frasesPasivas= new ArrayList<String>();
			for(int i=0; i< resultado.size(); i++){
				frasesPasivas.add(frases.get(resultado.get(i)));
			}
			reason+=frasesPasivas.toString();
			regla.setReason(reason);
		}
		Gson gson = new Gson();
		String jsonInString = gson.toJson(regla);
		return jsonInString;
	}
	public String reglaSinSujeto(String texto) throws IOException, ParseException{
		Conexion conexion= new Conexion(texto,"dependencies");
		if(!conexion.isActivo()){
			return "Se ha producido un error en el proceso de validación";
		}
		TratarJSON tratar = new TratarJSON(conexion.getContenido());
		List<JSONArray> listaHijos= tratar.tratarJSONDependencias();
		JSONArray hijoI = new JSONArray();
		JSONArray listaHijosAux=new JSONArray();//Para guardar los hijos de otros hijos.
		JSONObject elementoJSON=new JSONObject();
		List<Integer> frasesconsujeto=new ArrayList<Integer>();
		boolean sujeto=false;

		for(int i=0; i<listaHijos.size();i++){
			hijoI=listaHijos.get(i);
			sujeto=false;
			for(int j=0; j<hijoI.size()&&!sujeto;j++){
				elementoJSON =(JSONObject) hijoI.get(j);
				if(elementoJSON.get("function").equals("subj")){
					frasesconsujeto.add(i);
					sujeto=true;
				}else if(elementoJSON.get("children")!=null&&!sujeto){
					listaHijosAux=(JSONArray) elementoJSON.get("children");
					for(int k=0; k<listaHijosAux.size()&&!sujeto;k++){
						elementoJSON=(JSONObject) listaHijosAux.get(k);
						if(elementoJSON.get("function").equals("subj")){
							sujeto=true;
							frasesconsujeto.add(i);
						}
					}
				}
			}
		}
		// frases reflejas sí tienen sujeto pero es dobj
		System.out.println("sujeto");
		List<Integer>reflejas=reflejaSujeto(texto);
		for(int i=0; i<reflejas.size();i++){
			frasesconsujeto.add(reflejas.get(i));
		}

		List<Integer> frasesSinSujeto=new ArrayList<Integer>();
		for(int i=0; i<getFrases().size();i++){
			frasesSinSujeto.add(i);
		}
		for(int i=0;i<frasesconsujeto.size();i++){
			for(int j=0; j<frasesSinSujeto.size();j++){
				if(frasesconsujeto.get(i).equals(frasesSinSujeto.get(j))){
					frasesSinSujeto.remove(j);
				}
			}
		}
		Rule regla=new Rule();
		regla.setId(2);
		regla.setName("Regla - Sujeto en la oraci\u00f3n");
		regla.setDescription("Las oraciones deben tener sujeto");
		if(!frasesSinSujeto.isEmpty()){
			regla.setPass(false);
			String reason="El documento tiene la siguientes frases sin sujeto: ";
			List<String> frases= getFrases();
			List<String> resultado= new ArrayList<String>();
			for(int i=0; i< frasesSinSujeto.size(); i++){
				resultado.add(frases.get(frasesSinSujeto.get(i)));
			}
			reason+=resultado.toString();
			regla.setReason(reason);
		}
		Gson gson = new Gson();
		String jsonInString = gson.toJson(regla);
		return jsonInString;

	}
	//	public static void main(String[] args) throws Throwable{
	//	}
}











