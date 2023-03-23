package com.imss.sivimss.catroles.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MensajeResponseUtil {
	
	private static final Logger log = LoggerFactory.getLogger(MensajeResponseUtil.class);
	
	private MensajeResponseUtil() {
		super();
	}

	
	public  static Response<?>mensajeResponse(Response<?> respuestaGenerado, String numeroMensaje) {
		Integer codigo = respuestaGenerado.getCodigo();
		if (codigo == 200) {
			respuestaGenerado.setMensaje(numeroMensaje);
		} else if (codigo == 500  || codigo == 404 || codigo == 400 || codigo == 403) {
			log.error("Error.. {}",respuestaGenerado.getMensaje());
			respuestaGenerado.setMensaje("005");
		} 
		return respuestaGenerado;
	}
	
	public  static Response<?>mensajeConsultaResponse(Response<?> respuestaGenerado, String numeroMensaje) {
		Integer codigo = respuestaGenerado.getCodigo();
		if (codigo == 200 &&  (!respuestaGenerado.getDatos().toString().contains("id"))){
			respuestaGenerado.setMensaje(numeroMensaje);
		}
		return respuestaGenerado;
	}
	
}
