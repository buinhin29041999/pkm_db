package com.phuonghn.pkm;

import com.phuonghn.pkm.repository.PokemonRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@Slf4j
public class PkmApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(PkmApplication.class);
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);
    }

    private static void logApplicationStartup(Environment env) {
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        String serverPort = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path");
        if (StringUtils.isBlank(contextPath)) {
            contextPath = "/";
        }
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using localhost as fallback");
        }
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\t{}://localhost:{}{}\n\t" +
                        "server: \t{}://{}:{}{}\n\t" +
                        "swagger: \t{}://localhost:{}{}swagger-ui.html\n\t" +
                        "Profile(s): \t{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                protocol,
                serverPort,
                contextPath,
                protocol,
                hostAddress,
                serverPort,
                contextPath,
                protocol,
                serverPort,
                contextPath,
                env.getActiveProfiles());
    }

    @Bean
    public CommandLineRunner loadData(PokemonRepo pokemonRepo) {
        return (args) -> {
            try {
//                List<Pokemon> pokemons = pokemonRepo.findAll();
//                if (!DataUtils.isNullOrEmpty(pokemons)) {
//                    for (Pokemon pokemon : pokemons) {
//                        pokemon.setImgIcon(pokemon.getPokedexNumber() + ".png");
//                        pokemonRepo.save(pokemon);
//                    }
//                }
//                File myObj = new File("F:/Code/pkm_database/all_Moves.csv");
//                Scanner myReader = new Scanner(myObj);
//                while (myReader.hasNextLine()) {
//                    String data = myReader.nextLine();
//                    List<String> lstRes = new ArrayList<>();
//                    if (data.contains("\"")) {
//                        List<String> lstTmp = Arrays.asList(data.split("\""));
//                        lstRes.addAll(Arrays.asList(lstTmp.get(0).split(",")));
//                        lstRes.add("'" + lstTmp.get(1) + "'");
//                        lstRes.addAll(Arrays.asList(lstTmp.get(2).split(",")));
//                        if (lstRes.size() == 12) {
//                            Type type = typeRepo.findByName(lstRes.get(3)).get();
//                            lstRes.set(3, String.valueOf(type.getId()));
//                            lstRes.set(4, "'" + lstRes.get(4) + "'");
//                            lstRes.set(5, "'" + lstRes.get(5) + "'");
//                        } else if (lstRes.size() == 11) {
//                            Type type = typeRepo.findByName(lstRes.get(1)).get();
//                            lstRes.set(1, String.valueOf(type.getId()));
//                            lstRes.set(2, "'" + lstRes.get(2) + "'");
//                        }
//                    } else {
//                        lstRes = Arrays.asList(data.split(","));
//
//                        lstRes.set(2, "'" + lstRes.get(2) + "'");
//                        lstRes.set(3, "'" + lstRes.get(3) + "'");
//                        Type type = typeRepo.findByName(lstRes.get(1)).get();
//                        lstRes.set(1, String.valueOf(type.getId()));
//
//                    }
//                    lstRes.set(0, "'" + lstRes.get(0) + "'");
//                    if (!DataUtils.isNullOrEmpty(lstRes.get(7))) {
//                        lstRes.set(7, "'" + lstRes.get(7) + "'");
//                    }
//
//                    for (int i = 0; i < lstRes.size(); i++) {
//                        if (DataUtils.isNullOrEmpty(lstRes.get(i)) || "-".equals(lstRes.get(i))) {
//                            lstRes.set(i, null);
//                        }
//                    }
//                    lstRes.set(3, lstRes.get(3).replace("\'s", " s"));
//                    System.out.println("insert into move(name, typeId, category, effect, power, accuracy, pp, tm, prob, gen) VALUES (" + String.join(",", lstRes) + ");");
//                }
//                myReader.close();
            } catch (Exception e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        };
    }
}
