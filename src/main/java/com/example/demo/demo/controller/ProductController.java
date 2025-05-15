//package com.example.demo.demo.controller;
//import com.example.demo.demo.models.Product;
//import com.example.demo.demo.models.ResponseObject;
//import com.example.demo.demo.repositories.ProductRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping(path="/api/v1/Products")
//public class ProductController {
//    @Autowired
//    private ProductRepository repository;
//    @GetMapping("")
//    List<Product> getAllProducts(){
//        return repository.findAll();
//    }
//    //Get detail product
//    @GetMapping("/{id}")
//    ResponseEntity<ResponseObject> findById(@PathVariable Long id){
//        Optional<Product> foundProduct= repository.findById(id);
//        return foundProduct.isPresent()?
//          ResponseEntity.status(HttpStatus.OK).body(
//                  new ResponseObject("OK","Query product successfully",foundProduct)
//          ): ResponseEntity.status(HttpStatus.NOT_FOUND).body(
//                    new ResponseObject("fasle","Cannot find product with id = "+id,"")
//            );
//    }
//    @PostMapping("/insert")
//    ResponseEntity<ResponseObject> insertProduct(@RequestBody Product newProduct){
//        List<Product> foundProducts = repository.findByProductName(newProduct.getProductName().trim());
//        if(!foundProducts.isEmpty()){
//            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
//                    new ResponseObject("failed","product name already taken","")
//            );
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(
//                new ResponseObject("ok","Insert Product Successfully", repository.save(newProduct))
//        );
//    }
//
//    @PutMapping("/{id}")
//    ResponseEntity<ResponseObject> updataProduct(@RequestBody Product newProduct,@PathVariable Long id){
//        Product updatedProduct = repository.findById(id).
//                map(product ->{
//            product.setProductName(newProduct.getProductName());
//            product.setproductYear(newProduct.getproductYear());
//            product.setPrice(newProduct.getPrice());
//            return repository.save(product);
//        }).orElseGet(()->{
////            newProduct.setId(id);
//
//            return repository.save(newProduct);
//
//        });
//        return ResponseEntity.status(HttpStatus.OK).body(
//                new ResponseObject("ok","Update Product successfully",repository.save(updatedProduct))
//        );
//    }
//    //Delete a Product => DELETE method
//    @DeleteMapping("/{id}")
//    ResponseEntity<ResponseObject> deleteProduct(@PathVariable Long id){
//        boolean exists = repository.existsById(id);
//        if(exists){
//                repository.deleteById(id);
//                return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("ok","Delete product successfully",""));
//        }
//        else{
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("failed","Cannot find product to delete",""));
//        }
//    }
//}
//
//
//
