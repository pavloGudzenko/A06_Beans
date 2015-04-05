/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beans;

import java.util.List;
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

    public ProductListBean() {
    }
   
public void add(Product product){  
    productList.add(product);
}   

public void remove(Product product){  
    productList.remove(product);
}

public void remove(int id){  
    productList.remove(id);
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



    
}
