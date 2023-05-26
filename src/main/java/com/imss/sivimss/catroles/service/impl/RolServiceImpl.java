package com.imss.sivimss.catroles.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.xml.bind.DatatypeConverter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.catroles.beans.Rol;
import com.imss.sivimss.catroles.exception.BadRequestException;
import com.imss.sivimss.catroles.model.request.ReporteDto;
import com.imss.sivimss.catroles.model.request.RolRequest;
import com.imss.sivimss.catroles.model.request.UsuarioDto;
import com.imss.sivimss.catroles.model.request.UsuarioRequest;
import com.imss.sivimss.catroles.model.response.RolResponse;
import com.imss.sivimss.catroles.service.RolService;
import com.imss.sivimss.catroles.util.AppConstantes;
import com.imss.sivimss.catroles.util.ConvertirGenerico;
import com.imss.sivimss.catroles.util.DatosRequest;
import com.imss.sivimss.catroles.util.LogUtil;
import com.imss.sivimss.catroles.util.ProviderServiceRestTemplate;
import com.imss.sivimss.catroles.util.Response;

import lombok.extern.slf4j.Slf4j;

import com.imss.sivimss.catroles.util.MensajeResponseUtil;

@Service
@Slf4j
public class RolServiceImpl implements RolService {

	private static final String AGREGADO_CORRECTAMENTE = "30"; // Agregado correctamente.
	private static final String MODIFICADO_CORRECTAMENTE = "18"; // Modificado correctamente.
	private static final String DESACTIVADO_CORRECTAMENTE = "19"; // Desactivado correctamente.
	private static final String SIN_INFORMACION = "45"; // No se encontró información relacionada a tu búsqueda.
	private static final String ACTIVADO_CORRECTAMENTE = "69"; // Activado correctamente.
	private static final String ERROR_AL_DESCARGAR_DOCUMENTO = "64"; // Error en la descarga del documento.Intenta  nuevamente.
	private static final String ERROR_EJECUTAR_QUERY = "Error al ejecutar el query: ";
	private static final String CONSULTAR = "consulta";
	private static final String ALTA = "alta";
	private static final String GENERACION = "Generacion de Documento";
	private static final String MODIFICACION = "modificacion";
	private static final String CU04_NAME= "Gestionar Roles: ";

	Response<Object> response;

	@Value("${endpoints.mod-catalogos}")
	private String urlModCatalogos;
	
	private static final String CONSULTA = "/consulta";
	private static final String CONSULTA_PAGINADO = "/paginado";
	private static final String CREAR = "/crear";
	private static final String ACTUALIZAR = "/actualizar";


	@Value("${formato_fecha}")
	private String formatoFecha;

	@Value("${plantilla.pdf-reporteGestionRol}")
	private String nombrePdfReportes;

	@Value("${url_reportes.ms-reportes}")
	private String urlReportes;

	@Autowired
	private ProviderServiceRestTemplate providerRestTemplate;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private LogUtil logUtil;


	@Override
	public Response<Object> consultarRoles(DatosRequest request, Authentication authentication) throws IOException {
		Rol rol = new Rol();
		
		Map<String, Object> envioDatos = rol.obtenerRoles(request, formatoFecha).getDatos();
		try {
			log.error(CU04_NAME + queryDecoded(envioDatos));
			 logUtil.crearArchivoLog(Level.INFO.toString(), CU04_NAME + this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), "consultarRoles", CONSULTAR, authentication);
			response = providerRestTemplate.consumirServicio(envioDatos, urlModCatalogos + CONSULTA_PAGINADO, authentication);
			return MensajeResponseUtil.mensajeConsultaResponse(response, SIN_INFORMACION);
		} catch (Exception e) {
			log.error(ERROR_EJECUTAR_QUERY+ queryDecoded(envioDatos));
			logUtil.crearArchivoLog(Level.WARNING.toString(), CU04_NAME +  this.getClass().getSimpleName(), this.getClass().getPackage().toString(), ERROR_EJECUTAR_QUERY + queryDecoded(envioDatos), CONSULTAR,
					authentication);
			throw new IOException("52", e.getCause());
		}
	}

	@Override
	public Response<Object> catalogoRol(DatosRequest request, Authentication authentication) throws IOException {
		Rol rol = new Rol();
		Map<String, Object> envioDatos = rol.catalogoRol().getDatos();
		try {
			List<RolResponse> rolResponses;
			 logUtil.crearArchivoLog(Level.INFO.toString(), CU04_NAME +  this.getClass().getSimpleName(),
					this.getClass().getPackage().toString(), "catalogoRol", CONSULTAR, authentication);
				log.error(CU04_NAME + queryDecoded(envioDatos));
			response = providerRestTemplate.consumirServicio(envioDatos, urlModCatalogos + CONSULTA, authentication);
			if (response.getCodigo() == 200) {
				rolResponses = Arrays.asList(modelMapper.map(response.getDatos(), RolResponse[].class));
				response.setDatos(ConvertirGenerico.convertInstanceOfObject(rolResponses));
			}
			return MensajeResponseUtil.mensajeConsultaResponse(response, SIN_INFORMACION);
		} catch (Exception e) {
			log.error( CU04_NAME + ERROR_EJECUTAR_QUERY + queryDecoded(envioDatos));
			logUtil.crearArchivoLog(Level.WARNING.toString(),  CU04_NAME + this.getClass().getSimpleName(),
				this.getClass().getPackage().toString(), ERROR_EJECUTAR_QUERY + queryDecoded(envioDatos), CONSULTAR,
				authentication);
			throw new IOException("52", e.getCause());
		}
	}

	@Override
	public Response<Object> buscarFiltrosRol(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsuarioRequest usuarioRequest = gson.fromJson(datosJson, UsuarioRequest.class);

		Rol rol = new Rol(usuarioRequest);
		Map<String, Object> envioDatos = rol.buscarFiltrosRol(request, formatoFecha).getDatos();

		try {
			log.error(CU04_NAME + queryDecoded(envioDatos));
			logUtil.crearArchivoLog(Level.INFO.toString(), CU04_NAME +  this.getClass().getSimpleName(),
				this.getClass().getPackage().toString(), "buscarFiltrosRol", CONSULTAR, authentication);
			response = providerRestTemplate.consumirServicio(envioDatos, urlModCatalogos + CONSULTA_PAGINADO, authentication);
			return MensajeResponseUtil.mensajeConsultaResponse(response, SIN_INFORMACION);
		} catch (Exception e) {
			log.error( CU04_NAME + ERROR_EJECUTAR_QUERY + queryDecoded(envioDatos));
			logUtil.crearArchivoLog(Level.WARNING.toString(), CU04_NAME +  this.getClass().getSimpleName(),
				this.getClass().getPackage().toString(), ERROR_EJECUTAR_QUERY + queryDecoded(envioDatos), CONSULTAR,
				authentication);
			throw new IOException("52", e.getCause());
		}
	}

	@Override
	public Response<Object> detalleRol(DatosRequest request, Authentication authentication) throws IOException {

		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsuarioRequest usuarioRequest = gson.fromJson(datosJson, UsuarioRequest.class);

		Rol rol = new Rol(usuarioRequest);
		Map<String, Object> envioDatos = rol.detalleRol(request, rol, formatoFecha).getDatos();

		try {
			log.error(CU04_NAME + queryDecoded(envioDatos));
			logUtil.crearArchivoLog(Level.INFO.toString(), CU04_NAME +  this.getClass().getSimpleName(),	this.getClass().getPackage().toString(), "detalleRol", CONSULTA, authentication);
			response = providerRestTemplate.consumirServicio(envioDatos,
					urlModCatalogos + CONSULTA, authentication);

			return MensajeResponseUtil.mensajeConsultaResponse(response, SIN_INFORMACION);
		} catch (Exception e) {
			log.error( CU04_NAME + ERROR_EJECUTAR_QUERY + queryDecoded(envioDatos));
			logUtil.crearArchivoLog(Level.WARNING.toString(), CU04_NAME +  this.getClass().getSimpleName(),
				this.getClass().getPackage().toString(), ERROR_EJECUTAR_QUERY + queryDecoded(envioDatos), CONSULTAR,
				authentication);
			throw new IOException("52", e.getCause());
		}
	}

	@Override
	public Response<Object> agregarRol(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));

		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);

		RolRequest rolRequest = new RolRequest();

		rolRequest = gson.fromJson(datosJson, RolRequest.class);
		Rol rol = new Rol(rolRequest);
		rol.setIdUsuario(usuarioDto.getIdUsuario());
		Map<String, Object> envioDatos = rol.insertar().getDatos();
		try {
			log.error(CU04_NAME + queryDecoded(envioDatos));
			logUtil.crearArchivoLog(Level.INFO.toString(), CU04_NAME +  this.getClass().getSimpleName(),	this.getClass().getPackage().toString(), "agregarRol", ALTA, authentication);
			response = providerRestTemplate.consumirServicio(envioDatos, urlModCatalogos + CREAR, authentication);
			return MensajeResponseUtil.mensajeResponse(response, AGREGADO_CORRECTAMENTE);
		} catch (Exception e) {
			log.error( CU04_NAME + ERROR_EJECUTAR_QUERY + queryDecoded(envioDatos));
			logUtil.crearArchivoLog(Level.WARNING.toString(), CU04_NAME +  this.getClass().getSimpleName(), this.getClass().getPackage().toString(), ERROR_EJECUTAR_QUERY +  queryDecoded(envioDatos), ALTA,
				authentication);
			throw new IOException("52", e.getCause());
		}
	}

	@Override
	public Response<Object> actualizarRol(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);

		RolRequest rolRequest = gson.fromJson(datosJson, RolRequest.class);
		if (rolRequest.getIdRol() == null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "Informacion incompleta");
		}
		Rol rol = new Rol(rolRequest);
		rol.setClaveModifica(usuarioDto.getIdUsuario().toString());
		Map<String, Object> envioDatos = rol.actualizar().getDatos();
		try {
			log.error(CU04_NAME + queryDecoded(envioDatos));
			logUtil.crearArchivoLog(Level.INFO.toString(), CU04_NAME +  this.getClass().getSimpleName(),	this.getClass().getPackage().toString(), "actualizarRol", MODIFICACION, authentication);
			response = providerRestTemplate.consumirServicio(envioDatos, urlModCatalogos + ACTUALIZAR, authentication);
			return MensajeResponseUtil.mensajeResponse(response, MODIFICADO_CORRECTAMENTE);
		} catch (Exception e) {
			log.error( CU04_NAME + ERROR_EJECUTAR_QUERY +  queryDecoded(envioDatos));
			logUtil.crearArchivoLog(Level.WARNING.toString(), CU04_NAME +  this.getClass().getSimpleName(), this.getClass().getPackage().toString(), ERROR_EJECUTAR_QUERY +  queryDecoded(envioDatos), MODIFICACION,
					authentication);
			throw new IOException("52", e.getCause());
		}
	}

	@Override
	public Response<Object> cambiarEstatusRol(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);

		RolRequest rolRequest = gson.fromJson(datosJson, RolRequest.class);
		if (rolRequest.getIdRol() == null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "Informacion incompleta");
		}
		Rol rol = new Rol(rolRequest);
		rol.setClaveModifica(usuarioDto.getIdUsuario().toString());
		Map<String, Object> envioDatos = rol.cambiarEstatus().getDatos();

		try {
			log.error(CU04_NAME + queryDecoded(envioDatos));
			logUtil.crearArchivoLog(Level.INFO.toString(), CU04_NAME +  this.getClass().getSimpleName(),	this.getClass().getPackage().toString(), "cambiarEstatusRol", MODIFICACION, authentication);
			response = providerRestTemplate.consumirServicio(rol.cambiarEstatus().getDatos(), urlModCatalogos + ACTUALIZAR, authentication);
			if (rol.getEstatusRol() == 1) {
				return MensajeResponseUtil.mensajeResponse(response, ACTIVADO_CORRECTAMENTE);
			} else {
				return MensajeResponseUtil.mensajeResponse(response, DESACTIVADO_CORRECTAMENTE);
			}
		} catch (Exception e) {
			log.error( CU04_NAME + ERROR_EJECUTAR_QUERY + queryDecoded(envioDatos));
			logUtil.crearArchivoLog(Level.WARNING.toString(), CU04_NAME +  this.getClass().getSimpleName(), this.getClass().getPackage().toString(), ERROR_EJECUTAR_QUERY + queryDecoded(envioDatos), MODIFICACION,
					authentication);
			throw new IOException("52", e.getCause());
		}
	}

	@Override
	public Response<Object> generarDocumento(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		RolRequest rolRequest = gson.fromJson(datosJson, RolRequest.class);
		Rol rol = new Rol();
		rol = new Rol(rolRequest);
		ReporteDto reporteDto = gson.fromJson(datosJson, ReporteDto.class);
		Map<String, Object> envioDatos = rol.generarReporte(reporteDto, nombrePdfReportes);
		try {
			log.error(CU04_NAME + queryDecoded(envioDatos));
			logUtil.crearArchivoLog(Level.INFO.toString(), CU04_NAME +  this.getClass().getSimpleName(),	this.getClass().getPackage().toString(), "generarDocumento", GENERACION, authentication);
			response = providerRestTemplate.consumirServicioReportes(envioDatos, urlReportes, authentication);
		return MensajeResponseUtil.mensajeConsultaResponse(response,
				ERROR_AL_DESCARGAR_DOCUMENTO);
		} catch (Exception e) {
			String consulta = rol.generarReporte(reporteDto, nombrePdfReportes).get("condicion").toString();
			log.error( CU04_NAME + ERROR_EJECUTAR_QUERY + consulta);
			logUtil.crearArchivoLog(Level.WARNING.toString(), CU04_NAME +  this.getClass().getSimpleName(), this.getClass().getPackage().toString(), ERROR_EJECUTAR_QUERY + consulta, GENERACION,
				authentication);
			throw new IOException("52", e.getCause());
		}

	}

	private String queryDecoded (Map<String, Object> envioDatos ) {
		return new String(DatatypeConverter.parseBase64Binary(envioDatos.get(AppConstantes.QUERY).toString()));
	}
}