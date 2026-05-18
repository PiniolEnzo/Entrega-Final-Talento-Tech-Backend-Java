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

        log.info("Created categories: {}, {}, {}, {}, {}, {}",
                catLibros.getName(), catVaritas.getName(), catAccesorios.getName(), catJuegos.getName(), catImpresiones.getName(), catJuguetes.getName());

        // ==================== PRODUCTS ====================
        Product product1 = productRepository.save(Product.builder().name("Libro: Harry Potter y la Piedra Filosofal").description("La historia comienza aquí. Edición tapa dura con ilustraciones originales.").price(25.00).category(catLibros).stock(50).imageUrl("https://m.media-amazon.com/images/I/81mOclS-sKL.jpg").build());
        Product product2 = productRepository.save(Product.builder().name("Varita de Harry Potter").description("Madera de acebo, núcleo de pluma de fénix. Réplica exacta de 34cm.").price(45.00).category(catVaritas).stock(20).imageUrl("https://m.media-amazon.com/images/I/61Z2C6K7SXL.jpg").build());
        Product product3 = productRepository.save(Product.builder().name("Bufanda de Gryffindor").description("100% lana, colores rojo y dorado. Ideal para el invierno en Hogwarts.").price(30.00).category(catAccesorios).stock(30).imageUrl("https://m.media-amazon.com/images/I/61p58z6i9+L.jpg").build());
        Product product4 = productRepository.save(Product.builder().name("Juego de Mesa: Monopoly Harry Potter").description("Edición especial con casillas mágicas y fichas temáticas.").price(55.00).category(catJuegos).stock(15).imageUrl("https://m.media-amazon.com/images/I/81XzR-9XGkL.jpg").build());
        Product product5 = productRepository.save(Product.builder().name("Lámpara 3D: Las Reliquias de la Muerte").description("Impresión en PLA premium, luz LED cálida regulable.").price(35.00).category(catImpresiones).stock(10).imageUrl("https://m.media-amazon.com/images/I/61-sD-s6Y6L.jpg").build());
        Product product6 = productRepository.save(Product.builder().name("LEGO Hogwarts Castle").description("Set coleccionista con detalles increíbles y mini-figuras exclusivas.").price(450.00).category(catJuegos).stock(5).imageUrl("https://m.media-amazon.com/images/I/81C-S-S6Y6L.jpg").build());
        Product product7 = productRepository.save(Product.builder().name("Varita de Hermione Granger").description("Madera de vid, núcleo de fibra de corazón de dragón.").price(45.00).category(catVaritas).stock(15).imageUrl("https://m.media-amazon.com/images/I/61Y-S-S6Y6L.jpg").build());
        Product product8 = productRepository.save(Product.builder().name("Set de Cartas: Duelo de Magos").description("Juego de estrategia para 2 jugadores, incluye manual de hechizos.").price(20.00).category(catJuegos).stock(40).imageUrl("https://m.media-amazon.com/images/I/61Z-S-S6Y6L.jpg").build());
        Product product9 = productRepository.save(Product.builder().name("Sombrero Seleccionador (Miniatura)").description("Impresión 3D detallada y pintada a mano con acabado envejecido.").price(25.00).category(catImpresiones).stock(12).imageUrl("https://m.media-amazon.com/images/I/61A-S-S6Y6L.jpg").build());
        Product product10 = productRepository.save(Product.builder().name("Libro: Harry Potter y la Cámara Secreta").description("Continuación de las aventuras en Hogwarts. Edición especial.").price(25.00).category(catLibros).stock(40).imageUrl("https://m.media-amazon.com/images/I/81X-S-S6Y6L.jpg").build());
        Product product11 = productRepository.save(Product.builder().name("Anillo de la Familia Black").description("Joyería temática en plata con piedra negra y grabado antiguo.").price(60.00).category(catAccesorios).stock(8).imageUrl("https://m.media-amazon.com/images/I/61B-S-S6Y6L.jpg").build());
        Product product12 = productRepository.save(Product.builder().name("Mapa del Merodeador (Réplica)").description("Papel pergamino envejecido con tinta mágica y pliegues reales.").price(35.00).category(catAccesorios).stock(25).imageUrl("https://m.media-amazon.com/images/I/61C-S-S6Y6L.jpg").build());
        Product product13 = productRepository.save(Product.builder().name("Peluche de Hedwig").description("Búho nival ultra suave, tamaño mediano con detalles realistas.").price(28.00).category(catJuguetes).stock(18).imageUrl("https://m.media-amazon.com/images/I/61D-S-S6Y6L.jpg").build());
        Product product14 = productRepository.save(Product.builder().name("Soporte 3D para Varitas").description("Diseño elegante en plástico negro mate para organizar tu colección.").price(15.00).category(catImpresiones).stock(20).imageUrl("https://m.media-amazon.com/images/I/61E-S-S6Y6L.jpg").build());
        Product product15 = productRepository.save(Product.builder().name("Libro: Harry Potter y el Prisionero de Azkaban").description("La historia se vuelve más oscura. Edición tapa dura.").price(25.00).category(catLibros).stock(30).imageUrl("https://m.media-amazon.com/images/I/81Y-S-S6Y6L.jpg").build());
        

        productRepository.saveAll(List.of(
                product1, product2, product3, product4, product5,
                product6, product7, product8, product9, product10,
                product11, product12, product13, product14, product15
        ));

        log.info("Created 12 products across all categories");

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