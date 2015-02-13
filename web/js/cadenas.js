//funcion creada por Hernán
function verificarLista(lista, error){
if(lista.value==""){
window.alert("El campo "+error+" no puede ser nulo");
campo.focus();
return false;
}
return true
}

//funcion creada por Hernán
function verificarGrupo(grupo, elementos, error){
var chekeado=false;
for(i=0; i<elementos;i++)
	if(grupo[i].checked==true)
		chekeado=true;

if(!chekeado){
	window.alert("Por favor seleccione un "+error);
	grupo[0].focus();
	return false;
}
return true;
}
//funcion creada por Hernán
function verificarCampo(campo, textoError){
if(campo.value==""){
window.alert("El campo "+textoError+" no puede ser nulo");
campo.focus();
return false;
}
return true;
}

//Empieza el archivo cadenas original
function isInteger(theElement, theElementName)
{
  var s = Math.abs(theElement.value);
  var filter=/(^\d+$)/;
  if (s.length == 0 ) return true;
  if (filter.test(s))  
       return true;
  else  
         alert("Por Favor inserte un entero Válido!" );
    theElement.focus(); 
    theElement.select(); 
    return false;
}

function compararCadenas(campo1, campo2)
{
  if (campo1.value!=campo2.value)
  {
    window.alert("El valor del campo "+campo1.name+" no puede ser diferente del campo "+campo2.name);
    campo2.focus(); 
    campo2.select(); 
    return false;
  }
  return true;
}


function isAlfaNumber(theElement, theElementName)
{
  var s = theElement.value;
  var filter=/^[a-zA-Z_0-9]{1,}$/;
  if (s.length == 0 ) 
    return true;
  if (filter.test(s))  
    return true;
  else  
    alert(" Por favor ingrese caracteres válidos (a-z A-Z 0-9 _) en el campo " +theElementName );
  theElement.focus(); 
  theElement.select(); 
  return false;
}

function isAlfa(theElement, theElementName)
{
  var s = theElement.value;
  var filter=/^[a-zA-Zñ á-úÁ-ÚÑüÜ]{1,}$/;
  if (s.length == 0 ) 
    return true;
  if (filter.test(s))  
    return true;
  else  
    alert(" Por favor ingrese caracteres válidos en el campo " +theElementName );
  theElement.focus(); 
  theElement.select(); 
  return false;
}

function isEmailAddress (theElement, theElementName)
{
  var s = theElement.value;

  //E-mail address format - localpart@domain
  //The localpart should not contain the following characters - <>()[]\,;:@"<blank space character> and if the localpart includes 
  // a period, it should be followed by any character other than those mentioned above. However, special characters are allowed provided 
  // the entire local part is enclosed by double quotes e.g. "()[]"@domain
  var localPartfilter1 = /^[^<>()\[\]\x5C.,;:@" ]+(\.[^<>()\[\]\x5C.,;:@" ]+)*@$/;
  var localPartfilter2 = /^"[^\r\n]+"@$/;

  //The domain should start and end with an alphanumeric character and any period should be enclosed by an alphanumeric character.
  //Characters allowed in the domain are a-z A-Z 0-9 and -(hyphen)
  var domainfilter = /^([a-zA-Z0-9][a-zA-Z0-9-]*[a-zA-Z0-9]|[a-zA-Z0-9])(\.([a-zA-Z0-9][a-zA-Z0-9-]*[a-zA-Z0-9]|[a-zA-Z0-9]))*$/;
  var sepPos = 0;
  var localPart;
  var domain;
  var localPartOk = false;
  var domainOk    = false;
  sepPos = s.lastIndexOf("@");
  localPart = s.substring(0,sepPos+1);
  domain    = s.substring(sepPos+1,s.length);

  if  (localPartfilter1.test(localPart))
      localPartOk = true;
  else if (localPartfilter2.test(localPart))
      localPartOk = true;
  else
      localPartOk = false;

  if (domainfilter.test(domain))
      domainOk = true;
  else
      domainOk = false;

  if ( (localPartOk==true && domainOk==true)||(s=="") )
      return true;
  else
      alert(" Por favor ingrese una dirección válida!" );
  theElement.focus(); 
  theElement.select(); 
  return false;
}

function isNotNumber(theElement,theElementname)

{
  s = theElement.value;
    if (isNaN(Math.abs(theElement.value)) && (s.charAt(0) != '#'))
    
  {
      if ( typeof theElementname == "undefined" ) 
         return true;
      else  
      
      { 
         for (var i=0; (i <= s.length && s.charAt(i) != '.'); )
         {
          if (((s.charAt(i) >= 0) &&(s.charAt(i) <= 9)) || (s.charAt(i) == ',' && i != 0 && i != s.length-1) || (s.charAt(i) == '.') )
                 i++; 
          else
             return true; 

         } 
        if (s.charAt(i) == '.') 
         { 
	     for (i++;i <= s.length; ) 
           { 
            if (((s.charAt(i) >= 0) && (s.charAt(i) <= 9))) 
              i++; 
            else 
              return true;    
           } 
         } 
       } 
  } 
  alert( theElementname +  " NO debe contener sólo números" ); 
  theElement.focus(); 
  theElement.select();   
  return false;
}


function isNumber(theElement,theElementname)

{
  s = theElement.value;
    if (isNaN(Math.abs(theElement.value)) && (s.charAt(0) != '#'))
    
  {
      if ( typeof theElementname == "undefined" ) 
      {
         alert( "Debe contener sólo números" );
         theElement.focus(); 
         theElement.select(); 
         return false;
      } 
      else  
      
      { 
         for (var i=0; (i <= s.length && s.charAt(i) != '.'); )
         {
          if (((s.charAt(i) >= 0) &&(s.charAt(i) <= 9)) || (s.charAt(i) == ',' && i != 0 && i != s.length-1) || (s.charAt(i) == '.') )
                 i++; 
          else 
           { 
             alert( theElementname +  " debe contener sólo números" ); 
             theElement.focus(); 
             theElement.select(); 
             return false; 
           } 
         } 
        if (s.charAt(i) == '.') 
         { 
	     for (i++;i <= s.length; ) 
           { 
            if (((s.charAt(i) >= 0) && (s.charAt(i) <= 9))) 
              i++; 
            else 
            { 
             alert( theElementname +  " debe contener sólo números" ); 
             theElement.focus(); 
             theElement.select(); 
             return false;
            } 
           } 
         } 
       } 
  } 
  return true;
}


function isNullAlfa( field , fieldName) {
  selected = 0;
  fieldIsNull = 0;
  if ( field.type == "text" ||
       field.type == "password" ||
       field.type == "textarea" ) {
    if ( field.value == "" )
      fieldIsNull = 1;
  } else if ( field.type == "select-one" ) {
      if ( field.options[field.selectedIndex].value == "%null%")
        fieldIsNull = 1;
  } else if ( field.type == "select-multiple" ) {
      fieldIsNull = 1;
      for ( i = 0; i < field.length; i++ )
        if ( field.options[i].selected )
          fieldIsNull = 0;
  } else if ( field.type == "undefined" ||
              field.type == "checkbox"  ||
              field.type == "radio" ) {
      fieldIsNull = 1;
      for ( i = 0; i < field.length; i++ )
        if ( field[i].checked )
          fieldIsNull = 0;
  }
  if ( fieldIsNull ) {
      if ( typeof fieldName == "undefined" ) 
         alert( " El valor del campo no puede ser Nulo (null)." );
      else  
         alert( "El Valor del campo " + fieldName + " No puede ser Nulo (null)." );
      if ( field.type == "text" ||
           field.type == "textarea"  ||
           field.type == "password"  ||
           field.type == "select-one"  ||
           field.type == "select-multiple" )
        field.focus();
        field.select();
     return false;
  }

  // aqui empieza la validación de caracteres válidos
  var s = field.value;
  var filter=/^[a-zA-Zñ-ñÑ-Ñá-áÁ-Áé-éÉ-Éí-íÍ-Íó-óÓ-Óú-úÚ-Ú - ]{1,}$/;
  if (s.length == 0 ) return true;
  if (filter.test(s))  
       return true;
  else  
         alert(" Por favor ingrese caracteres válidos" );
    field.focus(); 
    field.select(); 
    return false;

}



//funcion generica para validar cadenas
//Parametros usados:	campo: objeto campo que se va a validar
//		    	name: Nombre que se quiere mostrar (si se deja en blanco se muestra el nombre que tiene el campo)	
//			logmin:Numero de la longitud minima que debe tener el campo
//                      nulo: se permite (1) o no (0) que el campo este vacío
//			tipodato: que validacion se va a aplicar
//				0 admite cualquier tipo
//				1 debe ser solo números
//				2 debe ser solo caracter
//				4 debe ser correo valido
//				8 debe ser usuario/contrasena valido


function validarCadena (campo,name,longmin,nulo,tipodato)
{
 if (name.length==0)
   vmostrar= campo.name;
 else
   vmostrar= name;

  if (campo.value.length==0 &&nulo!=1)
  {
    window.alert("El valor del campo "+vmostrar+" no puede ser nulo");
    campo.focus(); 
    campo.select(); 
    return false;

  }
  if (campo.value.length<longmin && (nulo==0 || campo.value.length!=0))
  {
    window.alert("La longitud del campo "+campo.name+" no es válida");
    campo.focus(); 
    campo.select(); 
    return false;
  }
 if (tipodato==1)
  return isNumber(campo,vmostrar);
 else 
  if (tipodato==2)
   return isAlfa(campo,vmostrar);
  else
   if (tipodato==4)
    return isEmailAddress(campo,vmostrar);
   else
    if (tipodato==8)
     {
     if(isAlfaNumber(campo,vmostrar))
       return isNotNumber(campo,vmostrar)
     return false;  
     }
 return true;
 
}