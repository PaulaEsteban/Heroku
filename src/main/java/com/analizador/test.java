package com.analizador;


	//Quitar el True ; 
import static org.junit.Assert.*;
import java.sql.Date;
import java.lang.reflect.Array; 
import java.text.SimpleDateFormat;
import java.util.*;
import com.controller.Main;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;



import org.junit.runners.MethodSorters;  
	
@FixMethodOrder(MethodSorters.NAME_ASCENDING) 
@RunWith(Parameterized.class)
public class test {
	
	

	private int id;
	private String regla;
	private String descripcion; 
	private boolean pasa; 
	private String razon ;
	   @Before
	   public void initialize() throws Throwable {//creo el objeto
		     

		      //pedidocomprobar=crearunaorden();
	   } 
		
	   
	   public test(int id, String regla, String descripcion, boolean pasa, String razon ) throws Throwable {
		    this.id=id;
			this.regla=regla;
			this.descripcion=descripcion; 
			this.pasa=pasa;
			this.razon=razon;
			
			 
	     
	   }

	   @Parameterized.Parameters
	   public static Collection primeNumbers() { 
	      return Arrays.asList(new Object[][] {
	    	  /*
	    	   * 
	    	   * 
	    	   * Falta poner todos los actores para probar todas las peticiones
	    	   */
	    	  {1,"Regla - Forma Pasiva","No se permite el uso de la forma pasiva",true,"Not Apply"},
            
              });
	   }  
	   //--------------------------------------------------------------------------------------------------------------
	      @Test
	    public void Test_1() throws Throwable  {//Verificado    
//	    	 String texto="Se vende casa. Es alta. Mi nombre es Paula. Ayer llovió. Mi abuelo fue llevado al hospital.";
	    	 Main pruebas = new Main ();
	    	 String pingresponse =pruebas.reglaConcreta(null,"1");
//	    	 String [] sol =pingresponse.split(":");
	    	 System.out.println(pingresponse);
//	    	 System.out.println(sol[sol.length-1]);
	    }   
 }