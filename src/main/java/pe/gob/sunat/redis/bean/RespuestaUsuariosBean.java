package pe.gob.sunat.redis.bean;

import java.util.List;

public class RespuestaUsuariosBean extends RespuestaBean{
	private List<UsuariosBean> lstUsuarios;

	public List<UsuariosBean> getLstUsuarios() {
		return lstUsuarios;
	}

	public void setLstUsuarios(List<UsuariosBean> lstUsuarios) {
		this.lstUsuarios = lstUsuarios;
	}
	
	
}
