package pe.gob.sunat.redis.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import pe.gob.sunat.redis.entities.Usuario;

public interface UserRepository extends MongoRepository<Usuario, String>{

	Usuario findFirstByUsuario(String usuario);

	void deleteByUsuario(String usuario);

}
