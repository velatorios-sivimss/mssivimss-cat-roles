package com.imss.sivimss.catroles.util;

public class ValidarResponseUtil {
	
	private ValidarResponseUtil() {
		super();
	}
	
	public  static Response<?>validarAgregaResponse(Response<?> respuestaGenerado) {
		Integer codigo = respuestaGenerado.getCodigo();
		if (codigo == 200) {
			respuestaGenerado.setMensaje("030");
		} else if (codigo == 500  || codigo == 404 || codigo == 400 || codigo == 403) {
			validarError(respuestaGenerado);
		}
		return respuestaGenerado;
	}
	
	
	public  static Response<?>validarActualizaResponse(Response<?> respuestaGenerado) {
		Integer codigo = respuestaGenerado.getCodigo();
		if (codigo == 200) {
			respuestaGenerado.setMensaje("018");
		} else if (codigo == 500  || codigo == 404 || codigo == 400 || codigo == 403) {
			validarError(respuestaGenerado);
		}
		return respuestaGenerado;
	}
	
	public  static Response<?>validarActivaResponse(Response<?> respuestaGenerado) {
		Integer codigo = respuestaGenerado.getCodigo();
		if (codigo == 200) {
			respuestaGenerado.setMensaje("069");
		} else if (codigo == 500  || codigo == 404 || codigo == 400 || codigo == 403) {
			validarError(respuestaGenerado);
		}
		return respuestaGenerado;
	}
	
	public  static Response<?>validarDesactivaResponse(Response<?> respuestaGenerado) {
		Integer codigo = respuestaGenerado.getCodigo();
		if (codigo == 200) {
			respuestaGenerado.setMensaje("019");
		} else if (codigo == 500  || codigo == 404 || codigo == 400 || codigo == 403) {
			validarError(respuestaGenerado);
		}
		return respuestaGenerado;
	}
	
	public  static  Response<?>validarConsultaResponse(Response<?> respuestaGenerado) {
		Integer codigo = respuestaGenerado.getCodigo();
		if (codigo == 200 &&  (!respuestaGenerado.getDatos().toString().contains("id"))) {
				respuestaGenerado.setMensaje("045");
			
		}
		return respuestaGenerado;
	}


	private static void validarError(Response <?>respuestaGenerado) {
		respuestaGenerado.setMensaje("005");
	}

}
