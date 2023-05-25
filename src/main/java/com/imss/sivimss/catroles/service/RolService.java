package com.imss.sivimss.catroles.service;

import java.io.IOException;

import org.springframework.security.core.Authentication;

import com.imss.sivimss.catroles.util.DatosRequest;
import  com.imss.sivimss.catroles.util.Response;

public interface RolService {

	Response<Object> consultarRoles(DatosRequest request, Authentication authentication) throws IOException;

	Response<Object> catalogoRol(DatosRequest request, Authentication authentication)  throws IOException;

	Response<Object> buscarFiltrosRol(DatosRequest request, Authentication authentication) throws IOException;

	Response<Object> detalleRol(DatosRequest request, Authentication authentication) throws IOException;

	Response<Object> agregarRol(DatosRequest request, Authentication authentication)throws IOException;

	Response<Object> actualizarRol(DatosRequest request, Authentication authentication)throws IOException;

	Response<Object> cambiarEstatusRol(DatosRequest request, Authentication authentication)throws IOException;
	
	Response<Object> generarDocumento(DatosRequest request, Authentication authentication)throws IOException;
	
}