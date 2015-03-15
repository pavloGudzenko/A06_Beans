/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import credentials.Credentials;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;


/**
 *
 * @author <Pavlo Gudzenko>
 */
@Path("/product")
public class AssignmentServlet {

    @GET
    @Produces("application/json")
    public Response findAll() throws IOException {
        return Response.ok(getResults("SELECT * FROM product")).build();
    }
    
    
    @GET
    @Path("{id}")
    @Produces("application/json")
    public Response find(@PathParam("id") String id) throws IOException {
        return Response.ok(getResults("SELECT * FROM product WHERE productid = ?", id)).build();
    }


    @POST
    @Consumes("application/json")
    @Produces("application/json")
    
    protected Response doPost(JsonObject json) {
          int rowsInserted = 0;
          Response response = null;
                String name = json.getString("name");
                String description = json.getString("description");
                String quantity = json.getString("quantity");
         rowsInserted = doUpdate("INSERT INTO product (name, description, quantity) VALUES (?, ?, ?)", name, description, quantity);
           if (rowsInserted == 0){
            response = Response.status(500).build();
           } else {
            response = Response.ok(json).build();
           }
           return response;
    }
    

    private JsonArray getResults(String query, String... params) throws IOException {
        JsonArray JSONArray = null;

        try (Connection conn = Credentials.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            for (int i = 1; i <= params.length; i++) {
                pstmt.setString(i, params[i - 1]);
            }
            
            ResultSet rs = pstmt.executeQuery();
            
            JsonArrayBuilder jsonArray = Json.createArrayBuilder();
            while (rs.next()) {
                jsonArray.add(Json.createObjectBuilder()
                        .add("id", rs.getString("productId"))
                        .add("name", rs.getString("name"))
                        .add("description", rs.getString("description"))
                        .add("quantity", rs.getInt("quantity")) );              
            }
            
            JSONArray = jsonArray.build();
        } catch (SQLException ex) {
            Logger.getLogger(AssignmentServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return JSONArray;
    }

    private int doUpdate(String query, String... params) {
        int numChanges = 0;
        try (Connection conn = Credentials.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(query);
            for (int i = 1; i <= params.length; i++) {
                pstmt.setString(i, params[i - 1]);
            }
            numChanges = pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(AssignmentServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return numChanges;
    }
    
    
    @DELETE
    @Path("{id}")
    public Response remove(@PathParam("id") String id) {
        Response deleteResponse = null;
        int rowsDeleted = 0;
        
        rowsDeleted = doUpdate("DELETE FROM product WHERE productId = ?", id);
        
        if (rowsDeleted == 0){
           deleteResponse = Response.status(500).build();
        } else {
           deleteResponse = Response.noContent().build();
        }
       return deleteResponse; 
    }
    

    @PUT
    @Path("{id}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Response edit(@PathParam("id") String id, JsonObject json) {
       int rowsUpdated = 0;
       Response response = null;
            String name = json.getString("name");
                String description = json.getString("description");
                String quantity = json.getString("quantity");
         rowsUpdated = doUpdate("UPDATE product SET name = ?, description =?, quantity = ? WHERE productId = ?", 
                                                 name, description, quantity, id);
           if (rowsUpdated == 0){
            response = Response.status(500).build();
           } else {
            response = Response.ok(json).build();
           }
           return response; 
    }
       
}
