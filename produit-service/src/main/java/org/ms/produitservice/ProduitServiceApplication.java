package org.ms.produitservice;
import org.ms.produitservice.entities.Produit;
import org.ms.produitservice.repository.ProduitRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
@SpringBootApplication
public class ProduitServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProduitServiceApplication.class, args);
	}
	@Bean
	CommandLineRunner start(ProduitRepository produitRepository, RepositoryRestConfiguration
			repositoryRestConfiguration) {
		repositoryRestConfiguration.exposeIdsFor(Produit.class);
		return args -> {
			produitRepository.save(new Produit(null, "Pc gaming", 1350, 100,"https://img.freepik.com/photos-gratuite/chaise-gamer-neons-multicolores_52683-99741.jpg","pac gaming plus",0));
			produitRepository.save(new Produit(null, "Smart Phone", 230, 20,"https://t3.ftcdn.net/jpg/02/46/10/66/360_F_246106643_zgImuzcun9iudSeJRMa51RydHkMXFfj5.jpg","Iphone 144 derniere generation",20.0));
			produitRepository.save(new Produit(null, "Decodeur", 460, 555,"https://t4.ftcdn.net/jpg/04/13/72/25/360_F_413722537_RJDlpfbifvRNbi6osSTlPyEBpzETTJbk.jpg","IpTv dor free",15.0));
			produitRepository.findAll().forEach(p -> {
				System.out.println(p.getName() + ":" + p.getPrice() + ":" + p.getQuantity());
			});
		};
	}
}