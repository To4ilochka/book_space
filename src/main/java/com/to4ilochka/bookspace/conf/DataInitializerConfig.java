package com.to4ilochka.bookspace.conf;

import com.to4ilochka.bookspace.dto.book.CreateBookRequest;
import com.to4ilochka.bookspace.dto.client.CreateClientRequest;
import com.to4ilochka.bookspace.dto.employee.CreateEmployeeRequest;
import com.to4ilochka.bookspace.mapper.BookMapper;
import com.to4ilochka.bookspace.mapper.ClientMapper;
import com.to4ilochka.bookspace.mapper.EmployeeMapper;
import com.to4ilochka.bookspace.model.Book;
import com.to4ilochka.bookspace.model.Client;
import com.to4ilochka.bookspace.model.Employee;
import com.to4ilochka.bookspace.model.User;
import com.to4ilochka.bookspace.model.enums.AgeGroup;
import com.to4ilochka.bookspace.model.enums.Language;
import com.to4ilochka.bookspace.model.enums.Role;
import com.to4ilochka.bookspace.repo.BookRepository;
import com.to4ilochka.bookspace.repo.ClientRepository;
import com.to4ilochka.bookspace.repo.EmployeeRepository;
import com.to4ilochka.bookspace.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializerConfig {
    @Value("${app.root.admin.email}")
    private String adminEmail;
    @Value("${app.root.admin.password}")
    private String adminPassword;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final BookRepository bookRepository;
    private final ClientMapper clientMapper;
    private final EmployeeMapper employeeMapper;
    private final BookMapper bookMapper;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initRootAdmin() {
        return args -> {
            if (!userRepository.existsByRoles(Role.ROLE_ADMIN)) {
                log.info("Initializing root administrator...");
                User rootAdmin = new User();
                rootAdmin.setEmail(adminEmail);
                rootAdmin.setName("System Admin");
                rootAdmin.setPassword(passwordEncoder.encode(adminPassword));
                rootAdmin.getRoles().add(Role.ROLE_ADMIN);

                userRepository.save(rootAdmin);
                log.info("Root admin initialized successfully with email: {}", adminEmail);
            }
        };
    }

    @Bean
    @Profile("!prod")
    public CommandLineRunner initData() {
        return args -> {
            if (clientRepository.count() == 0) {
                log.info("Initializing clients data...");
                List<Client> clients = Stream.of(
                                new CreateClientRequest("client1@example.com", "password", "Medelyn Wright", new BigDecimal("1000.00")),
                                new CreateClientRequest("client2@example.com", "password", "Landon Phillips", new BigDecimal("1500.50")),
                                new CreateClientRequest("client3@example.com", "password", "Harmony Mason", new BigDecimal("800.75")),
                                new CreateClientRequest("client4@example.com", "password", "Archer Harper", new BigDecimal("1200.25")),
                                new CreateClientRequest("client5@example.com", "password", "Kira Jacobs", new BigDecimal("900.80")),
                                new CreateClientRequest("client6@example.com", "password", "Maximus Kelly", new BigDecimal("1100.60")),
                                new CreateClientRequest("client7@example.com", "password", "Sierra Mitchell", new BigDecimal("1300.45")),
                                new CreateClientRequest("client8@example.com", "password", "Quinton Saunders", new BigDecimal("950.30")),
                                new CreateClientRequest("client9@example.com", "password", "Amina Clarke", new BigDecimal("1050.90")),
                                new CreateClientRequest("john.doe@email.com", "password", "John Doe", BigDecimal.ZERO),
                                new CreateClientRequest("jane.smith@email.com", "password", "Jane Smith", BigDecimal.ZERO),
                                new CreateClientRequest("bob.jones@email.com", "password", "Bob Jones", BigDecimal.ZERO),
                                new CreateClientRequest("alice.white@email.com", "password", "Alice White", BigDecimal.ZERO),
                                new CreateClientRequest("mike.wilson@email.com", "password", "Mike Wilson", BigDecimal.ZERO),
                                new CreateClientRequest("sara.brown@email.com", "password", "Sara Brown", BigDecimal.ZERO),
                                new CreateClientRequest("tom.jenkins@email.com", "password", "Tom Jenkins", BigDecimal.ZERO),
                                new CreateClientRequest("lisa.taylor@email.com", "password", "Lisa Taylor", BigDecimal.ZERO),
                                new CreateClientRequest("david.wright@email.com", "password", "David Wright", BigDecimal.ZERO)
                        ).map(req -> {
                            Client client = clientMapper.toEntity(req);
                            if (client.getUser() != null) {
                                client.getUser().setPassword(passwordEncoder.encode(req.password()));
                            }
                            return client;
                        })
                        .toList();

                List<Client> blockedClients = Stream.of(
                                new CreateClientRequest("client10@example.com", "password", "Bryson Chavez", new BigDecimal("880.20")),
                                new CreateClientRequest("emily.harris@email.com", "password", "Emily Harris", BigDecimal.ZERO)
                        )
                        .map(req -> {
                            Client client = clientMapper.toEntity(req);
                            if (client.getUser() != null) {
                                client.getUser().setPassword(passwordEncoder.encode(req.password()));
                                client.getUser().setLocked(true);
                            }
                            return client;
                        })
                        .toList();

                clientRepository.saveAll(clients);
                clientRepository.saveAll(blockedClients);
                log.debug("Saved {} clients to the database", clients.size());
            }

            if (employeeRepository.count() == 0) {
                log.info("Upgrading existing users to employees...");
                List<CreateEmployeeRequest> employeeRequests = List.of(
                        new CreateEmployeeRequest("john.doe@email.com", "password", "John Doe", "555-123-4567", LocalDate.parse("1990-05-15")),
                        new CreateEmployeeRequest("jane.smith@email.com", "password", "Jane Smith", "555-987-6543", LocalDate.parse("1985-09-20")),
                        new CreateEmployeeRequest("bob.jones@email.com", "password", "Bob Jones", "555-321-6789", LocalDate.parse("1978-03-08")),
                        new CreateEmployeeRequest("alice.white@email.com", "password", "Alice White", "555-876-5432", LocalDate.parse("1982-11-25")),
                        new CreateEmployeeRequest("mike.wilson@email.com", "password", "Mike Wilson", "555-234-5678", LocalDate.parse("1995-07-12")),
                        new CreateEmployeeRequest("sara.brown@email.com", "password", "Sara Brown", "555-876-5433", LocalDate.parse("1989-01-30")),
                        new CreateEmployeeRequest("tom.jenkins@email.com", "password", "Tom Jenkins", "555-345-6789", LocalDate.parse("1975-06-18")),
                        new CreateEmployeeRequest("lisa.taylor@email.com", "password", "Lisa Taylor", "555-789-0123", LocalDate.parse("1987-12-04")),
                        new CreateEmployeeRequest("david.wright@email.com", "password", "David Wright", "555-456-7890", LocalDate.parse("1992-08-22")),
                        new CreateEmployeeRequest("emily.harris@email.com", "password", "Emily Harris", "555-098-7654", LocalDate.parse("1980-04-10"))
                );

                List<String> emails = employeeRequests.stream()
                        .map(CreateEmployeeRequest::email)
                        .toList();

                Map<String, User> existingUsers = userRepository.findAllByEmailIn(emails).stream()
                        .collect(Collectors.toMap(User::getEmail, user -> user));

                List<Employee> employees = employeeRequests.stream()
                        .filter(req -> existingUsers.containsKey(req.email()))
                        .map(req -> {
                            Employee employee = employeeMapper.toEntity(req);
                            User user = existingUsers.get(req.email());
                            user.getRoles().add(Role.ROLE_EMPLOYEE);
                            employee.setId(user.getId());
                            employee.setUser(user);
                            return employee;
                        })
                        .toList();

                employeeRepository.saveAll(employees);
                log.debug("Successfully upgraded {} users to employees", employees.size());
            }

            if (bookRepository.count() == 0) {
                log.info("Initializing books data...");
                List<Book> books = Stream.of(
                                new CreateBookRequest("The Hidden Treasure", "Adventure", AgeGroup.ADULT, new BigDecimal("24.99"), LocalDate.parse("2018-05-15"), "Emily White", 400, "Mysterious journey", "An enthralling adventure", Language.ENGLISH),
                                new CreateBookRequest("Echoes of Eternity", "Fantasy", AgeGroup.TEEN, new BigDecimal("16.50"), LocalDate.parse("2011-01-15"), "Daniel Black", 350, "Magical realms", "A spellbinding tale", Language.ENGLISH),
                                new CreateBookRequest("Whispers in the Shadows", "Mystery", AgeGroup.ADULT, new BigDecimal("29.95"), LocalDate.parse("2018-08-11"), "Sophia Green", 450, "Intriguing suspense", "A gripping mystery", Language.ENGLISH),
                                new CreateBookRequest("The Starlight Sonata", "Romance", AgeGroup.ADULT, new BigDecimal("21.75"), LocalDate.parse("2011-05-15"), "Michael Rose", 320, "Heartwarming love story", "A beautiful journey", Language.ENGLISH),
                                new CreateBookRequest("Beyond the Horizon", "Science Fiction", AgeGroup.CHILD, new BigDecimal("18.99"), LocalDate.parse("2004-05-15"), "Alex Carter", 280, "Interstellar adventure", "An epic sci-fi", Language.ENGLISH),
                                new CreateBookRequest("Dancing with Shadows", "Thriller", AgeGroup.ADULT, new BigDecimal("26.50"), LocalDate.parse("2015-05-15"), "Olivia Smith", 380, "Suspenseful twists", "A thrilling tale", Language.ENGLISH),
                                new CreateBookRequest("Voices in the Wind", "Historical Fiction", AgeGroup.ADULT, new BigDecimal("32.00"), LocalDate.parse("2017-05-15"), "William Turner", 500, "Rich historical setting", "A compelling journey", Language.ENGLISH),
                                new CreateBookRequest("Serenade of Souls", "Fantasy", AgeGroup.TEEN, new BigDecimal("15.99"), LocalDate.parse("2013-05-15"), "Isabella Reed", 330, "Enchanting realms", "A magical fantasy", Language.ENGLISH),
                                new CreateBookRequest("Silent Whispers", "Mystery", AgeGroup.ADULT, new BigDecimal("27.50"), LocalDate.parse("2021-05-15"), "Benjamin Hall", 420, "Intricate detective work", "A mystery on the edge", Language.ENGLISH),
                                new CreateBookRequest("Whirlwind Romance", "Romance", AgeGroup.OTHER, new BigDecimal("23.25"), LocalDate.parse("2022-05-15"), "Emma Turner", 360, "Passionate love affair", "A romance sweeps you off", Language.ENGLISH)
                        )
                        .map(bookMapper::toEntity)
                        .toList();

                bookRepository.saveAll(books);
                log.debug("Saved {} books to the database", books.size());
            }
            log.info("--- Data initialization completed successfully ---");
        };
    }
}