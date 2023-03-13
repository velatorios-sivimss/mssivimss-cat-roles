package com.imss.sivimss.catroles.beans;


import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import com.imss.sivimss.catroles.model.request.RolRequest;
import com.imss.sivimss.catroles.model.request.UsuarioRequest;
import com.imss.sivimss.catroles.util.AppConstantes;
import com.imss.sivimss.catroles.util.DatosRequest;
import com.imss.sivimss.catroles.util.QueryHelper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class Rol {
	private Integer id;
	private String des_rol;
	private String estatus;
	private Integer nivel;
	private Integer velatorio;
	private String claveAlta;
	private String claveModifica;
	private String claveBaja;




	public Rol(RolRequest rolRequest) {
		this.id = rolRequest.getId();
		this.des_rol = rolRequest.getDes_rol();
		this.estatus = rolRequest.getEstatus();
		this.nivel= rolRequest.getNivel();
		this.velatorio= rolRequest.getVelatorio();
		
	}
	
	public DatosRequest obtenerRoles(DatosRequest request) {
		String query = "SELECT R.ID_ROL AS id, R.DES_ROL AS des, \r\n "
				+ "R.ID_OFICINA AS nivel, R.FEC_ALTA AS fCreacion, \r\n"
				+ "R.ESTATUS AS estatus FROM SVC_ROL AS R ORDER BY ID_ROL ASC";
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);

		return request;
	}

	public DatosRequest catalogoRol() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();
		String query = "SELECT * FROM SVC_ROL";
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		return request;
	}

	public DatosRequest buscarRol(DatosRequest request) {
		String palabra = request.getDatos().get("palabra").toString();
		String query = "SELECT ID_ROL as id, DES_ROL as des \r\n"
				+ " FROM SVC_ROL WHERE DES_ROL = '" + palabra + "' ORDER BY ID_ROL DESC";
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().remove("palabra");
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}
	


	public DatosRequest detalleRol(DatosRequest request) {
		String idRol = request.getDatos().get("id").toString();
		String query = "SELECT ID_ROL as id, DES_ROL as des, \r\n"
				+ "R.ID_OFICINA AS nivel, R.ID_VELATORIO AS velatorio, \r\n"
				+ "R.ESTATUS AS estatus, R.FEC_ALTA AS fCreacion \r\n"
				+ " FROM SVC_ROL AS R WHERE ID_ROL = " + Integer.parseInt(idRol) + " ORDER BY ID_ROL DESC";
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().remove("id");
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}

	public DatosRequest insertar() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();

		final QueryHelper q = new QueryHelper("INSERT INTO SVC_ROL");
		q.agregarParametroValues("DES_ROL", "'" + this.des_rol + "'");
		q.agregarParametroValues("ESTATUS", "1");
		q.agregarParametroValues("FEC_ALTA", "NOW()");
		q.agregarParametroValues("CVE_MATRICULA_ALTA", "'" + this.claveAlta + "'");
		q.agregarParametroValues("FEC_ACTUALIZACION", "NULL");
		q.agregarParametroValues("CVE_MATRICULA_MODIFICA", "NULL");
		q.agregarParametroValues("CVE_MATRICULA_BAJA", "NULL");
		q.agregarParametroValues("FEC_BAJA", "NULL");
		q.agregarParametroValues("ID_OFICINA", "" + this.nivel + "");
		q.agregarParametroValues("ID_VELATORIO", "" + this.velatorio + "");
		
		
		String query = q.obtenerQueryInsertar();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);

		return request;
	}

	public DatosRequest actualizar() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();

		final QueryHelper q = new QueryHelper("UPDATE SVC_ROL");
		q.agregarParametroValues("DES_ROL", "'" + this.des_rol + "'");
		q.agregarParametroValues("ESTATUS", "'" + this.estatus + "'");
		q.agregarParametroValues("ID_OFICINA", "" + this.nivel + "");
		q.agregarParametroValues("CVE_MATRICULA_MODIFICA", "'" + this.claveModifica + "'");
		q.agregarParametroValues("FEC_ACTUALIZACION", "NOW()");
		q.addWhere("ID_ROL = " + this.id);
		String query = q.obtenerQueryActualizar();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		return request;
	}

	public DatosRequest cambiarEstatus() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();
		String query = "UPDATE SVC_ROL SET ESTATUS=!ESTATUS WHERE ID_ROL=" + this.id + ";";
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		return request;
	}
}
