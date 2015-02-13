package co.edu.icesi.notas.utilidades;

import java.math.BigDecimal;

/**
 * Realiza algunas operaciones matemáticas de acuerdo a los requerimientos
 * de la aplicación.
 * @author mzapata
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class OperacionesMatematicas {
	
	/**
	 * Suma dos números decimales, retornando un valor exacto.
	 * */
	public static double sumar(double sumando1, double sumando2){
		BigDecimal bigSumando1 = new BigDecimal((new Double(sumando1)).toString());
		BigDecimal bigSumando2 = new BigDecimal((new Double(sumando2)).toString());
		BigDecimal bigResultado = bigSumando1.add(bigSumando2);
		return bigResultado.doubleValue();
	}
	
	/**
	 * Resta dos números decimales, retornando un valor exacto.
	 * */
	public static double restar(double minuendo, double sustraendo){
		BigDecimal bigMinuendo = new BigDecimal((new Double(minuendo)).toString());
		BigDecimal bigSustraendo = new BigDecimal((new Double(sustraendo)).toString());
		BigDecimal bigResultado = bigMinuendo.subtract(bigSustraendo);
		return bigResultado.doubleValue();
	}
	
	/**
	 * Este método se encarga de dividir dos números decimales,
	 * retornando un valor EXACTO.
	 * */
	public static double dividir(double dividendo, double divisor){
		if(divisor==0){
			return 0;
		}
		try{
			BigDecimal bigDividendo = new BigDecimal((new Double(dividendo)).toString());
			BigDecimal bigDivisor = new BigDecimal((new Double(divisor)).toString());
			BigDecimal bigResultado = bigDividendo.divide(bigDivisor,20,BigDecimal.ROUND_HALF_UP);
			return bigResultado.doubleValue();
		}catch(ArithmeticException are){
			System.out.println("Problemas haciendo division");
			System.out.println("dividendo: "+dividendo+", divisor: "+divisor+" division: "+dividendo/divisor);
			return dividendo/divisor;
		}
	}
	
	public static double multiplicar(double multiplicando1, double multiplicando2){
		BigDecimal bigMult1 = new BigDecimal((new Double(multiplicando1)).toString());
		BigDecimal bigMult2 = new BigDecimal((new Double(multiplicando2)).toString());
		BigDecimal bigResultado = bigMult1.multiply(bigMult2);
		return bigResultado.doubleValue();
	}
	
	public static double multiplicar(double multiplicando1, double multiplicando2,double multiplicando3){
		double producto1 = OperacionesMatematicas.multiplicar(multiplicando1,multiplicando2);
		double resultado = OperacionesMatematicas.multiplicar(producto1,multiplicando3);
		return resultado;
	}
	
	/*Redondea número a la cantidad de decimales establecida por numDecimales.*/
	public static double redondear(double numero, int numDecimales){
           
		String strNumero = String.valueOf(numero);
		String decimales = strNumero.substring(strNumero.indexOf(".")+1);
		int numDecimalesActual=decimales.length();
		//En caso de que posea menor o igual cantidad de decimales, ya está redondeado. 
		if(numDecimalesActual>numDecimales){
			int ultimoDigito;
			while(numDecimalesActual>numDecimales){
				//System.out.println("numero(cad): "+strNumero+", numero(number): "+numero);
                            
                            
				ultimoDigito=Integer.parseInt(strNumero.substring(strNumero.length()-1));
                          
				//Reemplazamos el anterior String con el mismo pero eliminando el último caracter.
				strNumero=strNumero.substring(0,strNumero.length()-1);
				if(ultimoDigito>=5){
					strNumero=OperacionesMatematicas.sumarUno(strNumero);
					numDecimalesActual=(strNumero.substring(strNumero.indexOf(".")+1)).length();
				}else{
					numDecimalesActual--;
				}
			}
			return Double.parseDouble(strNumero);
		}
		return numero;
	}
	
	private static String sumarUno(String numero){
		String decimales = numero.substring(numero.indexOf(".")+1);
		int numDecimalesActual=decimales.length();
		
		StringBuffer numeroASumar=new StringBuffer("0.");
		for(int i=0;i<numDecimalesActual-1;i++){
			numeroASumar.append("0");
		}
		numeroASumar.append("1");
		
		BigDecimal numSumar= new BigDecimal(numeroASumar.toString());
		
		BigDecimal numeroEntero=new BigDecimal(numero);
		
		BigDecimal suma=numSumar.add(numeroEntero);
		
		return suma.toString();
	}

}
