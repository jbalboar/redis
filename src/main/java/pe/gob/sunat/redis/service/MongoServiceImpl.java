package pe.gob.sunat.redis.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import pe.gob.sunat.redis.bean.DetalleUsuarioBean;
import pe.gob.sunat.redis.bean.RespuestaDetalleUsuarioBean;
import pe.gob.sunat.redis.bean.RespuestaUsuariosBean;
import pe.gob.sunat.redis.bean.UsuariosBean;
import pe.gob.sunat.redis.entities.Usuario;
import pe.gob.sunat.redis.repository.UserRepository;

@Service
public class MongoServiceImpl implements MongoService {

	@Autowired
    private UserRepository userRepository;
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@Override
	public RespuestaUsuariosBean getUsuarios() {
		List<Usuario> lstUsuariosMongo = userRepository.findAll();
		RespuestaUsuariosBean bean = null;
		if(lstUsuariosMongo!=null && !lstUsuariosMongo.isEmpty()) {
			bean = new RespuestaUsuariosBean();
			bean.setOrigen("MONGODB");
			List<UsuariosBean> lstUsuarios = new ArrayList<UsuariosBean>();
			for (Usuario usuario : lstUsuariosMongo) {
				UsuariosBean uBean = new UsuariosBean();
				uBean.setUser(usuario.getUsuario());
				lstUsuarios.add(uBean);
				/*Ademas cada usuario que se obtiene se agrega al REDIS*/
				redisTemplate.opsForList().rightPush("usuarios", usuario.getUsuario());
			}
			bean.setLstUsuarios(lstUsuarios);
		}
		
		return bean;
	}

	@Override
	public RespuestaDetalleUsuarioBean getDetalleUsuario(String usuario) {
		RespuestaDetalleUsuarioBean bean = new RespuestaDetalleUsuarioBean();
		Usuario user = userRepository.findFirstByUsuario(usuario);
		
		if(user!=null) {
			bean.setOrigen("MONGODB");
			DetalleUsuarioBean uBean = new DetalleUsuarioBean();
			uBean.setUsuario(user.getUsuario());
			uBean.setNombre(user.getNombre());
			uBean.setApellido(user.getApellido());
			uBean.setDireccion(user.getDireccion());			
			bean.setDatos(uBean);
			
			/*Ademas seteamos en el REDIS el usuario*/
			String jsonString = convertObjectToJsonString(uBean);
			redisTemplate.opsForValue().set("usuario."+usuario, jsonString);
			
		}
		
		return bean;
	}
	
	@Override
	public void guardarUsuario(DetalleUsuarioBean bean) {
		//Primero guardamos en Mongo
		Usuario user = new Usuario();
		user.setUsuario(bean.getUsuario());
		user.setNombre(bean.getNombre());
		user.setApellido(bean.getApellido());
		user.setDireccion(bean.getDireccion());
		userRepository.save(user);
		
		/*Ahora guardamos en REDIS*/
		//Agregamos al list
		redisTemplate.opsForList().rightPush("usuarios", bean.getUsuario());
		
		//El detalle de usuario
		String jsonString = convertObjectToJsonString(bean);
		redisTemplate.opsForValue().set("usuario."+bean.getUsuario(), jsonString);
		
	}
	
	@Override
	public void eliminarUsuario(String usuario) {
		/*Eliminar en Mongo*/
		userRepository.deleteByUsuario(usuario);
		
		/*Eliminamos de REDIS*/
		redisTemplate.delete("usuario." + usuario);
		
		Long index = redisTemplate.opsForList().indexOf("usuarios", usuario);
		
		redisTemplate.opsForList().remove("usuarios", index, usuario);
		
	}
	
	private String convertObjectToJsonString(Object object) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting object to JSON string", e);
        }
    }



}
