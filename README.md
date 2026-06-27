# TechLab API

> **Backend E-commerce REST API** - Proyecto final del curso "Buenos Aires Aprende - TalentoTech"  
> Java 21 + Spring Boot 3.5.3 + JWT + JPA + H2/PostgreSQL

---

## Tabla de Contenidos

1. [Descripción general](#descripcion-general)
2. [Stack tecnológico](#stack-tecnologico)
3. [Arquitectura](#arquitectura)
4. [Estructura del proyecto](#estructura-del-proyecto)
5. [Modelo de datos](#modelo-de-datos)
6. [Seguridad](#seguridad)
7. [API endpoints](#api-endpoints)
8. [Cómo levantar el proyecto](#como-levantar-el-proyecto)
9. [Variables de entorno](#variables-de-entorno)
10. [Docker](#docker)
11. [Testing](#testing)
12. [Frontend](#frontend)
13. [Roadmap / Mejoras futuras](#roadmap--mejoras-futuras)

---

## Descripción general

TechLab es una API RESTful para un e-commerce genérico. Permite:

- **Registro y autenticación** de usuarios con JWT
- **Catálogo de productos** con filtrado por categoría
- **Carrito de compras** (uno por usuario)
- **Checkout** con validación de stock transaccional
- **Órdenes de compra** con seguimiento de estado
- **Recuperación de contraseña** vía email
- **Panel administrativo** para CRUD completo de productos, categorías, usuarios y órdenes

Está pensada como backend para un frontend SPA (React, Angular, etc.) con arquitectura stateless.

---

## Stack tecnológico

| Componente          | Tecnología                                               |
|---------------------|----------------------------------------------------------|
| Lenguaje            | Java 21                                                  |
| Framework           | Spring Boot 3.5.3                                        |
| Seguridad           | Spring Security + JWT (jjwt 0.11.5, HMAC-SHA)            |
| Base de datos       | H2 (file-based, dev) / PostgreSQL                        |
| ORM                 | Spring Data JPA + Hibernate (`ddl-auto: update`)         |
| Validación          | Hibernate Validator + Jakarta Validation                 |
| Email               | Spring Boot Mail Starter (SMTP)                          |
| Documentación API   | SpringDoc OpenAPI 2.8.9 (Swagger UI)                     |
| Build               | Maven + Wrapper (`mvnw`)                                 |
| Container           | Docker (multi-stage, Eclipse Temurin 21 JRE)             |
| Testing             | JUnit 5 + Spring Boot Test + Spring Security Test        |

---

## Arquitectura

### Capas

```
Controller → Service (interface) → ServiceImpl → Repository (JPA) → DB
                    ↓
                Mapper (MapStruct)
                    ↓
                  DTO (Request/Response)
```

- **Controller**: expone endpoints REST, recibe requests DTO, delega en servicios
- **Service**: interfaces que definen el contrato de negocio
- **ServiceImpl**: implementación con lógica de negocio, validaciones, transacciones
- **Repository**: Spring Data JPA para acceso a datos
- **Mapper**: MapStruct para conversión Entity ↔ DTO
- **DTO**: objetos de transferencia separados por tipo (request vs response)

### Patrones aplicados

- **Container-Presentational**: separación entre lógica (service) y exposición (controller)
- **DTO Pattern**: los entities nunca se exponen directamente en la API
- **Repository Pattern**: abstracción de persistencia vía interfaces JPA
- **Strategy**: servicios con interfaces intercambiables
- **Factory Method**: `Order.orderFromShoppingCart()` para crear órdenes desde el carrito
- **Global Exception Handler**: `@RestControllerAdvice` para manejo centralizado de errores

### Flujo de checkout transaccional

```
POST /orders/checkout/{cartId}
  │
  ├── 1. Validar ownership del carrito
  ├── 2. Validar que el carrito no esté vacío
  ├── 3. Por cada item: validar stock ≥ cantidad
  ├── 4. Reducir stock de cada producto
  ├── 5. Crear Order desde el carrito (congela precios)
  ├── 6. Limpiar el carrito
  └── 7. Si algo falla → ROLLBACK completo (@Transactional)
```

---

## Estructura del proyecto

```
TalentoTech/
├── .example.env             # Template para las variables de entorno
├── .gitignore
├── dockerfile               # Build multi-stage
├── mvnw / mvnw.cmd          # Maven Wrapper (sin necesidad de Maven instalado)
├── pom.xml                  # Dependencias y configuración del build
│
├── data/
│   └── techlab.mv.db        # Base de datos H2 file-based (generada al correr)
│
└── src/
    ├── main/
    │   ├── java/com/techlab/
    │   │   ├── TechLabApplication.java          # Entry point (@SpringBootApplication)
    │   │   │
    │   │   ├── configuration/
    │   │   │   ├── AplicationConfig.java        # Beans globales (PasswordEncoder, AuthManager)
    │   │   │   ├── SecurityConfig.java          # Security filter chain, requestMatchers
    │   │   │   ├── CorsGlobalConfig.java        # CORS global configuration
    │   │   │   ├── JwtAuthenticationFilter.java # Filtro JWT (cada request)
    │   │   │   ├── DataLoader.java              # Seed data (usuarios, productos, órdenes)
    │   │   │   └── MailConfig.java              # Configuración SMTP
    │   │   │
    │   │   ├── controller/
    │   │   │   ├── AuthController.java          # /auth/**
    │   │   │   ├── ProductController.java       # /products/**
    │   │   │   ├── CategoryController.java      # /categories/**
    │   │   │   ├── CartController.java          # /carts/**
    │   │   │   ├── OrderController.java         # /orders/**
    │   │   │   └── UserController.java          # /users/**
    │   │   │
    │   │   ├── service/
    │   │   │   ├── IAuthService.java
    │   │   │   ├── IUserService.java
    │   │   │   ├── IProductService.java
    │   │   │   ├── ICategoryService.java
    │   │   │   ├── IShoppingCartService.java
    │   │   │   ├── IOrderService.java
    │   │   │   ├── IJwtService.java
    │   │   │   ├── IEmailService.java
    │   │   │   ├── ILogoutService.java
    │   │   │   └── implementation/
    │   │   │       ├── AuthServiceImpl.java
    │   │   │       ├── UserServiceImpl.java
    │   │   │       ├── ProductServiceImpl.java
    │   │   │       ├── CategoryServiceImpl.java
    │   │   │       ├── ShoppingCartServiceImpl.java
    │   │   │       ├── OrderServiceImpl.java
    │   │   │       ├── JwtServiceImpl.java
    │   │   │       ├── EmailService.java
    │   │   │       └── LogoutServiceImpl.java
    │   │   │
    │   │   ├── repository/
    │   │   │   ├── IUserRepository.java
    │   │   │   ├── IProductRepository.java
    │   │   │   ├── ICategoryRepository.java
    │   │   │   ├── IShoppingCartRepository.java
    │   │   │   ├── IOrderRepository.java
    │   │   │   └── IPasswordResetTokenRepository.java
    │   │   │
    │   │   ├── entity/
    │   │   │   ├── User.java
    │   │   │   ├── Role.java (enum: USER, ADMIN)
    │   │   │   ├── Product.java
    │   │   │   ├── Category.java
    │   │   │   ├── ShoppingCart.java
    │   │   │   ├── CartItem.java
    │   │   │   ├── Order.java
    │   │   │   ├── OrderLine.java
    │   │   │   ├── PaymentStatus.java (enum: PENDING, PAID, PROCESSING, CANCELED, FAILED, REFUNDED)
    │   │   │   └── PasswordChangeToken.java
    │   │   │
    │   │   ├── dto/
    │   │   │   ├── auth/
    │   │   │   │   ├── AuthResponse.java
    │   │   │   │   ├── LoginRequest.java
    │   │   │   │   ├── ForgotPasswordRequest.java
    │   │   │   │   └── ResetPasswordRequest.java
    │   │   │   ├── user/
    │   │   │   │   ├── RegisterRequest.java
    │   │   │   │   ├── ChangePassword.java
    │   │   │   │   ├── UpdateUser.java
    │   │   │   │   ├── UserDto.java
    │   │   │   │   ├── UserResponse.java
    │   │   │   │   └── UserProfileResponse.java
    │   │   │   ├── product/
    │   │   │   │   ├── ProductRequest.java
    │   │   │   │   ├── ProductResponse.java
    │   │   │   │   └── ProductDto.java 
    │   │   │   ├── category/
    │   │   │   │   ├── CategoryRequest.java
    │   │   │   │   └── CategoryResponse.java
    │   │   │   ├── shoppingCart/
    │   │   │   │   ├── AddItemRequest.java
    │   │   │   │   ├── UpdateItemRequest.java
    │   │   │   │   ├── CartResponse.java
    │   │   │   │   └── CartItemResponse.java
    │   │   │   └── order/
    │   │   │       ├── OrderResponse.java
    │   │   │       └── OrderLineResponse.java
    │   │   │
    │   │   ├── mapper/
    │   │   │   ├── ProductMapper.java
    │   │   │   ├── CategoryMapper.java
    │   │   │   ├── UserMapper.java
    │   │   │   ├── ShoppingCartMapper.java
    │   │   │   ├── CartItemMapper.java
    │   │   │   ├── OrderMapper.java
    │   │   │   └── OrderLineMapper.java
    │   │   │
    │   │   ├── exception/
    │   │   │   ├── GlobalExceptionHandler.java  # @RestControllerAdvice central
    │   │   │   ├── DuplicateUserException.java
    │   │   │   ├── DuplicateCategory.java
    │   │   │   ├── UserNotFoundException.java
    │   │   │   ├── ProductNotFoundException.java
    │   │   │   ├── CategoryNotFoundException.java
    │   │   │   ├── CartNotFoundException.java
    │   │   │   ├── CartEmptyException.java
    │   │   │   ├── OrderNotFoundException.java
    │   │   │   ├── InsufficientStockException.java
    │   │   │   └── InvalidPaymentStatusException.java
    │   │   │
    │   │   └── utils/
    │   │       └── UserAccessValidate.java      # Validación de ownership
    │   │
    │   └── resources/
    │       ├── application.yml                  # Config principal (DB, JPA, JWT, mail, server)
    │       ├── static/                          # Contenido estático (vacío)
    │       └── templates/                       # Templates (vacío, email es programático)
    │
    └── test/java/com/techlab/
        ├── TechLabApplicationTests.java         
        └── entity/                              # Unit tests las entidades
            ├── ShoppingCartTest.java            
            ├── CartItemTest.java
            ├── ProductTest.java
            ├── OrderTest.java
            ├── OrderLineTest.java
            ├── CategoryTest.java
            ├── RoleTest.java
            └── PaymentStatusTest.java

```

---

## Modelo de datos

```
  ┌───────────────┐       ┌──────────────────┐         ┌──────────────────┐
  │    User       │       │  ShoppingCart    │         │    CartItem      │
  ├───────────────┤       ├──────────────────┤         ├──────────────────┤
  │ id (PK)       │──1:1──│ id (PK)          │───1:N───│ id (PK)          │
  │ name          │       │ user_id (FK,UQ)  │         │ cart_id (FK)     │
  │ email (UQ)    │       │ created_at       │         │ product_id (FK)  │
  │ password      │       │ updated_at       │         │ quantity (int)   │
  │ active        │       └──────────────────┘         │ created_at       │
  │ user_role     │                                    │ updated_at       │
  │ created_at    │                                    └───────────┬──────┘
  │ updated_at    │                                                │
  └─┬───────────┬─┘                                            ┌───┴──────────────┐
    │           │                                              │     Product      │
    │           │ 1:N                                          ├──────────────────┤
    │           │                                        ┌─────│ id (PK)          │
    │           │                                        │     │ name             │
    │       ┌───┴─────────┐        ┌────────────────┐    │     │ description      │
    │       │    Order    │──1:N───│  OrderLine     │    │     │ price (Float)    │
    │       ├─────────────┤        ├────────────────┤    │     │ stock (Integer)  │
    │       │ id (PK)     │        │ id (PK)        │    │     │ image_url        │
    │       │ user_id (FK)│        │ order_id (FK)  │    │     │ category_id (FK) │
    │       │ payment_    │        │ product_id(FK) │────┘     │ created_at       │
    │       │  status     │        │ unit_price     │          │ updated_at       │
1:N │       │ total (Float)        │ quantity       │          └────────┬─────────┘
    │       │ created_at  │        │ subtotal       │                   │
    │       │ updated_at  │        │ created_at     │             N:1   │
    │       └─────────────┘        │ updated_at     │                   │
    │                              └────────────────┘          ┌────────┴─────────┐
    │                                                          │    Category      │
    │                                                          ├──────────────────┤
    │                                                          │ id (PK, Short)   │
    │                                                          │ name             │
    │                                                          │ created_at       │
    │                                                          │ updated_at       │
    │                                                          └──────────────────┘
┌───┴──────────────────┐
│  PasswordChangeToken │
├──────────────────────┤
│ id (PK)              │
│ token (SHA-256, UQ)  │
│ user_id (FK)         │
│ expiration_date      │
│ used (boolean)       │
│ created_at           │
│ updated_at           │
└──────────────────────┘
```

### Relaciones clave

| Entidad origen   | Relación | Entidad destino    | Notas                                          |
|------------------|----------|--------------------|------------------------------------------------|
| User             | 1:1      | ShoppingCart       | Un carrito por usuario (`UNIQUE` en user_id)   |
| ShoppingCart     | 1:N      | CartItem           | Cascade MERGE, orphanRemoval, EAGER            |
| CartItem         | N:1      | Product            | -                                              |
| Order            | N:1      | User               | -                                              |
| Order            | 1:N      | OrderLine          | Cascade PERSIST + REMOVE                       |
| OrderLine        | N:1      | Product            | `unitPrice` congelado al momento de la orden   |
| Product          | N:1      | Category           | EAGER, nullable                                |
| User             | 1:N      | PasswordChangeToken| Tokens de reseteo de contraseña                |

---

## Seguridad

### Autenticación JWT

1. El cliente envía `POST /auth/login` con email + password
2. El servidor valida credenciales y devuelve un JWT firmado con HMAC-SHA
3. El cliente incluye el token en cada request: `Authorization: Bearer <token>`
4. `JwtAuthenticationFilter` valida el token en cada request
5. Al hacer logout, el token se agrega a una blacklist en memoria (se pierde al reiniciar)

### Roles

| Rol     | Authority    | Acceso                                                   |
|---------|--------------|----------------------------------------------------------|
| `USER`  | `ROLE_USER`  | Catálogo, carrito propio, órdenes propias, perfil        |
| `ADMIN` | `ROLE_ADMIN` | CRUD completo + Swagger UI                               |

### Reglas de acceso por endpoint

```
/auth/**       → POST register, login, forgot-password, reset-password, validate: PUBLIC
                 POST logout, change-password, GET me: USER/ADMIN
/carts/**      → USER (solo carrito propio)
/categories/** → GET: PUBLIC, POST/PUT/DELETE: ADMIN
/orders/**     → GET my-orders, POST checkout: USER; resto: ADMIN
/products/**   → GET: PUBLIC, POST/PUT/DELETE: ADMIN
/users/**      → GET, DELETE: ADMIN; PUT: USER/ADMIN (con validación de ownership)
/swagger-ui/** → ADMIN
```

### Headers de seguridad

La API incluye protección via headers HTTP:
- **CSP** (Content-Security-Policy)
- **HSTS** (HTTP Strict-Transport-Security)
- **Permissions-Policy**
- **Referrer-Policy**

---

## API endpoints

| Método | Endpoint                          | Auth     | Descripción                        |
|--------|-----------------------------------|----------|------------------------------------|
| POST   | `/auth/register`                  | Pública  | Registrar nuevo usuario            |
| POST   | `/auth/login`                     | Pública  | Iniciar sesión (devuelve JWT)      |
| POST   | `/auth/logout`                    | USER/ADM | Invalidar JWT                      |
| POST   | `/auth/forgot-password`           | Pública  | Solicitar reseteo de contraseña    |
| GET    | `/auth/validate?token=`           | Pública  | Validar token de reseteo           |
| POST   | `/auth/reset-password`            | Pública  | Resetear contraseña                |
| POST   | `/auth/change-password`           | USER/ADM | Cambiar contraseña (logueado)      |
| GET    | `/auth/me`                        | USER/ADM | Perfil del usuario autenticado     |
| GET    | `/products?categoryId=`           | Pública  | Listar productos (filtrable)       |
| GET    | `/products/{id}`                  | USER/ADM | Detalle de producto                |
| POST   | `/products`                       | ADMIN    | Crear producto                     |
| PUT    | `/products/{id}`                  | ADMIN    | Actualizar producto                |
| DELETE | `/products/{id}`                  | ADMIN    | Eliminar producto                  |
| GET    | `/categories`                     | Pública  | Listar categorías                  |
| GET    | `/categories/all`                 | ADMIN    | Listar categorías (entidad cruda)  |
| GET    | `/categories/{id}`                | USER/ADM | Detalle de categoría               |
| POST   | `/categories`                     | ADMIN    | Crear categoría                    |
| PUT    | `/categories/{id}`                | ADMIN    | Actualizar categoría               |
| DELETE | `/categories/{id}`                | ADMIN    | Eliminar categoría                 |
| GET    | `/carts/mine`                     | USER     | Mi carrito (get-or-create)         |
| POST   | `/carts`                          | USER     | Crear carrito (idempotente)        |
| GET    | `/carts/{cartId}`                 | USER     | Carrito por ID                     |
| POST   | `/carts/{cartId}/items`           | USER     | Agregar producto (1 unidad)        |
| PUT    | `/carts/{cartId}/items/{prodId}`  | USER     | Actualizar cantidad                |
| DELETE | `/carts/{cartId}/items/{prodId}`  | USER     | Quitar ítem                        |
| DELETE | `/carts/{cartId}/items`           | USER     | Vaciar carrito                     |
| POST   | `/orders/checkout/{cartId}`       | USER     | Checkout (transaccional)           |
| GET    | `/orders/my-orders`               | USER     | Mis órdenes                        |
| GET    | `/orders`                         | ADMIN    | Todas las órdenes                  |
| GET    | `/orders/{orderId}`               | ADMIN    | Detalle de orden                   |
| PUT    | `/orders/{orderId}/status`        | ADMIN    | Cambiar estado de pago             |
| GET    | `/users`                          | ADMIN    | Listar usuarios                    |
| GET    | `/users/{id}`                     | ADMIN    | Detalle de usuario                 |
| PUT    | `/users/{id}`                     | USER/ADM | Actualizar nombre                  |
| DELETE | `/users/{id}`                     | ADMIN    | Eliminar usuario                   |

---

## Cómo levantar el proyecto

### Prerequisitos

- **Java 21+** (Temurin recomendado)
- **Maven 3.9+** (o usar el wrapper `./mvnw`)
- **Git**

### 1. Clonar

```bash
git clone https://github.com/piniolenzo/entrega-final-talento-tech-backend-java.git
cd entrega-final-talento-tech-backend-java
```

### 2. Configurar variables de entorno

Copiá `.example.env` a `.env` y completá las variables:

```bash
cp .example.env .env
```

Mínimo necesario para desarrollo local (con H2):

```env
JWT_SECRET=zwYwN2Y3MGUtOWE4NC00ODA5LWE4NmMtODJkZGM5Y2MzNDhjYWI4M2U3YjUtYmNjOS00NDljLWEyNGYtMTQ1OTc0YmU3ZmZjMDM1NjZmZmItMjJhMS00ZGY3LWI0NDktN2E3NTdhNDI1MDNj
JWT_EXPIRATION=900000

# SMTP (opcional para desarrollo - password reset no funciona sin esto)
FRONTEND_URL=http://localhost:5173 # Cambiar por el enlace de tu frontend
MAIL_HOST=smtp.gmail.com # Cambiar por los datos de tu proveedor
MAIL_PORT=587 
MAIL_USERNAME=tu-email@gmail.com
MAIL_PASSWORD=tu-contraseña-de-app
```

### 3. Ejecutar

Con Maven Wrapper (recomendado):

```bash
./mvnw spring-boot:run
```

O con Maven instalado:

```bash
mvn spring-boot:run
```

La aplicación arranca en `http://localhost:8080`.

### 4. Verificar que funciona

```bash
curl http://localhost:8080/products
```

### 5. Swagger UI (admin)

```
http://localhost:8080/swagger-ui
```

### 6. Consola H2

```
http://localhost:8080/h2-console
```

JDBC URL: `jdbc:h2:file:./data/techlab`  
User: `sa`  
Password: *(vacío)*

---

## Variables de entorno

| Variable           | Obligatoria | Default | Descripción                                          |
|--------------------|-------------|---------|------------------------------------------------------|
| `DB_URL`           | Sí*         | —       | JDBC URL (ej: `jdbc:h2:file:./data/techlab`)         |
| `DB_USERNAME`      | Sí*         | —       | Usuario de base de datos                             |
| `DB_PASSWORD`      | Sí*         | —       | Contraseña de base de datos                          |
| `DB_DRIVER`        | Sí*         | —       | Driver class (ej: `org.h2.Driver`, `org.postgresql.Driver`) |
| `JWT_SECRET`       | Sí          | —       | Clave HMAC-SHA en Base64                             |
| `JWT_EXPIRATION`   | Sí          | —       | TTL del token en milisegundos (ej: `900000` = 15 min)|
| `FRONTEND_URL`     | Sí          | —       | URL del frontend (para links en emails)              |
| `MAIL_HOST`        | No          | —       | Servidor SMTP                                        |
| `MAIL_PORT`        | No          | —       | Puerto SMTP                                          |
| `MAIL_USERNAME`    | No          | —       | Usuario SMTP                                         |
| `MAIL_PASSWORD`    | No          | —       | Contraseña SMTP                                      |
| `PORT`             | No          | `8080`  | Puerto del servidor                                  |

> \* Cuando se usa H2 embebido (configuración por defecto en `application.yml`), `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` y `DB_DRIVER` **no son necesarias** porque la conexión está hardcodeada en el YAML. Si querés conectar a PostgreSQL/MySQL, sobreescribí esas propiedades vía variables de entorno y seteá `spring.datasource.url`, etc.

---

## Docker

El `Dockerfile` usa build multi-stage para mantener la imagen final liviana:

```dockerfile
# Etapa 1: build con Maven + Temurin 21
FROM maven:3.9.9-eclipse-temurin-21 AS builder
# ...
RUN mvn -B -DskipTests package

# Etapa 2: runtime con JRE solamente
FROM eclipse-temurin:21-jre-jammy
COPY --from=builder /app/target/*.jar app/talentotech.jar
EXPOSE 3000
ENTRYPOINT ["java","-jar","app/talentotech.jar"]
```

### Build y ejecución

```bash
docker build -t techlab-api .
docker run -p 3000:3000 \
  -e JWT_SECRET=... \
  -e JWT_EXPIRATION=... \
  -e FRONTEND_URL=... \
  techlab-api
```

> **Nota:** El Dockerfile expone el puerto 3000 (no 8080). Si usás Docker, asegurate de que `PORT=3000` esté seteado o modificá el EXPOSE en el Dockerfile.

---

## Testing

### Tests unitarios de entidades

Los tests cubren la lógica de negocio de las entidades:

```bash
./mvnw test
```

**Tests incluidos:**

| Test class               | Sujeto               | Casos                                          |
|--------------------------|----------------------|------------------------------------------------|
| `ShoppingCartTest`       | ShoppingCart         | addItem, getItem, getTotalPrice, belongsTo     |
| `CartItemTest`           | CartItem             | getSubtotalPrice, null price handling           |
| `ProductTest`            | Product              | price, stock validation                         |
| `OrderTest`              | Order                | factory method, total calculation               |
| `OrderLineTest`          | OrderLine            | subtotal, unit price freeze                     |
| `CategoryTest`           | Category             | name validation                                 |
| `RoleTest`               | Role enum            | authority string                                |
| `PaymentStatusTest`      | PaymentStatus enum   | values, transitions                             |
| `TechLabApplicationTests`| Application context  | Smoke test: context loads                       |

### Para mejorar

Faltan tests de integración para:
- Controladores (WebMvcTest)
- Servicios (lógica de negocio)
- Repositorios (DataJpaTest)
- Seguridad (filtro JWT, autenticación)
- Flujo completo de checkout

---

## Seed data

Al iniciar la aplicación con la base de datos vacía, `DataLoader.java` carga automáticamente:

### Usuarios de prueba

| Rol     | Nombre        | Email                  | Contraseña     |
|---------|---------------|------------------------|----------------|
| ADMIN   | Admin         | `admin@techlab.com`    | `Admin12345-`  |
| USER    | Juan Pérez    | `juan@example.com`     | `User12345-`   |
| USER    | María García  | `maria@example.com`    | `User12345-`   |

### Datos de ejemplo

- **12 productos** en 5 categorías (Electrónica, Ropa, Hogar, Deportes, Libros)
- **2 carritos** (uno por usuario; Juan tiene carrito limpio post-seed, María vacío)
- **1 orden de ejemplo** para Juan (estado PAID, con 3 productos)
- **Categorías** predefinidas

---

## Frontend

Esta API está diseñada para consumirse desde un frontend SPA. El flujo típico sería:

```
1. VISITA → GET /products, GET /categories
2. REGISTRO/LOGIN → POST /auth/register | /auth/login → guardar JWT
3. CATÁLOGO + CARRITO → GET /carts/mine, POST /carts/{id}/items
4. CHECKOUT → POST /orders/checkout/{cartId}
5. ÓRDENES → GET /orders/my-orders
6. PERFIL → GET /auth/me, PUT /users/{id}, POST /auth/change-password
7. ADMIN → CRUD de productos, categorías, usuarios, órdenes
8. LOGOUT → POST /auth/logout → eliminar JWT del storage
```

---

## Roadmap / Mejoras futuras

### Pendientes técnicos

- [ ] Tests de integración para controladores y servicios
- [ ] Migrar `price` de `Float` a `BigDecimal` (precisión financiera)
- [ ] Migrar `category.id` de `Short` a `Long` (límite de 32k categorías)
- [ ] Agregar paginación a `GET /products` y `GET /orders`
- [ ] Agregar búsqueda por nombre en productos (`?search=`)
- [ ] Subir imágenes a cloud (S3/Cloudinary) en lugar de URL texto
- [ ] Refresh tokens (rotación de JWT)
- [ ] Rate limiting
- [ ] Logging estructurado (SLF4J + MDC)
- [ ] Health check endpoint (`/actuator/health`)
- [ ] Perfiles Spring (`dev`, `prod`) con configuraciones diferenciadas
- [ ] Migrar a PostgreSQL como base de datos principal en producción

### Comentarios y mejoras

¿Encontraste un bug, tenés una sugerencia o querés agregar funcionalidad? Este proyecto está abierto a contribuciones. Abrí un issue o enviá un pull request - toda ayuda suma.

### Features posibles a futuro

- [ ] Wishlist / lista de deseos
- [ ] Reviews y rating de productos
- [ ] Carrito persistente entre sesiones (ya está, pero falta merge guest→user)
- [ ] Cupones de descuento
- [ ] Webhooks de pago (Mercado Pago / Stripe)
- [ ] Notificaciones por email de estado de orden
- [ ] Historial de cambios de stock
- [ ] Soft-delete para productos y categorías
- [ ] Migración a microservicios

---

## Creador

**Piñol Enzo Ignacio**

Proyecto desarrollado como trabajo final del curso **Backend Java + Spring** impartido por **Talento Tech**.
