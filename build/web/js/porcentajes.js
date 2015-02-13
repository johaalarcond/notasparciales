	function buscarElemento(nombre){
		forma = modificarPorcentajesForm;
		return forma[nombre];
	}

function totalCategoria(categoria){
		valorCampo='0';
		campo = '0';
		valorTotal = 0;
		for(i=0;campo!=null;i++){
			campo = buscarElemento("elemento["+categoria+"].activity["+i+"].percentage");
			if(campo!=null){
				valorCampo = campo.value;
				if(valorCampo != '' && !isNaN(valorCampo)){
					valorTotal += parseFloat(valorCampo);
				}
			}
		}
		
		campo = buscarElemento("elemento["+categoria+"].percentage");
		campo.value = valorTotal;
	}