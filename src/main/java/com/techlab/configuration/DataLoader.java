package com.techlab.configuration;

import com.techlab.entity.*;
import com.techlab.repository.ICategoryRepository;
import com.techlab.repository.IOrderRepository;
import com.techlab.repository.IProductRepository;
import com.techlab.repository.IShoppingCartRepository;
import com.techlab.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final IUserRepository userRepository;
    private final ICategoryRepository categoryRepository;
    private final IProductRepository productRepository;
    private final IShoppingCartRepository cartRepository;
    private final IOrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Only load data if tables are empty
        if (userRepository.count() > 0) {
            log.info("Database already has data, skipping DataLoader");
            return;
        }

        log.info("Loading initial data...");

        // ==================== USERS ====================
        User admin = User.builder()
                .name("Admin")
                .email("admin@techlab.com")
                .password(passwordEncoder.encode("admin123"))
                .active(true)
                .userRole(Role.ADMIN)
                .build();
        admin = userRepository.save(admin);

        User user1 = User.builder()
                .name("Juan Pérez")
                .email("juan@example.com")
                .password(passwordEncoder.encode("user1234"))
                .active(true)
                .userRole(Role.USER)
                .build();
        user1 = userRepository.save(user1);

        User user2 = User.builder()
                .name("María García")
                .email("maria@example.com")
                .password(passwordEncoder.encode("user1234"))
                .active(true)
                .userRole(Role.USER)
                .build();
        user2 = userRepository.save(user2);

        log.info("Created users: admin (id={}, email={}), juan (id={}, email={}), maria (id={}, email={})", 
                admin.getId(), admin.getEmail(),
                user1.getId(), user1.getEmail(),
                user2.getId(), user2.getEmail());

        // ==================== CATEGORIES ====================
        Category electronica = categoryRepository.save(new Category("Electrónica"));
        Category ropa = categoryRepository.save(new Category("Ropa"));
        Category hogar = categoryRepository.save(new Category("Hogar"));
        Category deportes = categoryRepository.save(new Category("Deportes"));
        Category libros = categoryRepository.save(new Category("Libros"));

        log.info("Created categories: {}, {}, {}, {}, {}", 
                electronica.getName(), ropa.getName(), hogar.getName(), 
                deportes.getName(), libros.getName());

        // ==================== PRODUCTS ====================
        // Electrónica
        Product product1 = Product.builder()
                .name("Laptop Gaming Pro")
                .description("Laptop de alta gama para gaming con RTX 4080, 32GB RAM, 1TB SSD")
                .price(2499999f)
                .category(electronica)
                .stock(15)
                .imageUrl("https://http2.mlstatic.com/D_NQ_NP_XXXXX-1.webp")
                .build();

        Product product2 = Product.builder()
                .name("Smartphone Galaxy S24 Ultra")
                .description("Samsung Galaxy S24 Ultra con 256GB, cámara 200MP, pantalla AMOLED")
                .price(1899999f)
                .category(electronica)
                .stock(25)
                .imageUrl("https://http2.mlstatic.com/D_NQ_NP_XXXXX-2.webp")
                .build();

        Product product3 = Product.builder()
                .name("Auriculares Sony WH-1000XM5")
                .description("Auriculares wireless con cancelación de ruido premium")
                .price(449999f)
                .category(electronica)
                .stock(30)
                .imageUrl("https://http2.mlstatic.com/D_NQ_NP_XXXXX-3.webp")
                .build();

        Product product4 = Product.builder()
                .name("Smartwatch Apple Watch Series 9")
                .description("Apple Watch con GPS, pantalla Always-On, sensor de salud")
                .price(749999f)
                .category(electronica)
                .stock(20)
                .imageUrl("https://http2.mlstatic.com/D_NQ_NP_XXXXX-4.webp")
                .build();

        // Ropa
        Product product5 = Product.builder()
                .name("Campera Adidas Invincible")
                .description("Campera deportiva impermeable, ideal para running y entrenamiento")
                .price(189999f)
                .category(ropa)
                .stock(50)
                .imageUrl("https://http2.mlstatic.com/D_NQ_NP_XXXXX-5.webp")
                .build();

        Product product6 = Product.builder()
                .name("Zapatillas Nike Air Max")
                .description("Zapatillas urbanas con tecnología Air Max, comodidad garantizada")
                .price(249999f)
                .category(ropa)
                .stock(40)
                .imageUrl("https://http2.mlstatic.com/D_NQ_NP_XXXXX-6.webp")
                .build();

        // Hogar
        Product product7 = Product.builder()
                .name("Asppiradora Dyson V15")
                .description("Aspiradora inalámbrica con detección de polvo, autonomía 60min")
                .price(1299999f)
                .category(hogar)
                .stock(12)
                .imageUrl("https://http2.mlstatic.com/D_NQ_NP_XXXXX-7.webp")
                .build();

        Product product8 = Product.builder()
                .name("Juego de Sábanas 400 TC")
                .description("Juego de sábanas premium algodon egipcio, 400 hilos")
                .price(89999f)
                .category(hogar)
                .stock(35)
                .imageUrl("https://http2.mlstatic.com/D_NQ_NP_XXXXX-8.webp")
                .build();

        // Deportes
        Product product9 = Product.builder()
                .name("Bicicleta Mountain Bike Specialized")
                .description("Bicicleta MTB cuadro aluminio, suspensiones Fox, 27 velocidades")
                .price(1599999f)
                .category(deportes)
                .stock(8)
                .imageUrl("https://http2.mlstatic.com/D_NQ_NP_XXXXX-9.webp")
                .build();

        Product product10 = Product.builder()
                .name("Set de Pesas Rusas 5-20kg")
                .description("Set de 6 pesas俄罗斯as para entrenamiento en casa")
                .price(179999f)
                .category(deportes)
                .stock(22)
                .imageUrl("https://http2.mlstatic.com/D_NQ_NP_XXXXX-10.webp")
                .build();

        // Libros
        Product product11 = Product.builder()
                .name("El Principito - Antoine de Saint-Exupéry")
                .description("Clásico de la literatura francesa, edición hardcover")
                .price(24999f)
                .category(libros)
                .stock(100)
                .imageUrl("https://http2.mlstatic.com/D_NQ_NP_XXXXX-11.webp")
                .build();

        Product product12 = Product.builder()
                .name("Clean Code - Robert C. Martin")
                .description("Guía para escribir código limpio y mantenible")
                .price(89999f)
                .category(libros)
                .stock(45)
                .imageUrl("https://http2.mlstatic.com/D_NQ_NP_XXXXX-12.webp")
                .build();

        productRepository.saveAll(List.of(
                product1, product2, product3, product4, product5,
                product6, product7, product8, product9, product10,
                product11, product12
        ));

        log.info("Created 12 products across all categories");

        // ==================== SHOPPING CART (optional) ====================
        ShoppingCart cart1 = new ShoppingCart();
        cart1.setUserId(user1.getId());
        cart1 = cartRepository.save(cart1);

        ShoppingCart cart2 = new ShoppingCart();
        cart2.setUserId(user2.getId());
        cart2 = cartRepository.save(cart2);

        log.info("Created shopping carts for users");

        // ==================== ORDERS (optional) ====================
        // Create a completed order for user1
        Order order1 = Order.orderFromShoppingCart(user1, cart1);
        order1.setPaymentStatus(PaymentStatus.PAID);
        orderRepository.save(order1);

        log.info("Created sample order for user1");

        log.info("=================================================");
        log.info("           DATA LOADER COMPLETED                ");
        log.info("=================================================");
        log.info("                                                 ");
        log.info("  USUARIOS DE PRUEBA:                            ");
        log.info("  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━  ");
        log.info("  👑 ADMIN:                                      ");
        log.info("     email: admin@techlab.com                   ");
        log.info("     password: admin123                          ");
        log.info("                                                 ");
        log.info("  👤 USER 1:                                     ");
        log.info("     email: juan@example.com                   ");
        log.info("     password: user1234                          ");
        log.info("                                                 ");
        log.info("  👤 USER 2:                                     ");
        log.info("     email: maria@example.com                  ");
        log.info("     password: user1234                          ");
        log.info("                                                 ");
        log.info("  📦 12 productos cargados en 5 categorías       ");
        log.info("  🛒 Carritos creados para usuarios              ");
        log.info("  📋 Pedido de ejemplo para juan                ");
        log.info("                                                 ");
        log.info("  🌐 Swagger UI: http://localhost:8080/swagger-ui");
        log.info("  📚 API Docs: http://localhost:8080/v3/api-docs ");
        log.info("=================================================");
    }
}