package com.imss.sivimss.catroles.util;

/**
 * Clase para la paginacion
 *
 * @author    
 * @puesto dev
 * @date 24 nov. 2022
 */
public class AppConstantes {
	
	public static final String NUMERO_DE_PAGINA = "0";
	public static final String TAMANIO_PAGINA = "10";
	public static final String ORDER_BY= "id";
	public static final String ORDER_DIRECTION= "asc";
	public static final String SUPERVISOR = "Supervisor";
	
	public static final String DATOS= "datos";
	public static final String QUERY= "query";
	public static final String STATUSEXCEPTION = "status";
	public static final String EXPIREDJWTEXCEPTION = "expired";
	public static final String MALFORMEDJWTEXCEPTION = "malformed";
	public static final String UNSUPPORTEDJWTEXCEPTION = "unsupported";
	public static final String ILLEGALARGUMENTEXCEPTION  = "illegalArgument";
	public static final String SIGNATUREEXCEPTION  = "signature";
	public static final String FORBIDDENEXCEPTION  = "forbidden";
	
	public static final String EXPIREDJWTEXCEPTION_MENSAJE = "Token expirado.";
	public static final String MALFORMEDJWTEXCEPTION_MENSAJE = "Token mal formado.";
	public static final String UNSUPPORTEDJWTEXCEPTION_MENSAJE = "Token no soportado.";
	public static final String ILLEGALARGUMENTEXCEPTION_MENSAJE  = "Token vacío.";
	public static final String SIGNATUREEXCEPTION_MENSAJE  = "Fallo la firma.";
	public static final String FORBIDDENEXCEPTION_MENSAJE  = "No tiene autorización para realizar la solicitud.";
	
	public static final String ID_USUARIO_MODIFICA = "ID_USUARIO_MODIFICA";
	public static final String FEC_ACTUALIZACION = "FEC_ACTUALIZACION";
	public static final String ID_USUARIO_BAJA = "ID_USUARIO_BAJA";
	public static final String UPDATE_SVC_ROL = "UPDATE SVC_ROL";
	public static final String CVE_ESTATUS = "CVE_ESTATUS";
	public static final String FEC_BAJA = "FEC_BAJA";
	public static final String DES_ROL = "DES_ROL";
	public static final String ID_ROL = "ID_ROL = ";
	public static final String NOW = "NOW()";
	public static final String NULL = "NULL";
	

	private AppConstantes() {
	    throw new IllegalStateException("AppConstantes class");
	  }

}
