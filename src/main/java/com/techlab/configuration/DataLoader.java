package com.techlab.configuration;

import com.techlab.entity.*;
import com.techlab.repository.*;
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
                .password(passwordEncoder.encode("Admin12345-"))
                .active(true)
                .userRole(Role.ADMIN)
                .build();
        admin = userRepository.save(admin);

        User user1 = User.builder()
                .name("Juan Pérez")
                .email("juan@example.com")
                .password(passwordEncoder.encode("User12345-"))
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
        Category catLibros = categoryRepository.save(new Category("Libros"));
        Category catVaritas = categoryRepository.save(new Category("Varitas"));
        Category catAccesorios = categoryRepository.save(new Category("Accesorios"));
        Category catJuegos = categoryRepository.save(new Category("Juegos"));
        Category catImpresiones = categoryRepository.save(new Category("Impresiones 3D"));
        Category catJuguetes = categoryRepository.save(new Category("Juguetes"));

        log.info("Created 6 categories: {}, {}, {}, {}, {}, {}",
                catLibros.getName(), catVaritas.getName(), catAccesorios.getName(), catJuegos.getName(), catImpresiones.getName(), catJuguetes.getName());

        // ==================== PRODUCTS ====================
        Product product1 = productRepository
                .save(Product.builder()
                        .name("Harry Potter y la Piedra Filosofal")
                        .description("La historia comienza aquí. Edición tapa dura con ilustraciones originales.")
                        .price(17180f)
                        .category(catLibros)
                        .stock(50)
                        .imageUrl("https://harrypottershop.co.uk/cdn/shop/products/Philosophers_Stone_Paperback.png?v=1610099834&width=533")
                        .build());
        Product product2 = productRepository
                .save(Product.builder()
                        .name("Varita de Harry Potter")
                        .description("Madera de acebo, núcleo de pluma de fénix. Réplica exacta de 34cm.")
                        .price(61170f)
                        .category(catVaritas)
                        .stock(20)
                        .imageUrl("https://harrypottershop.co.uk/cdn/shop/products/256320_2.png?v=1756309038&width=533")
                        .build());
        Product product3 = productRepository
                .save(Product.builder()
                        .name("Bufanda de Gryffindor")
                        .description("100% lana, colores rojo y dorado. Ideal para el invierno en Hogwarts.")
                        .price(51520f)
                        .category(catAccesorios)
                        .stock(30)
                        .imageUrl("https://harrypottershop.co.uk/cdn/shop/products/1231750_1_grande_652a5c40-c4b1-4e24-ba55-fa5898a302d7.png?v=1679959806&width=533")
                        .build());
        Product product4 = productRepository
                .save(Product.builder()
                        .name("Juego de Mesa \"Monopoly Harry Potter\"")
                        .description("Edición especial con casillas mágicas y fichas temáticas.")
                        .price(72620f)
                        .category(catJuegos)
                        .stock(15)
                        .imageUrl("https://harrypottershop.co.uk/cdn/shop/files/Monopoly1.png?v=1726669087&width=533")
                        .build());
        Product product5 = productRepository
                .save(Product.builder()
                        .name("Lámpara de Las Reliquias de la Muerte")
                        .description("Impresión en PLA premium, luz LED cálida regulable.")
                        .price(45000f)
                        .category(catImpresiones)
                        .stock(10)
                        .imageUrl("https://http2.mlstatic.com/D_Q_NP_809812-MLA88882805916_082025-F.webp")
                        .build());
        Product product6 = productRepository
                .save(Product.builder()
                        .name("LEGO Hogwarts Castle - The Main Tower")
                        .description("Set coleccionista con detalles increíbles y mini-figuras exclusivas.")
                        .price(420550f)
                        .category(catJuegos)
                        .stock(5)
                        .imageUrl("https://harrypottershop.co.uk/cdn/shop/files/76454_1.png?v=1749024097&width=1100")
                        .build());
        Product product7 = productRepository
                .save(Product.builder()
                        .name("Varita de Hermione Granger")
                        .description("Madera de vid, núcleo de fibra de corazón de dragón.")
                        .price(61170f)
                        .category(catVaritas)
                        .stock(15)
                        .imageUrl("https://harrypottershop.co.uk/cdn/shop/products/Hermione2_Product.png?v=1756309130&width=533")
                        .build());
        Product product8 = productRepository
                .save(Product.builder()
                        .name("Set de Cartas \"Duelo de Magos\"")
                        .description("Juego de estrategia para 2 jugadores, incluye manual de hechizos.")
                        .price(34900f)
                        .category(catJuegos)
                        .stock(40)
                        .imageUrl("https://http2.mlstatic.com/D_Q_NP_697761-MLV31250707618_062019-F.webp")
                        .build());
        Product product9 = productRepository
                .save(Product.builder()
                        .name("Sombrero Seleccionador (Miniatura)")
                        .description("Impresión 3D detallada y pintada a mano con acabado envejecido.")
                        .price(9560f)
                        .category(catImpresiones)
                        .stock(12)
                        .imageUrl("https://harrypottershop.co.uk/cdn/shop/products/product_ornament_sortinghat_gryff.png?v=1670418257&width=533")
                        .build());
        Product product10 = productRepository
                .save(Product.builder()
                        .name("Harry Potter y la Cámara Secreta")
                        .description("Continuación de las aventuras en Hogwarts. Edición especial.")
                        .price(20630f)
                        .category(catLibros)
                        .stock(40)
                        .imageUrl("https://harrypottershop.co.uk/cdn/shop/products/Chamber_of_Secrets_Paperback.png?v=1610097244&width=533")
                        .build());
        Product product11 = productRepository
                .save(Product.builder()
                        .name("Anillo de Sorvolo Gaunt (Horrocrux)")
                        .description("Joyería temática en plata con piedra negra y grabado antiguo.")
                        .price(1560f)
                        .category(catAccesorios)
                        .stock(8)
                        .imageUrl("https://http2.mlstatic.com/D_763820-MLA85278168163_052025-O.jpg")
                        .build());
        Product product12 = productRepository
                .save(Product.builder()
                        .name("Mapa del Merodeador (Réplica)")
                        .description("Papel pergamino envejecido con tinta mágica y pliegues reales.")
                        .price(57350f)
                        .category(catAccesorios)
                        .stock(25)
                        .imageUrl("https://harrypottershop.co.uk/cdn/shop/files/67400005_Marauder_sMapReplica_A.png?v=1759332388&width=533")
                        .build());
        Product product13 = productRepository
                .save(Product.builder()
                        .name("Peluche de Hedwig")
                        .description("Búho nival ultra suave, tamaño mediano con detalles realistas.")
                        .price(76280f)
                        .category(catJuguetes)
                        .stock(18)
                        .imageUrl("https://harrypottershop.co.uk/cdn/shop/files/024214_edit.png?v=1751459053&width=533")
                        .build());
        Product product14 = productRepository
                .save(Product.builder()
                        .name("Soporte para Varitas")
                        .description("Diseño elegante en plástico negro mate para organizar tu colección.")
                        .price(18000f)
                        .category(catImpresiones)
                        .stock(20)
                        .imageUrl("https://http2.mlstatic.com/D_Q_NP_902004-MLA91296457891_082025-F.webp")
                        .build());
        Product product15 = productRepository
                .save(Product.builder()
                        .name("Harry Potter y el Prisionero de Azkaban")
                        .description("La historia se vuelve más oscura. Edición tapa dura.")
                        .price(20630f)
                        .category(catLibros)
                        .stock(30)
                        .imageUrl("https://harrypottershop.co.uk/cdn/shop/products/Prisoner_of_Azkaban1.png?v=1626166383&width=533")
                        .build());


        productRepository.saveAll(List.of(
                product1, product2, product3, product4, product5,
                product6, product7, product8, product9, product10,
                product11, product12, product13, product14, product15
        ));

        log.info("Created 15 products across all categories");

        // ==================== SHOPPING CART (optional) ====================
        ShoppingCart cart1 = new ShoppingCart();
        cart1.setUser(user1);
        cart1 = cartRepository.save(cart1);

        // Add items to Juan's cart before creating the order
        cart1.addItem(product1);
        cart1.addItem(product3);
        cart1.addItem(product12);
        cartRepository.save(cart1);

        log.info("Created shopping carts for users (juan's cart has 3 items)");

        // ==================== ORDERS ====================
        // Create a completed order for user1
        Order order1 = Order.orderFromShoppingCart(user1, cart1);
        order1.setPaymentStatus(PaymentStatus.PAID);
        orderRepository.save(order1);

        // Clear Juan's cart after creating the example order
        cart1.clearItems();
        cartRepository.save(cart1);

        log.info("Created sample order for user1 (cart cleared)");

        log.info("=================================================");
        log.info("           DATA LOADER COMPLETED                ");
        log.info("=================================================");
        log.info("                                                 ");
        log.info("  USUARIOS DE PRUEBA:                            ");
        log.info("  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━  ");
        log.info("  👑 ADMIN:                                      ");
        log.info("     email: admin@techlab.com                   ");
        log.info("     password: Admin12345-                          ");
        log.info("                                                 ");
        log.info("  👤 USER 1:                                     ");
        log.info("     email: juan@example.com                   ");
        log.info("     password: User12345-                          ");
        log.info("                                                 ");
        log.info("  👤 USER 2:                                     ");
        log.info("     email: maria@example.com                  ");
        log.info("     password: User12345-                          ");
        log.info("                                                 ");
        log.info("  📦 15 productos cargados en 6 categorías       ");
        log.info("  🛒 Carritos creados para usuarios              ");
        log.info("  📋 Pedido de ejemplo para juan                ");
        log.info("                                                 ");
        log.info("  🌐 Swagger UI: http://localhost:8080/swagger-ui");
        log.info("  📚 API Docs: http://localhost:8080/v3/api-docs ");
        log.info("=================================================");
    }
}