package edu.depaul.cdm.se452.fall2023group1.books;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {

    @GetMapping("/book")
    public String index() {
        return "books/list";
    }
}

