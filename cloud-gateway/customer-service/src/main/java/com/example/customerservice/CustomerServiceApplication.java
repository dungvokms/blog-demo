package com.example.customerservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

import java.time.Duration;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@SpringBootApplication
public class CustomerServiceApplication {

	@Bean
	ApplicationRunner init(CustomerRepository customerRepo) {
		return args -> customerRepo.deleteAll()
				.thenMany(Flux.just("A", "B", "C").map(l -> new Customer(null, l)).flatMap(customerRepo::save))
				.thenMany(customerRepo.findAll())
				.subscribe(System.out::println);
	}

	@Bean
	RouterFunction<?> routes(CustomerRepository cr) {
		return RouterFunctions.route(GET("/customers"), r -> ok().body(cr.findAll(), Customer.class))
				.andRoute(GET("/customers/{id}"), r -> ok().body(cr.findById(r.pathVariable("id")), Customer.class))
				.andRoute(GET("/delay"), r -> ok().body(Flux.just("Hello World").delayElements(Duration.ofSeconds(10)), String.class));
	}

	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApplication.class, args);
	}

}

interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {

}

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
class Customer {
	@Id
	private String id;
	private String name;
}

