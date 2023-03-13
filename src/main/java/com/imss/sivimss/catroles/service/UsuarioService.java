package com.imss.sivimss.catroles.service;

import java.io.IOException;

import org.springframework.security.core.Authentication;

import com.imss.sivimss.catroles.util.DatosRequest;
import com.imss.sivimss.catroles.util.Response;

public interface UsuarioService {

	Response<?> consultarUsuarios(DatosRequest request, Authentication authentication) throws IOException;

	Response<?> buscarUsuario(DatosRequest request, Authentication authentication) throws IOException;

	Response<?> detalleUsuario(DatosRequest request, Authentication authentication) throws IOException;

	Response<?> catalogoUsuario(DatosRequest request, Authentication authentication) throws IOException;

	Response<?> agregarUsuario(DatosRequest request, Authentication authentication) throws IOException;

	Response<?> actualizarUsuario(DatosRequest request, Authentication authentication) throws IOException;

	Response<?> cambiarEstatusUsuario(DatosRequest request, Authentication authentication) throws IOException;
}
