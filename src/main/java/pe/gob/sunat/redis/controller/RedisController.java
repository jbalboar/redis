package pe.gob.sunat.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pe.gob.sunat.redis.bean.DetalleUsuarioBean;
import pe.gob.sunat.redis.bean.RespuestaDetalleUsuarioBean;
import pe.gob.sunat.redis.bean.RespuestaUsuariosBean;
import pe.gob.sunat.redis.service.MongoService;
import pe.gob.sunat.redis.service.RedisService;

@RestController
public class RedisController {
	
	@Autowired
	private RedisService iRedisService;
	
	@Autowired
	private MongoService iMongoService;

	@GetMapping("/usuarios")
	public RespuestaUsuariosBean getUsuarios() {
		
		/*Primero se consulta de REDIS*/
		RespuestaUsuariosBean bean = iRedisService.getUsuarios("usuarios");
		
		/*Si no hay en REDIS, se va a la BD*/
		if(bean==null) {
			bean = iMongoService.getUsuarios();
		}
		
		
		return bean;
	}
	
	@GetMapping("/usuarios/{user}")
	public RespuestaDetalleUsuarioBean getUsuario(@PathVariable String user) {
		/*Primero se consulta de REDIS*/
		RespuestaDetalleUsuarioBean bean = iRedisService.getDetalleUsuario("usuario."+user);
		
		/*Si no hay en REDIS, se va a la BD*/
		if(bean == null) {
			bean = iMongoService.getDetalleUsuario(user);
		}
		
		return bean;
	}
	
	@GetMapping("/eliminar/{user}")
	public void eliminarUsuario(@PathVariable String user) {
		iMongoService.eliminarUsuario(user);

	}
	
	@PostMapping("/guardar")
	public void guardarUsuario(@RequestBody DetalleUsuarioBean bean) {
		iMongoService.guardarUsuario(bean);
	}
	
	@GetMapping("/cargaMasiva")
	public void cargaMasiva() {
		iMongoService.cargaMasiva();

	}
	
}
