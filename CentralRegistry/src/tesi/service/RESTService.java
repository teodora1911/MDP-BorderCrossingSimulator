package tesi.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import tesi.model.Passengers;
import tesi.util.Utility;

import javax.ws.rs.core.MediaType;

@Path("/potjernice")
public class RESTService {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getDetectedOnWarrantPassengers() {
		try(BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(PassengerService.WarrantFilePath)))){
			XmlMapper mapper = new XmlMapper();
			String xmlContent = "";
			String line = null;
			while((line = in.readLine()) != null)
				xmlContent += line;
			Passengers passengers = xmlContent.isEmpty() ? new Passengers() : mapper.readValue(xmlContent, Passengers.class);
			return Response.status(Response.Status.OK).entity(passengers.getPassengers()).build();
		} catch (Exception e) {
			Utility.logger.log(Level.SEVERE, e.fillInStackTrace().toString());
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build(); // 500 - Internal Server Error
		}
	}
}
