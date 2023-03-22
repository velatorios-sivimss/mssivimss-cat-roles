package com.imss.sivimss.catroles.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.catroles.beans.Rol;
import com.imss.sivimss.catroles.exception.BadRequestException;
import com.imss.sivimss.catroles.model.request.RolRequest;
import com.imss.sivimss.catroles.model.request.UsuarioDto;
import com.imss.sivimss.catroles.model.request.UsuarioRequest;
import com.imss.sivimss.catroles.model.response.RolResponse;
import com.imss.sivimss.catroles.service.RolService;
import com.imss.sivimss.catroles.util.AppConstantes;
import com.imss.sivimss.catroles.util.ConvertirGenerico;
import com.imss.sivimss.catroles.util.DatosRequest;
import com.imss.sivimss.catroles.util.ProviderServiceRestTemplate;
import com.imss.sivimss.catroles.util.Response;
import com.imss.sivimss.catroles.util.ValidarResponseUtil;

@Service
public class RolServiceImpl  implements RolService {
	
	private static final String GENERICO_ACTUALIZAR = "/generico/actualizar";

	@Value("${endpoints.dominio-consulta}")
	private String urlDominioConsulta;

	@Autowired
	private ProviderServiceRestTemplate providerRestTemplate;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public Response<?> consultarRoles(DatosRequest request, Authentication authentication) throws IOException {
		Rol rol= new Rol();
		return ValidarResponseUtil.validarConsultaResponse(providerRestTemplate.consumirServicio(rol.obtenerRoles(request).getDatos(), urlDominioConsulta + "/generico/paginado",
				authentication));
	}

	@Override
	public Response<?> catalogoRol(DatosRequest request, Authentication authentication) throws IOException {
		Rol rol= new Rol();
		List<RolResponse> rolResponses;
		Response<?> response =  providerRestTemplate.consumirServicio(rol.catalogoRol().getDatos(),
				urlDominioConsulta.concat("/generico/consulta"), authentication);
		if (response.getCodigo() == 200) {
			rolResponses = Arrays.asList(modelMapper.map(response.getDatos(), RolResponse[].class));
			response.setDatos(ConvertirGenerico.convertInstanceOfObject(rolResponses));
		}
		return response;
	}
	
	@Override
	public Response<?> buscarFiltrosRol(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsuarioRequest usuarioRequest = gson.fromJson(datosJson, UsuarioRequest.class);
		
		Rol rol = new Rol(usuarioRequest);
		return ValidarResponseUtil.validarConsultaResponse(providerRestTemplate.consumirServicio(rol.buscarFiltrosRol(request,rol).getDatos(), urlDominioConsulta.concat("/generico/paginado"),
				authentication));
	}

	@Override
	public Response<?> detalleRol(DatosRequest request, Authentication authentication) throws IOException {
		Rol rol = new Rol();
		return ValidarResponseUtil.validarConsultaResponse(providerRestTemplate.consumirServicio(rol.detalleRol(request).getDatos(), urlDominioConsulta.concat("/generico/consulta"),
				authentication));
	}

	@Override
	public Response<?> agregarRol(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);

		RolRequest rolRequest = gson.fromJson(datosJson, RolRequest.class);
		Rol rol = new Rol(rolRequest);
		rol.setClaveAlta(usuarioDto.getId().toString());
		
		return ValidarResponseUtil.validarAgregaResponse(providerRestTemplate.consumirServicio(rol.insertar().getDatos(), urlDominioConsulta.concat("/generico/crear"),
				authentication));
	}

	@Override
	public Response<?> actualizarRol(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);

		RolRequest rolRequest = gson.fromJson(datosJson, RolRequest.class);
		if (rolRequest.getIdRol() == null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "Informacion incompleta");
		}
		Rol rol = new Rol(rolRequest);
		rol.setClaveModifica(usuarioDto.getId().toString());
		
		return ValidarResponseUtil.validarActualizaResponse(providerRestTemplate.consumirServicio(rol.actualizar().getDatos(), urlDominioConsulta.concat(GENERICO_ACTUALIZAR),
				authentication));
	}
	
	@Override
	public Response<?> cambiarEstatusRol(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);

		RolRequest rolRequest = gson.fromJson(datosJson, RolRequest.class);
		if (rolRequest.getIdRol() == null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "Informacion incompleta");
		}
		Rol rol = new Rol(rolRequest);
		rol.setClaveModifica(usuarioDto.getId().toString());
		
		if (rol.getEstatusRol()  == 1) {
			return ValidarResponseUtil.validarActivaResponse(providerRestTemplate.consumirServicio(rol.cambiarEstatus().getDatos(), urlDominioConsulta.concat(GENERICO_ACTUALIZAR),
					authentication));
		} else {
			return ValidarResponseUtil.validarDesactivaResponse(providerRestTemplate.consumirServicio(rol.cambiarEstatus().getDatos(), urlDominioConsulta.concat(GENERICO_ACTUALIZAR),
					authentication));
		}
	}


}