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
                .password(passwordEncoder.encode("Admin1234-"))
                .active(true)
                .userRole(Role.ADMIN)
                .build();
        admin = userRepository.save(admin);

        User user1 = User.builder()
                .name("Juan Pérez")
                .email("juan@example.com")
                .password(passwordEncoder.encode("User1234."))
                .active(true)
                .userRole(Role.USER)
                .build();
        user1 = userRepository.save(user1);

        User user2 = User.builder()
                .name("María García")
                .email("maria@example.com")
                .password(passwordEncoder.encode("User12345-"))
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
                .name("Notebook ASUS ROG Strix SCAR 18\" Intel Core Ultra 9 275HX 32GB 2TB RTX 5080")
                .description("Notebook ASUS ROG Strix SCAR 18 con Intel " +
                        "Core Ultra 9 275HX, 32GB RAM DDR5 y almacenamiento " +
                        "SSD NVMe de 2TB. Equipada con GPU NVIDIA GeForce RTX 5080" +
                        " de 16GB. Pantalla de 18 pulgadas 2.5K (2560x1600) con 240Hz, " +
                        "ideal para gaming competitivo. Conectividad avanzada con WiFi BE, " +
                        "Bluetooth y 2 puertos Thunderbolt 5, además de Ethernet. " +
                        "Incluye 3 USB 3.2 tipo A y HDMI. Diseño negro con teclado RGB, webcam " +
                        "y teclado numérico. Windows 11 Home preinstalado.")
                .price(8108400f)
                .category(electronica)
                .stock(150)
                .imageUrl("https://imagenes.compragamer.com/productos/compragamer_Imganen_general_49443_Notebook_" +
                        "ASUS_ROG_Strix_SCAR_18_18__Intel_Core_Ultra_9_275HX_32GB_DDR5_SSD_2TB_RTX_5080_2.5K_240Hz_" +
                        "Win11_G835LW-SA024W_ffb8ba36-grn.jpg")
                .build();

        Product product2 = Product.builder()
                .name("Smartphone Samsung Galaxy S24 Ultra")
                .description("Samsung Galaxy S24 Ultra con 256GB, cámara 200MP, pantalla AMOLED")
                .price(2155990f)
                .category(electronica)
                .stock(25)
                .imageUrl("https://http2.mlstatic.com/D_NQ_NP_963419-MLA77739568521_072024-O.webp")
                .build();

        Product product3 = Product.builder()
                .name("Auriculares Sony WH-1000XM5")
                .description("Auriculares wireless con cancelación de ruido premium")
                .price(690525f)
                .category(electronica)
                .stock(30)
                .imageUrl("https://http2.mlstatic.com/D_NQ_NP_719976-MLA99500633372_112025-O.webp")
                .build();

        Product product4 = Product.builder()
                .name("Smartwatch Apple Watch Series 9")
                .description("Apple Watch con GPS, pantalla Always-On, sensor de salud")
                .price(840000f)
                .category(electronica)
                .stock(20)
                .imageUrl("https://http2.mlstatic.com/D_Q_NP_782085-MLA80127450634_112024-F.webp")
                .build();

        // Ropa
        Product product5 = Product.builder()
                .name("Campera Adidas Casual ZNE")
                .description("Campera adidas ZNE modelo DVJ18 para mujer, ideal para uso lifestyle en " +
                        "temporada Primavera/Verano 2026. Diseño en color blanco con estilo deportivo y moderno. " +
                        "Confeccionada con materiales reciclados, combinando comodidad y compromiso sustentable. " +
                        "Cuenta con 2 bolsillos externos para mayor practicidad. No es impermeable ni oversize, y no " +
                        "incluye capucha. Prenda liviana y versátil para uso diario y urbano.")
                .price(159999f)
                .category(ropa)
                .stock(50)
                .imageUrl("https://http2.mlstatic.com/D_Q_NP_767648-MLA100267310613_122025-F.webp")
                .build();

        Product product6 = Product.builder()
                .name("Zapatillas Hombre Nike Air Max Excee Moda Negro Fn7304-001")
                .description("Zapatillas Nike Air Max Excee FN7304-001 para hombre, estilo urbano. Inspiradas en el " +
                        "Air Max 90, con diseño moderno y líneas alargadas. Incorporan unidad Max Air visible para " +
                        "amortiguación duradera y entresuela de espuma suave. Cuello acolchado para mayor confort y " +
                        "suela de goma que brinda tracción y resistencia. Ideales para uso diario con estilo " +
                        "y comodidad.")
                .price(229999f)
                .category(ropa)
                .stock(40)
                .imageUrl("https://http2.mlstatic.com/D_Q_NP_935782-MLA81561875170_012025-F.webp")
                .build();

        // Hogar
        Product product7 = Product.builder()
                .name("Asppiradora Dyson V15")
                .description("Aspiradora inalámbrica con detección de polvo, autonomía 60min")
                .price(1299999f)
                .category(hogar)
                .stock(12)
                .imageUrl("https://http2.mlstatic.com/D_Q_NP_903403-MLA107458481843_022026-F.webp")
                .build();

        Product product8 = Product.builder()
                .name("Juego de Sábanas 400 TC")
                .description("Juego de sábanas premium algodon egipcio, 400 hilos")
                .price(103230f)
                .category(hogar)
                .stock(35)
                .imageUrl("https://http2.mlstatic.com/D_Q_NP_868292-MLA107590107202_032026-F.webp")
                .build();

        // Deportes
        Product product9 = Product.builder()
                .name("Bicicleta Mountain Bike Specialized")
                .description("Bicicleta MTB cuadro aluminio, suspensiones Fox, 27 velocidades")
                .price(1599999f)
                .category(deportes)
                .stock(8)
                .imageUrl("https://encrypted-tbn0.gstatic.com/shopping?q=tbn:ANd9GcSfsgQTYYm2tvP6IL7W7hZ3mc-" +
                        "lcvDLpgwRDTzFMuLNY2KK9xy6WXCpAcUyGXCfiylr3D7t66gSBfDGjMsyBzWHqSxJJ1meTg")
                .build();

        Product product10 = Product.builder()
                .name("Set de Pesas Rusas 21kg")
                .description("Set de 6 pesas rusas para entrenamiento en casa")
                .price(179999f)
                .category(deportes)
                .stock(22)
                .imageUrl("https://http2.mlstatic.com/D_Q_NP_2X_698002-MLA89101613736_082025-P.webp")
                .build();

        // Libros
        Product product11 = Product.builder()
                .name("El Principito - Antoine de Saint-Exupéry")
                .description("Clásico de la literatura francesa, edición hardcover")
                .price(24999f)
                .category(libros)
                .stock(100)
                .imageUrl("https://tienda.planetadelibros.com.ar/cdn/shop/products/portada_el-principito_antoine-de-" +
                        "saint-exupery_201507152131.jpg?v=1684356025")
                .build();

        Product product12 = Product.builder()
                .name("Clean Code - Robert C. Martin")
                .description("Guía para escribir código limpio y mantenible")
                .price(89999f)
                .category(libros)
                .stock(45)
                .imageUrl("https://images.cdn1.buscalibre.com/fit-in/360x360/87/da/87da3d378f0336fd04014c4ea153d064" +
                        ".jpg")
                .build();

        productRepository.saveAll(List.of(
                product1, product2, product3, product4, product5,
                product6, product7, product8, product9, product10,
                product11, product12
        ));

        log.info("Created 12 products across all categories");

        // ==================== SHOPPING CART (optional) ====================
        ShoppingCart cart1 = new ShoppingCart();
        cart1.setUser(user1);
        cart1 = cartRepository.save(cart1);

        // Add items to Juan's cart before creating the order
        cart1.addItem(product1);  // Laptop Gaming Pro
        cart1.addItem(product3);  // Auriculares Sony WH-1000XM5
        cart1.addItem(product12); // Clean Code - Robert C. Martin
        cartRepository.save(cart1);

        ShoppingCart cart2 = new ShoppingCart();
        cart2.setUser(user2);
        cart2 = cartRepository.save(cart2);

        log.info("Created shopping carts for users (juan's cart has 3 items)");

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