package bootiful.service;

import org.springframework.ai.client.AiClient;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.Executors;

@SpringBootApplication
public class ServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }

    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {

            var es = Executors.newVirtualThreadPerTaskExecutor();
            var observed = new ConcurrentSkipListSet<String>();
            var threads = new ArrayList<Thread>();
            for (var i = 0; i < 1000; i++) {
                var first = i == 0;
                threads.add(Thread.ofVirtual().unstarted(new Runnable() {
                    @Override
                    public void run() {

                        if (first) observed.add(Thread.currentThread().toString());
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }


                        if (first) observed.add(Thread.currentThread().toString());
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }


                        if (first) observed.add(Thread.currentThread().toString());
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }


                        if (first) observed.add(Thread.currentThread().toString());
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }


                    }
                }));
            }

            for (var t : threads) t.start();

            for (var t : threads) t.join();

            System.out.println(observed);

        };
    }

}


@Controller
@ResponseBody
class CustomerController {

    private final CustomerRepository repository;

    CustomerController(CustomerRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/customers")
    Collection<Customer> customers() {
        return this.repository.findAll();
    }
}


@Controller
@ResponseBody
class StoryController {

    private final AiClient singularity;

    StoryController(AiClient singularity) {
        this.singularity = singularity;
    }

    @GetMapping("/story")
    Map<String, String> story() {

        var prompt = """
                                
                Dear Singularity, 
                                
                Would you please tell me a story about the wonderful CERN research center in Switzerland and France. 
                About its wonderful people, the local food, and the amazing culture. 
                                
                Also, please write it in the style of famed children's author Dr. Seuss.
                                
                Cordially, 
                Josh
                                
                """;

        var responses = this.singularity.generate(prompt);
        return Map.of("story", responses);
    }

}


interface CustomerRepository extends ListCrudRepository<Customer, Integer> {
}

// look mom, no Lombok!
record Customer(@Id Integer id, String name) {
}

