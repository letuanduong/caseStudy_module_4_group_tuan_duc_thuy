package com.c0220i1.group.controller;

import com.c0220i1.group.model.CartLine;
import com.c0220i1.group.model.Product;
import com.c0220i1.group.service.products.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

@Controller
@SessionAttributes("mycart")
  public class CartController {

    @ModelAttribute("mycart")
        public HashMap<Long, CartLine> showMyCart(){
            return new HashMap<>();
    }
    @Autowired
    private ProductService productService;



    @GetMapping("/mycart")
    public ModelAndView cartPage(@ModelAttribute("mycart")HashMap<Long,CartLine> mycart){
      ModelAndView modelAndView = new ModelAndView("cart");
      double amount = 0;
      for(CartLine cartLine:mycart.values()){
          amount+=cartLine.getAmount();
      }
      modelAndView.addObject("amount",amount);
      modelAndView.addObject("mycart",mycart);
       return modelAndView;

    }
    @GetMapping("/addcart/{id}")
    public ModelAndView addProductToCart(@PathVariable Long id,
                                         @ModelAttribute("mycart")HashMap<Long,CartLine> mycart){
        ModelAndView modelAndView = new ModelAndView("redirect:/");
        if(mycart==null){
            mycart= new HashMap<Long,CartLine>();
        }
        Product product =productService.findById(id).orElse(null);
        if(!mycart.containsKey(id)){
            CartLine cartLine = new CartLine(product,1);
            mycart.put(id,cartLine);
        } else {
            CartLine cartLine= mycart.get(id);
            cartLine.setQuantity(cartLine.getQuantity()+1);
            mycart.put(id,cartLine);
        }
        modelAndView.addObject("mycart",mycart);
        return modelAndView;
    }
    @DeleteMapping("/removefromcart/{id}")
    @ResponseBody
    public CartLine deleteProductFromToCart(@PathVariable Long id,
                                            @ModelAttribute("mycart")HashMap<Long,CartLine> mycart){

       CartLine cartLine= mycart.get(id);
       mycart.remove(id);
       return cartLine;

    }
    @PostMapping("/updatecart")
    public ModelAndView  updateToCart(@RequestParam("quantity_pro")int[] quantity,
                               @ModelAttribute("mycart")HashMap<Long,CartLine> mycart){
      Set<Long> keys= mycart.keySet();
      int index=0;
        for(Long key:keys){
            CartLine cartLine=mycart.get(key);
            cartLine.setQuantity(quantity[index]);;
            mycart.put(key,cartLine);
            index++;
        }
        double amount=0;

        for(CartLine cartLine:mycart.values()){
            amount+=cartLine.getAmount();
        }
        ModelAndView modelAndView = new ModelAndView("cart");
        modelAndView.addObject("amount",amount);
        modelAndView.addObject("mycart",mycart);
        return modelAndView;
    }
    @GetMapping("/clearallcart")
    public ModelAndView showCartPageWhenclearCart(@ModelAttribute("mycart")HashMap<Long,CartLine> mycart){
        ModelAndView modelAndView = new ModelAndView("redirect:/");
        mycart.clear();
        modelAndView.addObject("mycart",mycart);
        return modelAndView;
    }




    }

