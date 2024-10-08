
package com.vaman.demo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("pokemon")
public class PokemonResource {

	@PersistenceContext(unitName = "pu1")
	private EntityManager entityManager;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Pokemon> getPokemons() {
		logger.info("getPokemons() method invoked.");
		return entityManager.createNamedQuery("getPokemons", Pokemon.class).getResultList();
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Pokemon getPokemonById(@PathParam("id") Integer id) {
		logger.info(id.toString());
		Pokemon pokemon = entityManager.find(Pokemon.class, id);
		if (pokemon == null) {
			throw new NotFoundException("Unable to find pokemon with ID " + id);
		}
		return pokemon;
	}

	@GET
	@Path("name/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Pokemon getPokemonByName(@PathParam("name") String name) {
		logger.info(name);
		TypedQuery<Pokemon> query = entityManager.createNamedQuery("getPokemonByName", Pokemon.class);
		List<Pokemon> list = query.setParameter("name", name).getResultList();
		if (list.isEmpty()) {
			throw new NotFoundException("Unable to find pokemon with name " + name);
		}
		return list.get(0);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional(Transactional.TxType.REQUIRED)
	public Pokemon createPokemon(Pokemon pokemon) {
		logger.info(pokemon.toString());
		try {
			PokemonType pokemonType = entityManager.createNamedQuery("getPokemonTypeById", PokemonType.class)
					.setParameter("id", pokemon.getType()).getSingleResult();
			pokemon.setPokemonType(pokemonType);
			entityManager.persist(pokemon);
			return pokemon;
		} catch (Exception e) {
			throw new BadRequestException("Unable to create pokemon with ID " + pokemon.getId());
		}
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(Transactional.TxType.REQUIRED)
	public Pokemon updatePokemon(Pokemon pokemon) {
		logger.info(pokemon.toString());
		return entityManager.merge(pokemon);
	}

	@DELETE
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional(Transactional.TxType.REQUIRED)
	public Pokemon deletePokemon(@PathParam("id") Integer id) {
		logger.info(id.toString());
		Pokemon pokemon = getPokemonById(id);
		entityManager.remove(pokemon);
		return pokemon;
	}

}

//
//package com.vaman.demo;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.persistence.TypedQuery;
//import jakarta.transaction.Transactional;
//import jakarta.ws.rs.BadRequestException;
//import jakarta.ws.rs.Consumes;
//import jakarta.ws.rs.DELETE;
//import jakarta.ws.rs.GET;
//import jakarta.ws.rs.NotFoundException;
//import jakarta.ws.rs.POST;
//import jakarta.ws.rs.Path;
//import jakarta.ws.rs.PathParam;
//import jakarta.ws.rs.Produces;
//import jakarta.ws.rs.core.MediaType;
//import java.util.List;
//
///**
// * This class implements REST endpoints to interact with Pokemons. The following
// * operations are supported:
// *
// * <ul>
// * <li>GET /pokemon: Retrieve list of all pokemons</li>
// * <li>GET /pokemon/{id}: Retrieve single pokemon by ID</li>
// * <li>GET /pokemon/name/{name}: Retrieve single pokemon by name</li>
// * <li>DELETE /pokemon/{id}: Delete a pokemon by ID</li>
// * <li>POST /pokemon: Create a new pokemon</li>
// * </ul>
// *
// * Pokemon, and Pokemon character names are trademarks of Nintendo.
// */
//@Path("pokemon")
//public class PokemonResource {
//
//	@PersistenceContext(unitName = "pu1")
//	private EntityManager entityManager;
//
//	@GET
//	@Produces(MediaType.APPLICATION_JSON)
//	public List<Pokemon> getPokemons() {
//		return entityManager.createNamedQuery("getPokemons", Pokemon.class).getResultList();
//	}
//
//	@GET
//	@Path("{id}")
//	@Produces(MediaType.APPLICATION_JSON)
//	public Pokemon getPokemonById(@PathParam("id") String id) {
//		Pokemon pokemon = entityManager.find(Pokemon.class, Integer.valueOf(id));
//		if (pokemon == null) {
//			throw new NotFoundException("Unable to find pokemon with ID " + id);
//		}
//		return pokemon;
//	}
//
//	@DELETE
//	@Path("{id}")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional(Transactional.TxType.REQUIRED)
//	public void deletePokemon(@PathParam("id") String id) {
//		Pokemon pokemon = getPokemonById(id);
//		entityManager.remove(pokemon);
//	}
//
//	@GET
//	@Path("name/{name}")
//	@Produces(MediaType.APPLICATION_JSON)
//	public Pokemon getPokemonByName(@PathParam("name") String name) {
//		TypedQuery<Pokemon> query = entityManager.createNamedQuery("getPokemonByName", Pokemon.class);
//		List<Pokemon> list = query.setParameter("name", name).getResultList();
//		if (list.isEmpty()) {
//			throw new NotFoundException("Unable to find pokemon with name " + name);
//		}
//		return list.get(0);
//	}
//
//	@POST
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Transactional(Transactional.TxType.REQUIRED)
//	public void createPokemon(Pokemon pokemon) {
//		try {
//			PokemonType pokemonType = entityManager.createNamedQuery("getPokemonTypeById", PokemonType.class)
//					.setParameter("id", pokemon.getType()).getSingleResult();
//			pokemon.setPokemonType(pokemonType);
//			entityManager.persist(pokemon);
//		} catch (Exception e) {
//			throw new BadRequestException("Unable to create pokemon with ID " + pokemon.getId());
//		}
//	}
//}
