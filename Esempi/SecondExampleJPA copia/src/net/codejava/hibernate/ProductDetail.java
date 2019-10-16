package net.codejava.hibernate;
 
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
 
@Entity
@Table(name = "product_detail")
public class ProductDetail {
	@Id
	@Column(name = "PRODUCT_ID")
    private long productId;

    @Column(name = "PART_NUMBER")
    private String partNumber;
    private String dimension;
    private float weight;
    private String manufacturer;
    private String origin;

    @OneToOne
    @MapsId
    private Product product;
 
    public ProductDetail() {
    }
 
    
    
    public long getProductId() {
        return productId;
    }
 
 
    public String getPartNumber() {
        return partNumber;
    }
 
    public Product getProduct() {
        return product;
    }

	public String getDimension() {
		return dimension;
	}

	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
 
    // other getters and setters
}