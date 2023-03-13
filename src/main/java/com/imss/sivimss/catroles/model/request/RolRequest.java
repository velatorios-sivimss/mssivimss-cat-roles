package com.imss.sivimss.catroles.model.request;
import com.fasterxml.jackson.annotation.JsonIgnoreType;

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
@JsonIgnoreType(value = true)
public class RolRequest {
	private Integer id;
	private String des_rol;
	private String estatus;
	private Integer nivel;
	private Integer velatorio;
	private String claveAlta;
	
	
}
