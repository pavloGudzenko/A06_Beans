/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beans;

import credentials.Credentials;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Singleton;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;

/**
 *
 * @author c0650853
 */

@Singleton
public class ProductListBean {
    
   private List <Product> productList;

    public ProductListBean(){
        
        try (Connection conn = Credentials.getConnection()) {
            
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM product");
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Product product = new Product();
                 product.setProductId(rs.getInt("productId"));
                 product.setProductName(rs.getString("name"));
                 product.setDescription(rs.getString("description"));
                 product.setQty(rs.getInt("quantity"));
                 
                 productList.add(product);
            }
            
        }  catch (SQLException ex) {
            Logger.getLogger(ProductListBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
   
public void add(Product product){ 
      int rowsInserted = 0;
      
      rowsInserted = doUpdate("INSERT INTO product (productid, name, description, quantity) VALUES (?, ?, ?, ?)", 
              Integer.toString(product.getProductId()), 
              product.getProductName(), 
              product.getDescription(), 
              Integer.toString(product.getQty()));
      
           if (rowsInserted == 0){
               System.out.println("No rows were inserted");
           } else {
               productList.add(product);
           }
}   


public void remove(Product product){ 
    int rowsDeleted = 0;
      
      rowsDeleted = doUpdate("DELETE FROM product WHERE productid = ? AND name = ? AND description = ? AND quantity = ?", 
                             Integer.toString(product.getProductId()),
                             product.getProductName(),
                             product.getDescription(),
                             Integer.toString(product.getQty()));
      
      if (rowsDeleted == 0){
               System.out.println("No rows were Deleted");
           } else {
               productList.remove(product);
           }
}

public void remove(int id){  
        int rowsDeleted = 0;
      
      rowsDeleted = doUpdate("DELETE FROM product WHERE productid = ?", 
                             Integer.toString(productList.get(id).getProductId()));
      
      if (rowsDeleted == 0){
               System.out.println("No rows were Deleted");
           } else {
               productList.remove(id);
           }  
}

public void set(int id, Product product){
    productList.set(id, product);
}

public Product get(int id){
    return productList.get(id);
}

public JsonArray toJSON(){

    JsonArrayBuilder jsonArray = Json.createArrayBuilder();
           
     for (int i = 0; i <= productList.size(); i++)
    {
                jsonArray.add(Json.createObjectBuilder()
                        .add("id", get(i).getProductId())
                        .add("name", get(i).getProductName())
                        .add("description", get(i).getDescription())
                        .add("quantity", get(i).getQty()) );              
            }
            
        return jsonArray.build();
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
            Logger.getLogger(ProductListBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return numChanges;
    }



    
}
