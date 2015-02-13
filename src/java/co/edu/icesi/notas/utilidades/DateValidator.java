package co.edu.icesi.notas.utilidades;

import java.text.DateFormat;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Esta clase permite la validación del formato(yyyy-mm-dd) de fechas.
 * Además posee otras funcionalidades.
 * */
public abstract class DateValidator {
	
	/**
	 * Valida si la fecha por parámetro es inferior a la fecha del sistema.
	 * @param fecha formato aaaa-mm-dd
	 * @return true si la fecha es menor a la actual, false si es mayor
	 */
 public static boolean anteriorActual(String fecha){
 	Date actual=new Date();
 	DateFormat formato=DateFormat.getDateInstance(DateFormat.SHORT);
 	String actualFormato=formato.format(actual);
 	StringTokenizer tokenActual=new StringTokenizer(actualFormato, "/");
 	StringTokenizer tokenFecha=new StringTokenizer(fecha, "-");
 	if(tokenActual.countTokens()==3&&tokenFecha.countTokens()==3){
 	  String diaActual=tokenActual.nextToken();
 	  String mesActual=tokenActual.nextToken();
 	  String añoActual=tokenActual.nextToken();
 	  String añoFecha=tokenFecha.nextToken();
 	  //añoFecha=añoFecha.substring(2,4);
 	  String mesFecha=tokenFecha.nextToken();
 	  String diaFecha=tokenFecha.nextToken();
 	  int actualCifra=Integer.parseInt(añoActual);
 	  if(actualCifra<100)
 	  	actualCifra+=2000;
 	  int fechaCifra=Integer.parseInt(añoFecha);
 	  if(actualCifra>fechaCifra)
 	  	return true;
 	  if(actualCifra<fechaCifra)
 	  	return false;
 	  //hasta aqui los años son iguales, sigue validar los meses  
 	  actualCifra=Integer.parseInt(mesActual);
 	  fechaCifra=Integer.parseInt(mesFecha);
 	  if(actualCifra>fechaCifra)
 	  	return true;
 	 if(actualCifra<fechaCifra)
 	  	return false;
 	 //el mes y el año son iguales
 	  actualCifra=Integer.parseInt(diaActual);
	  fechaCifra=Integer.parseInt(diaFecha);
	  if(actualCifra>fechaCifra)
 	  	return true;
 	  if(actualCifra<fechaCifra)
 	  	return false;
 	}
 	return false;
 	}
 
 public static boolean fechaIgualActual(String fecha){
 	Date actual=new Date();
 	DateFormat formato=DateFormat.getDateInstance(DateFormat.SHORT);
 	String actualFormato=formato.format(actual);
 	StringTokenizer tokenActual=new StringTokenizer(actualFormato, "/");
 	StringTokenizer tokenFecha=new StringTokenizer(fecha, "-");
 	if(tokenActual.countTokens()==3&&tokenFecha.countTokens()==3){
 	  String diaActual=tokenActual.nextToken();
 	  String mesActual=tokenActual.nextToken();
 	  String añoActual=tokenActual.nextToken();
 	  String añoFecha=tokenFecha.nextToken();
 	  añoFecha=añoFecha.substring(2,4);
 	  String mesFecha=tokenFecha.nextToken();
 	  String diaFecha=tokenFecha.nextToken();
 	  int actualCifra=Integer.parseInt(añoActual);
 	  int fechaCifra=Integer.parseInt(añoFecha);
 	  if(actualCifra!=fechaCifra)
 	  	return false;
 	  //hasta aqui los años son iguales, sigue validar los meses  
 	  actualCifra=Integer.parseInt(mesActual);
 	  fechaCifra=Integer.parseInt(mesFecha);
 	  if(actualCifra!=fechaCifra)
 	  	return false;
 	 //el mes y el año son iguales
 	  actualCifra=Integer.parseInt(diaActual);
	  fechaCifra=Integer.parseInt(diaFecha);
	  if(actualCifra==fechaCifra)
 	  	return true;
 	}
 	return false;
 	}

    
    public static boolean validarFecha(String fecha){
    	boolean resp=true;
    	try{
    	StringTokenizer token=new StringTokenizer(fecha, "-");
    	String año=token.nextToken();
    	String mes=token.nextToken();
    	String dia=token.nextToken();
    	int m=Integer.parseInt(mes);
    	if(m>12||m<1)
    		return false;
    	int d=Integer.parseInt(dia);
    	if((m==1||m==3||m==5||m==7||m==8||m==10||m==12)&&(d>31))
    		return false;
    	
    	int a = Integer.parseInt(año);
    	if (m == 2) {
            boolean leap = (a % 4 == 0 && (a % 100 != 0 || a % 400 == 0));
            if (d>29 || (d == 29 && !leap)) {
                return false;
            }
        }
    	if((m==4||m==6||m==9||m==11)&&(d>30))
    		return false;
    	
    	}catch(Exception e){
    		resp=false;
    	}
    	return resp;
    }
}
