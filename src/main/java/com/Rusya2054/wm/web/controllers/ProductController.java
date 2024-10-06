//package com.Rusya2054.wm.web.controllers;
//
//import com.Rusya2054.wm.web.models.Product;
//import com.Rusya2054.wm.web.services.ProductService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//
///**
// * Контролирует подачу
// * Обращение как пользователей к приложению
// */
//@Controller
//@RequiredArgsConstructor
//public class ProductController {
//    private final ProductService productService;
//
//    /*
//    Внедрение сервиса через IoD внедрение зависимости
//    (аннотация @Service, и он же как раз равен равен Component)
//     */
//
//    /**
//     * Контроллер основной страницы
//     * @return название предславления, т.е. "products"
//     */
//    @GetMapping("/")
//    public String products( @RequestParam(name = "title", required = false) String title, Model model){
//        // передача списка всех товаров
//        model.addAttribute("products", productService.listProducts(title));
//        return "products";
//    }
//
//    @PostMapping("/product/create")
//    public String createProduct(@RequestParam("file1")MultipartFile file1,
//                                @RequestParam("file2")MultipartFile file2,
//                                @RequestParam("file3")MultipartFile file3,
//                                Product product) throws IOException {
//        if (!productService.isEmptyProduct(product)){
//             productService.saveProduct(product, file1, file2, file3);
//             return "redirect:/";
//        }
//        return "redirect:/";
//    }
//
//    @PostMapping("/product/delete/{id}")
//    public String deleteProduct(@PathVariable Long id){
//        productService.deleteProduct(id);
//        return "redirect:/";
//    }
//
//    @GetMapping("/product/{id}")
//    public String productInfo(@PathVariable Long id, Model model){
//        Product product = productService.getProductById(id);
//        model.addAttribute("product", product);
//        model.addAttribute("images", product.getImages());
//        return "product-info";
//    }
//
//}
