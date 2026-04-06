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
                                new CreateBookRequest("The Crimson Crown", "Fantasy", AgeGroup.TEEN, new BigDecimal("19.99"), LocalDate.parse("2020-10-12"), "Arthur Pendelton", 410, "Royal intrigue", "An epic tale of power and magic", Language.ENGLISH),
                                new CreateBookRequest("Quantum Paradox", "Science Fiction", AgeGroup.ADULT, new BigDecimal("25.50"), LocalDate.parse("2022-03-05"), "Elena Rostova", 340, "Time travel mystery", "Mind-bending sci-fi adventure", Language.ENGLISH),
                                new CreateBookRequest("The Sunken City", "Adventure", AgeGroup.CHILD, new BigDecimal("14.50"), LocalDate.parse("2019-07-22"), "Mark Davis", 210, "Underwater exploration", "Exciting deep sea journey", Language.ENGLISH),
                                new CreateBookRequest("Midnight Whispers", "Horror", AgeGroup.ADULT, new BigDecimal("21.00"), LocalDate.parse("2016-10-31"), "Stephen King", 450, "Creepy small town", "A terrifying horror story", Language.ENGLISH),
                                new CreateBookRequest("The Last Ember", "Fantasy", AgeGroup.TEEN, new BigDecimal("17.25"), LocalDate.parse("2021-11-18"), "Laura White", 380, "Dying magic world", "A quest to save the realm", Language.ENGLISH),
                                new CreateBookRequest("Neon Dreams", "Cyberpunk", AgeGroup.ADULT, new BigDecimal("28.90"), LocalDate.parse("2023-01-20"), "Victor Vance", 510, "Dystopian future city", "A gritty cyberpunk thriller", Language.ENGLISH),
                                new CreateBookRequest("The Lost Kingdom", "Historical Fiction", AgeGroup.ADULT, new BigDecimal("24.00"), LocalDate.parse("2014-04-14"), "James Rollins", 480, "Ancient secrets", "Uncovering a lost civilization", Language.ENGLISH),
                                new CreateBookRequest("Summer of Secrets", "Romance", AgeGroup.TEEN, new BigDecimal("15.99"), LocalDate.parse("2019-06-15"), "Chloe Adams", 320, "Summer romance", "A heartwarming teen love story", Language.ENGLISH),
                                new CreateBookRequest("The Clockwork Heart", "Steampunk", AgeGroup.TEEN, new BigDecimal("18.50"), LocalDate.parse("2017-09-09"), "Fiona Gale", 390, "Mechanical wonders", "A thrilling steampunk adventure", Language.ENGLISH),
                                new CreateBookRequest("Shadows of the Past", "Mystery", AgeGroup.ADULT, new BigDecimal("22.95"), LocalDate.parse("2020-02-28"), "David Holmes", 415, "Cold case investigation", "A gripping detective mystery", Language.ENGLISH),
                                new CreateBookRequest("The Emerald Forest", "Fantasy", AgeGroup.CHILD, new BigDecimal("16.00"), LocalDate.parse("2015-12-01"), "Sarah Jenkins", 250, "Magical creatures", "A wonderful journey into the woods", Language.ENGLISH),
                                new CreateBookRequest("Galactic Frontiers", "Science Fiction", AgeGroup.ADULT, new BigDecimal("29.99"), LocalDate.parse("2022-08-14"), "Robert Cline", 550, "Space colonization", "An epic space opera", Language.ENGLISH),
                                new CreateBookRequest("Tears of the Sun", "Historical Fiction", AgeGroup.ADULT, new BigDecimal("26.50"), LocalDate.parse("2018-03-10"), "Maria Rossi", 430, "War and love", "A touching historical drama", Language.ENGLISH),
                                new CreateBookRequest("The Silent Patient", "Thriller", AgeGroup.ADULT, new BigDecimal("20.00"), LocalDate.parse("2019-05-05"), "Alex Michaelides", 360, "Psychological suspense", "A mind-bending psychological thriller", Language.ENGLISH),
                                new CreateBookRequest("The Dragon's Lair", "Fantasy", AgeGroup.TEEN, new BigDecimal("18.99"), LocalDate.parse("2021-07-07"), "Christopher Paolini", 490, "Dragon riders", "A classic fantasy epic", Language.ENGLISH),
                                new CreateBookRequest("Echoes of the Mind", "Psychology", AgeGroup.OTHER, new BigDecimal("35.00"), LocalDate.parse("2020-11-11"), "Dr. Anna Freud", 310, "Understanding thoughts", "A deep dive into human psychology", Language.ENGLISH),
                                new CreateBookRequest("The Silver Dagger", "Mystery", AgeGroup.ADULT, new BigDecimal("23.45"), LocalDate.parse("2017-08-22"), "Agatha Christie", 280, "Classic whodunit", "A brilliant murder mystery", Language.ENGLISH),
                                new CreateBookRequest("Starfall", "Science Fiction", AgeGroup.TEEN, new BigDecimal("17.50"), LocalDate.parse("2023-04-18"), "Luna Lovegood", 340, "Meteor strike survival", "A thrilling apocalyptic survival story", Language.ENGLISH),
                                new CreateBookRequest("The Golden Key", "Adventure", AgeGroup.CHILD, new BigDecimal("12.99"), LocalDate.parse("2016-09-30"), "George MacDonald", 190, "Finding the hidden door", "A magical childhood adventure", Language.ENGLISH),
                                new CreateBookRequest("Whispering Pines", "Horror", AgeGroup.ADULT, new BigDecimal("21.99"), LocalDate.parse("2022-10-15"), "Blake Crouch", 400, "Isolated cabin terror", "A chilling horror novel", Language.ENGLISH),
                                new CreateBookRequest("The Glass Slipper", "Romance", AgeGroup.TEEN, new BigDecimal("14.95"), LocalDate.parse("2018-02-14"), "Ella Cinders", 290, "Modern fairy tale", "A sweet romantic comedy", Language.ENGLISH),
                                new CreateBookRequest("City of Brass", "Fantasy", AgeGroup.ADULT, new BigDecimal("27.00"), LocalDate.parse("2019-11-20"), "S.A. Chakraborty", 520, "Djinn mythology", "A rich Middle Eastern fantasy", Language.ENGLISH),
                                new CreateBookRequest("The Martian Chronicles", "Science Fiction", AgeGroup.ADULT, new BigDecimal("19.50"), LocalDate.parse("1950-05-04"), "Ray Bradbury", 260, "Life on Mars", "A classic collection of sci-fi tales", Language.ENGLISH),
                                new CreateBookRequest("Treasure Island", "Adventure", AgeGroup.CHILD, new BigDecimal("10.00"), LocalDate.parse("1883-05-23"), "Robert Louis Stevenson", 240, "Pirates and gold", "The ultimate pirate adventure", Language.ENGLISH),
                                new CreateBookRequest("The Da Vinci Code", "Thriller", AgeGroup.ADULT, new BigDecimal("25.99"), LocalDate.parse("2003-03-18"), "Dan Brown", 480, "Religious conspiracy", "A fast-paced historical thriller", Language.ENGLISH),
                                new CreateBookRequest("Pride and Prejudice", "Romance", AgeGroup.ADULT, new BigDecimal("15.50"), LocalDate.parse("1813-01-28"), "Jane Austen", 430, "Classic romance", "A timeless story of love and society", Language.ENGLISH),
                                new CreateBookRequest("The Book Thief", "Historical Fiction", AgeGroup.TEEN, new BigDecimal("20.99"), LocalDate.parse("2005-09-01"), "Markus Zusak", 580, "WWII Germany", "A moving story narrated by Death", Language.ENGLISH),
                                new CreateBookRequest("Dune", "Science Fiction", AgeGroup.ADULT, new BigDecimal("28.50"), LocalDate.parse("1965-08-01"), "Frank Herbert", 600, "Desert planet politics", "A monumental sci-fi epic", Language.ENGLISH),
                                new CreateBookRequest("Harry Potter", "Fantasy", AgeGroup.CHILD, new BigDecimal("22.00"), LocalDate.parse("1997-06-26"), "J.K. Rowling", 320, "Boy wizard", "A magical journey begins", Language.ENGLISH),
                                new CreateBookRequest("Gone Girl", "Thriller", AgeGroup.ADULT, new BigDecimal("24.00"), LocalDate.parse("2012-06-05"), "Gillian Flynn", 415, "Missing wife", "A dark psychological thriller", Language.ENGLISH),
                                new CreateBookRequest("The Hobbit", "Fantasy", AgeGroup.CHILD, new BigDecimal("18.00"), LocalDate.parse("1937-09-21"), "J.R.R. Tolkien", 310, "A journey there", "A fantastic adventure in Middle-earth", Language.ENGLISH),
                                new CreateBookRequest("1984", "Science Fiction", AgeGroup.ADULT, new BigDecimal("16.99"), LocalDate.parse("1949-06-08"), "George Orwell", 328, "Dystopian surveillance", "A chilling vision of the future", Language.ENGLISH),
                                new CreateBookRequest("The Catcher in the Rye", "Historical Fiction", AgeGroup.TEEN, new BigDecimal("14.50"), LocalDate.parse("1951-07-16"), "J.D. Salinger", 234, "Teenage rebellion", "A classic coming-of-age novel", Language.ENGLISH),
                                new CreateBookRequest("The Alchemist", "Adventure", AgeGroup.OTHER, new BigDecimal("19.95"), LocalDate.parse("1988-01-01"), "Paulo Coelho", 208, "Chasing dreams", "An inspiring philosophical tale", Language.ENGLISH),
                                new CreateBookRequest("The Girl with the Dragon Tattoo", "Mystery", AgeGroup.ADULT, new BigDecimal("26.00"), LocalDate.parse("2005-08-01"), "Stieg Larsson", 670, "Hacker and journalist", "A complex and gripping mystery", Language.ENGLISH),
                                new CreateBookRequest("The Fault in Our Stars", "Romance", AgeGroup.TEEN, new BigDecimal("17.99"), LocalDate.parse("2012-01-10"), "John Green", 313, "Star-crossed lovers", "A heartbreaking and beautiful story", Language.ENGLISH),
                                new CreateBookRequest("The Hunger Games", "Science Fiction", AgeGroup.TEEN, new BigDecimal("21.50"), LocalDate.parse("2008-09-14"), "Suzanne Collins", 374, "Fight for survival", "A thrilling dystopian competition", Language.ENGLISH),
                                new CreateBookRequest("The Road", "Science Fiction", AgeGroup.ADULT, new BigDecimal("23.00"), LocalDate.parse("2006-09-26"), "Cormac McCarthy", 287, "Post-apocalyptic survival", "A bleak and moving father-son journey", Language.ENGLISH),
                                new CreateBookRequest("The Kite Runner", "Historical Fiction", AgeGroup.ADULT, new BigDecimal("20.50"), LocalDate.parse("2003-05-29"), "Khaled Hosseini", 371, "Childhood friends", "A powerful story of redemption", Language.ENGLISH),
                                new CreateBookRequest("The Handmaid's Tale", "Science Fiction", AgeGroup.ADULT, new BigDecimal("22.99"), LocalDate.parse("1985-01-01"), "Margaret Atwood", 311, "Totalitarian society", "A terrifying dystopian vision", Language.ENGLISH),
                                new CreateBookRequest("The Shining", "Horror", AgeGroup.ADULT, new BigDecimal("25.00"), LocalDate.parse("1977-01-28"), "Stephen King", 447, "Haunted hotel", "A masterpiece of psychological horror", Language.ENGLISH),
                                new CreateBookRequest("The Great Gatsby", "Historical Fiction", AgeGroup.ADULT, new BigDecimal("15.99"), LocalDate.parse("1925-04-10"), "F. Scott Fitzgerald", 180, "Jazz Age decadence", "A tragic story of love and illusion", Language.ENGLISH),
                                new CreateBookRequest("Fahrenheit 451", "Science Fiction", AgeGroup.TEEN, new BigDecimal("18.50"), LocalDate.parse("1953-10-19"), "Ray Bradbury", 249, "Burning books", "A powerful dystopian novel", Language.ENGLISH),
                                new CreateBookRequest("The Lord of the Rings", "Fantasy", AgeGroup.ADULT, new BigDecimal("35.00"), LocalDate.parse("1954-07-29"), "J.R.R. Tolkien", 1178, "Destroying the ring", "The defining epic fantasy", Language.ENGLISH),
                                new CreateBookRequest("To Kill a Mockingbird", "Historical Fiction", AgeGroup.TEEN, new BigDecimal("19.00"), LocalDate.parse("1960-07-11"), "Harper Lee", 281, "Racial injustice", "A profound story of morality", Language.ENGLISH),
                                new CreateBookRequest("The Chronicles of Narnia", "Fantasy", AgeGroup.CHILD, new BigDecimal("29.99"), LocalDate.parse("1950-10-16"), "C.S. Lewis", 767, "Wardrobe portal", "A beloved magical adventure", Language.ENGLISH),
                                new CreateBookRequest("The Outsiders", "Historical Fiction", AgeGroup.TEEN, new BigDecimal("14.00"), LocalDate.parse("1967-04-24"), "S.E. Hinton", 192, "Rival teen gangs", "A classic story of youth and loyalty", Language.ENGLISH),
                                new CreateBookRequest("The Giver", "Science Fiction", AgeGroup.CHILD, new BigDecimal("16.50"), LocalDate.parse("1993-04-16"), "Lois Lowry", 240, "Utopian society", "A thought-provoking dystopian tale", Language.ENGLISH),
                                new CreateBookRequest("The Maze Runner", "Science Fiction", AgeGroup.TEEN, new BigDecimal("19.99"), LocalDate.parse("2009-10-06"), "James Dashner", 375, "Trapped in a maze", "A fast-paced survival thriller", Language.ENGLISH),
                                new CreateBookRequest("The Percy Jackson", "Fantasy", AgeGroup.CHILD, new BigDecimal("21.00"), LocalDate.parse("2005-06-28"), "Rick Riordan", 377, "Greek demigods", "A modern mythological adventure", Language.ENGLISH)
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