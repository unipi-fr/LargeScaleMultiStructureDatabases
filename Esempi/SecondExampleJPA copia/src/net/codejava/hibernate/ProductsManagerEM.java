package net.codejava.hibernate;
 
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

 
/**
 * This program demonstrates using Hibernate framework to manage a
 * bidirectional one-to-one association on a primary key using
 * annotations.
 * @author www.codejava.net
 *
 */
public class ProductsManagerEM {
 
    private static EntityManager entityManager;
	private static EntityManagerFactory factory;

	public static void main(String[] args) {
        try {   
        	factory = Persistence.createEntityManagerFactory("productsdb");
            entityManager = factory.createEntityManager();


          } catch (Exception ex) {
  		ex.printStackTrace();
  		System.out.println("A problem occurred in Creating Entity Manager!");}
         
        // creates a new product
        Product product = new Product();
        product.setName("Civic2");
        product.setDescription("Comfortable, fuel-saving car");
        product.setPrice(20000);
         
        // creates product detail
        ProductDetail detail = new ProductDetail();
        detail.setPartNumber("ABCDEFGHIJKL");
        detail.setDimension("2,5m x 1,4m x 1,2m");
        detail.setWeight(1000);
        detail.setManufacturer("Honda Automobile");
        detail.setOrigin("Japan");
         
        // sets the bi-directional association
        product.setProductDetail(detail);
        detail.setProduct(product);
         
        // persists the product
      	 
         try {
        	 entityManager.getTransaction().begin();
           	entityManager.persist(product);           	
           	entityManager.getTransaction().commit();
            System.out.println("Product Added");


           } catch (Exception ex) {
   		ex.printStackTrace();
   		System.out.println("A problem occurred in Persistence!");}
         
         //creates a new product
         Product product2 = new Product();
         product2.setName("Punto");
         product2.setDescription("Non Comfortable, little car");
         product2.setPrice(10000);
          
         // creates product detail
         ProductDetail detail2 = new ProductDetail();
         detail2.setPartNumber("trwrwtw");
         detail2.setDimension("2,5m x 1,4m x 1,2m");
         detail2.setWeight(2000);
         detail2.setManufacturer("FIAT Automobile");
         detail2.setOrigin("Italy");
          
         // sets the bi-directional association
         product2.setProductDetail(detail2);
         detail2.setProduct(product2);
          
         // persists the product
       	 
          try {
         	 entityManager.getTransaction().begin();
            	entityManager.persist(product2);           	
            	entityManager.getTransaction().commit();
             System.out.println("Second Product Added");


            } catch (Exception ex) {
    		ex.printStackTrace();
    		System.out.println("A problem occurred in Persistence!");}
        
      // queries all products
         List<Product> listProducts = entityManager.createQuery("from Product").getResultList();
         for (Product aProd : listProducts) {
             String info = "Product: " + aProd.getName() + "\n";
             info += "\tDescription: " + aProd.getDescription() + "\n";
             info += "\tPrice: $" + aProd.getPrice() + "\n";
              
             ProductDetail aDetail = aProd.getProductDetail();
             info += "\tPart number: " + aDetail.getPartNumber() + "\n";
             info += "\tDimension: " + aDetail.getDimension() + "\n";
             info += "\tWeight: " + aDetail.getWeight() + "\n";
             info += "\tManufacturer: " + aDetail.getManufacturer() + "\n";
             info += "\tOrigin: " + aDetail.getOrigin() + "\n";
              
             System.out.println(info);
         }
         
        entityManager.close();
    }
 
}