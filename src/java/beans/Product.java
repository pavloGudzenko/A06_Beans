/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package beans;

import javax.ejb.Singleton;

/**
 *
 * @author c0650853
 */
@Singleton
public class Product {
    
  private  int productId;
  private  String productName;
  private  String description;
  private  int qty;

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getDescription() {
        return description;
    }

    public int getQty() {
        return qty;
    }
    
    
    
    
}
