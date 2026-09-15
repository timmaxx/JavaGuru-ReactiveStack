package com.example.ReactiveDemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ReactiveDemoController {

    @GetMapping("/test")
    public Flux<String> getItems() {
        return getItemsFromDatasource();
    }

    private Flux<String> getItemsFromDatasource() {
        return Flux.just("Java", "Guru", "Dot","BY"); //Publisher
    }

    public static void main(String[] args){
        System.out.println("start");
        new ReactiveDemoController().getItems()
                .log()
                .subscribe();
        System.out.println("end");
    }

}
