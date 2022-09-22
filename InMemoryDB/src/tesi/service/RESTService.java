package tesi.service;

import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tesi.database.redis.DBService;
import tesi.model.User;

@Path("/korisnici")
public class RESTService {
	
	DBService dbService;
	
	public RESTService() {
		dbService = new DBService();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsers() {
		Set<String> allUsers = dbService.getUsers();
		return (allUsers == null) ? Response.status(Response.Status.NOT_FOUND).build() : Response.status(Response.Status.OK).entity(allUsers).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response add(User user) {
		int result = dbService.add(user);
		if(result > 0)
			return Response.status(Response.Status.CREATED).entity("Korisnik je uspjesno dodat.").build(); // 201 - CREATED
		else if (result < 0)
			return Response.status(Response.Status.CONFLICT).entity("Korisnik sa specifikovanim korisnickim imenom vec postoji.").build(); // 409 - CONFLICT
		else 
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Greska pri dodavanju.").build(); // 500 - INTERNAL SERVER ERROR
	}
	
	@PUT
	@Path("/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(User user, @PathParam("username") String username) {
		if(user == null || username == null)
			return Response.status(Response.Status.BAD_REQUEST).build(); // 400 - BAD REQUEST
		user.setUsername(username);
		if(dbService.update(user))
			return Response.status(Response.Status.OK).entity("Uspjesno azuriranje podataka.").build();
		return Response.status(Response.Status.NOT_FOUND).build(); // 404 - NOT FOUND
	}
	
	@POST
	@Path("/prijava")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(User user) {
		Response.Status status = dbService.validate(user) ? Response.Status.OK : Response.Status.FORBIDDEN;
		return Response.status(status).build();
	}
	
	@DELETE
	@Path("/{username}")
	public Response delete(@PathParam("username") String username) {
		int status = 400;
		if(username == null)
			return Response.status(status).build();
		User user = new User(username, null, null);
		status = (dbService.delete(user)) ? 200 : 404;
		return Response.status(status).build();
	}
}
