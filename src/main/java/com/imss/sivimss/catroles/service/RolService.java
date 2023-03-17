package com.imss.sivimss.catroles.service;

import java.io.IOException;

import org.springframework.security.core.Authentication;

import com.imss.sivimss.catroles.util.DatosRequest;
import  com.imss.sivimss.catroles.util.Response;

public interface RolService {

	Response<?> consultarRoles(DatosRequest request, Authentication authentication) throws IOException;

	Response<?> catalogoRol(DatosRequest request, Authentication authentication)  throws IOException;

	Response<?> buscarFiltrosRol(DatosRequest request, Authentication authentication) throws IOException;

	Response<?> detalleRol(DatosRequest request, Authentication authentication) throws IOException;

	Response<?> agregarRol(DatosRequest request, Authentication authentication)throws IOException;

	Response<?> actualizarRol(DatosRequest request, Authentication authentication)throws IOException;
	
	Response<?> activarRol(DatosRequest request, Authentication authentication)throws IOException;

	Response<?> borrarRol(DatosRequest request, Authentication authentication)throws IOException;
	
}