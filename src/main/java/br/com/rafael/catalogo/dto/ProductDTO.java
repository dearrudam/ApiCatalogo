package br.com.rafael.catalogo.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import br.com.rafael.catalogo.entities.Category;
import br.com.rafael.catalogo.entities.Product;

public class ProductDTO implements Serializable {


    private static final long serialVersionUID = 1L;

    private Long id;

    @Size(min = 2, max = 100, message = "O campo nome deve ter entre 2 a 100 caracteres")
    @NotBlank(message = "Campo nome é obrigatorio")
    private String name;

    private String description;

    @Positive(message = "O valor deve ser positivo")
    private Double price;

    private String imgUrl;

    @PastOrPresent(message = "A data não pode ser futura")
    private Instant date;

    private List<CategoryDTO> listaCategories = new ArrayList<>();

    public ProductDTO() {
        // TODO Auto-generated constructor stub
    }

    public ProductDTO(Long id, String name, String description, Double price, String imgUrl, Instant date) {
        super();
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
        this.date = date;
    }


    public ProductDTO(Product entity) {
        this(entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getImgUrl(), entity.getDate());
        entity.getCategories().forEach(cat -> this.listaCategories.add(new CategoryDTO(cat)));
    }

    public ProductDTO(Product entity, Set<Category> categories) {
        this(entity);
        categories.forEach(cat -> this.listaCategories.add(new CategoryDTO(cat)));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }


    public List<CategoryDTO> getListaCategories() {
        return listaCategories;
    }

    public void setListaCategories(List<CategoryDTO> listaCategories) {
        this.listaCategories = listaCategories;
    }


}
