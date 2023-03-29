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
	
	private Integer idRol;
	private String desRol;
	private Integer estatusRol;
	private Integer nivel;
	private String claveAlta;
	private String claveModifica;
	private String claveBaja;
	
	private static final String CURRENT_TIMESTAMP = "CURRENT_TIMESTAMP()";
	private static final String ID_USUARIO_MODIFICA = "ID_USUARIO_MODIFICA";
	private static final String FEC_ACTUALIZACION = "FEC_ACTUALIZACION";
	private static final String ID_USUARIO_BAJA = "ID_USUARIO_BAJA";
	private static final String UPDATE_SVC_ROL = "UPDATE SVC_ROL";
	private static final String CVE_ESTATUS = "CVE_ESTATUS";
	private static final String FEC_BAJA = "FEC_BAJA";
	private static final String DES_ROL = "DES_ROL";
	private static final String ID_ROL = "ID_ROL = ";
	private static final String NULL = "NULL";


	public Rol(RolRequest rolRequest) {
		this.idRol = rolRequest.getIdRol();
		this.desRol = rolRequest.getDesRol();
		this.estatusRol = rolRequest.getEstatusRol();
		this.nivel= rolRequest.getNivel();
	}
	public Rol(UsuarioRequest usuarioRequest) {
		this.nivel= usuarioRequest.getIdOficina();
		this.idRol = usuarioRequest.getIdRol();
	}
	
	public DatosRequest obtenerRoles(DatosRequest request) {
		String query = "SELECT R.ID_ROL AS idRol, R.DES_ROL AS desRol, \r\n "
				+ "NO.DES_NIVELOFICINA AS desNivelOficina, date_format(R.FEC_ALTA, \"%d/%m/%Y\") AS fCreacion, \r\n"
				+ "R.CVE_ESTATUS AS estatus FROM SVC_ROL AS R INNER JOIN SVC_NIVEL_OFICINA NO  ON R.ID_OFICINA = NO.ID_OFICINA "
				+ "ORDER BY ID_ROL ASC";
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);

		return request;
	}

	public DatosRequest catalogoRol() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();
		String query = "SELECT * FROM SVC_ROL R  ORDER BY ID_ROL ASC";
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		return request;
	}

	public DatosRequest buscarFiltrosRol(DatosRequest request, Rol rol) {
		StringBuilder query = new StringBuilder(" SELECT  ID_ROL as idRol, DES_ROL as desRol, NO.DES_NIVELOFICINA AS desNivelOficina, R.CVE_ESTATUS AS estatusRol  FROM SVC_ROL AS R "
				+ " INNER JOIN SVC_NIVEL_OFICINA NO  ON R.ID_OFICINA = NO.ID_OFICINA ");
		query.append(" WHERE IFNULL(ID_ROL,0) > 0" );
		if (rol.getNivel() != null) {
			query.append(" AND R.ID_OFICINA = ").append(this.getNivel());
		}
		if (this.getIdRol() != null) {
			query.append(" AND R.ID_ROL = ").append(this.getIdRol());
		}
		
		query.append(" ORDER BY R.ID_ROL DESC");
		
		String encoded = DatatypeConverter.printBase64Binary(query.toString().getBytes());
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}
	


	public DatosRequest detalleRol(DatosRequest request) {
		String query = "SELECT R.ID_ROL as id, R.DES_ROL as desRol, \r\n"
				+ "NO.DES_NIVELOFICINA AS desNivelOficina, R.CVE_ESTATUS AS estatusRol,\r\n"
				+ "date_format(R.FEC_ALTA, \"%d/%m/%Y\") AS fCreacion FROM SVC_ROL AS R INNER JOIN SVC_NIVEL_OFICINA NO  ON R.ID_OFICINA = NO.ID_OFICINA WHERE ID_ROL = " 
				+ Integer.parseInt(request.getDatos().get("id").toString()) + " ORDER BY ID_ROL DESC";
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		request.getDatos().remove("id");
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
	}

	public DatosRequest insertar() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();

		final QueryHelper q = new QueryHelper("INSERT INTO SVC_ROL");
		q.agregarParametroValues(DES_ROL, "'" + this.desRol + "'");
		q.agregarParametroValues(CVE_ESTATUS, "1");
		q.agregarParametroValues("FEC_ALTA", CURRENT_TIMESTAMP);
		q.agregarParametroValues("ID_USUARIO_ALTA", "'" + this.claveAlta + "'");
		q.agregarParametroValues(FEC_ACTUALIZACION, NULL );
		q.agregarParametroValues(ID_USUARIO_MODIFICA, NULL);
		q.agregarParametroValues(ID_USUARIO_BAJA, NULL);
		q.agregarParametroValues(FEC_BAJA , NULL);
		q.agregarParametroValues("ID_OFICINA", "" + this.nivel + "");
		
		String query = q.obtenerQueryInsertar();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		
		request.setDatos(parametro);
		
		return request;
	}

	public DatosRequest actualizar() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();

		final QueryHelper q = new QueryHelper(UPDATE_SVC_ROL);
		q.agregarParametroValues(DES_ROL, "'" + this.desRol + "'");
		q.agregarParametroValues(CVE_ESTATUS, "" + this.estatusRol +"");
		q.agregarParametroValues("ID_OFICINA", "" + this.nivel + "");
		q.agregarParametroValues(ID_USUARIO_MODIFICA, "'" + this.claveModifica + "'");
		q.agregarParametroValues(FEC_ACTUALIZACION, CURRENT_TIMESTAMP);
		q.addWhere(ID_ROL + this.idRol);
		
		String query = q.obtenerQueryActualizar();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		
		return request;
	}
	
	public DatosRequest cambiarEstatus() {
		DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();

		final QueryHelper q = new QueryHelper(UPDATE_SVC_ROL);
		q.agregarParametroValues(CVE_ESTATUS, "" + this.estatusRol +"");
		q.agregarParametroValues(ID_USUARIO_MODIFICA, "'" + this.claveModifica + "'");
		q.agregarParametroValues(FEC_ACTUALIZACION, CURRENT_TIMESTAMP);
		q.addWhere(ID_ROL + this.idRol);
		
		String query = q.obtenerQueryActualizar();
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		
		return request;
	}
}