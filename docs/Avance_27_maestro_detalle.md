# Avance del 27: compra de videojuegos con detalles

Este paquete parte de la versión de consolas y videojuegos que ya compilaste y probaste. Incluye solo archivos nuevos o modificados. No contiene credenciales, configuración privada ni historial Git; no hace commit ni push.

## Resultado

Una petición POST /purchases registra una compra (venta de la tienda) con una lista de videojuegos. El servidor consulta los precios del catálogo, calcula importes, asigna fecha/estado y descuenta existencias. La transacción abarca la cabecera, los detalles y el inventario. Se conserva la ruta /purchases y las tablas compras/compras_productos.

La lista ahora usa clientId, paymentMethod, comment e items; cada detalle recibe solamente videogameId y quantity. El JSON anterior en español ya no corresponde a este endpoint. El cliente no fija el total ni el precio cobrado.

## Aplicar los archivos

1. Detén la aplicación (Ctrl+C en la terminal donde corre bootRun).
2. Extrae este paquete fuera del proyecto. Conserva un respaldo de los archivos que vas a cambiar fuera del repositorio.
3. Copia el contenido del paquete dentro de videogame-store-backend: combina las carpetas src, database, docs y examples, y acepta reemplazar los archivos existentes. No elimines la carpeta src: este paquete es parcial. No incluye resources.
4. Reemplaza build.gradle por el incluido. El único cambio respecto a la versión del 26 es testRuntimeOnly 'com.h2database:h2', para probar la persistencia en una base temporal. Si modificaste build.gradle por tu cuenta, agrega únicamente esa dependencia en dependencies.
5. Si utilizas el instalador opcional Aplicar_avance_27.ps1, este copia esos mismos archivos y deja un respaldo fuera del proyecto. No ejecuta SQL, Gradle ni Git.

El código del catálogo permanece como en el 26, excepto la consulta que bloquea el videojuego al venderlo. El paquete corrige también equals/hashCode de CompraProductoPK.

## Base de datos

Con la aplicación detenida, haz un respaldo de videogame_store y ejecuta database/02_purchases.sql completo en Query Tool de ESA base. El script exige que ya existan las tablas del avance 26.

- Amplía medio_pago, estado y comentario de compras.
- Guarda total con tipo decimal y agrega precio_unitario a cada detalle.
- Conserva los registros existentes. Al convertir totales monetarios se redondean a dos decimales.
- Completa precios unitarios históricos desde total/cantidad donde sea posible.
- No borra tablas, compras ni clientes.
- Si hay un error, ejecuta ROLLBACK y revisa el mensaje antes de continuar.

## Compilar y ejecutar las pruebas

En la terminal de IntelliJ, dentro del proyecto:

```powershell
.\gradlew.bat clean compileJava compileTestJava
.\gradlew.bat test --tests "*CatalogValidationTest" --tests "*CatalogServiceTest" --tests "*PurchaseServiceTest" --tests "*PurchasePersistenceTest"
```

La dependencia H2 puede descargarse la primera vez. PurchasePersistenceTest configura expresamente una base H2 en memoria y credenciales de prueba; no utiliza PostgreSQL ni DB_PASSWORD. El test de contexto antiguo no se incluye en el comando.

Las cinco pruebas de persistencia verifican Cascade, rollback por stock insuficiente, rollback cuando falla la inserción de un detalle después de insertar la cabecera, referencia inexistente e historial de precios. No se debe dar por comprobado el rollback local hasta que las pruebas pasen.

## Iniciar la aplicación

Usa la configuración Videogame Store que tenga DB_PASSWORD y JWT_SECRET. Si ejecutas mediante Gradle desde PowerShell, esas variables deben estar definidas en ESA terminal antes de ejecutar:

```powershell
.\gradlew.bat --no-daemon bootRun
```

No pongas las credenciales en archivos versionados. Abre http://localhost:8081/swagger-ui.html, inicia sesión con uno de los clientes registrados y pega el token en Authorize.

## Demostración guiada en Swagger

1. Crea una consola Nintendo Switch mediante POST /consoles. Anota el ID real.
2. Crea dos juegos de esa consola: Super Mario Odyssey (precio 899, stock 10) y Zelda Tears of the Kingdom (precio 1299, stock 10). Anota ambos IDs reales; los IDs 2 de la prueba anterior fueron eliminados, por lo que no deben suponerse existentes.
3. POST /purchases con este cuerpo, reemplazando 1 y 2 por los IDs REALES de los juegos:

```json
{
  "clientId": "2",
  "paymentMethod": "CASH",
  "comment": "Prueba maestro-detalle",
  "items": [
    {"videogameId": 1, "quantity": 2},
    {"videogameId": 2, "quantity": 1}
  ]
}
```

Espera 201 y Location. Anota purchaseId. La respuesta debe tener dos detalles, total 3097.00, estado COMPLETED y fecha. Los detalles contienen unitPrice y total; no los envíes en el cuerpo de creación.

4. GET /purchases/{id}: debe devolver la misma compra con ambos detalles. GET /purchases/client/2 debe incluirla.
5. Consulta los juegos: Mario debe tener stock 8 y Zelda stock 9.
6. En pgAdmin abre database/03_verify_purchases.sql. Sustituye el ID 1 por el purchaseId devuelto y ejecuta las consultas. Deben aparecer una cabecera, dos detalles y suma 3097.00.
7. Antes de probar una compra inválida, anota los conteos de compras/detalles y el stock de los dos juegos. Envía otra compra: Mario cantidad 1 y Zelda cantidad 1000. Debe responder 409. Repite las consultas: conteos y existencias deben seguir iguales.
8. Envía un detalle sin quantity: espera 400. Envía un videogameId inexistente: espera 404. Ninguno debe crear compras ni descontar stock.

El archivo examples/purchases.http reúne las peticiones. Los IDs y el token son marcadores que debes reemplazar. Las secuencias pueden saltar números tras un rollback: no es un registro parcial.

## Reglas implementadas

- Cliente y videojuegos deben existir.
- De 1 a 100 líneas; cantidades enteras de 1 a 1000. No se permite repetir un juego en el mismo JSON.
- Juego y consola deben estar activos y debe haber stock suficiente.
- Formas de pago: CASH, CARD, TRANSFER.
- Se usan BigDecimal e importes persistidos por detalle, para que cambiar el precio del catálogo después no altere compras anteriores.
- Las compras bloquean filas de inventario por ID en orden para serializar compras concurrentes del mismo juego.
- Compra.productos conserva CascadeType.ALL. Solo se guarda la cabecera y se asigna la referencia del padre en cada detalle; no hay guardados individuales de detalles.
- @Transactional cubre el flujo completo. Errores de datos/integridad responden 400, 404 o 409 en el controlador de compras.

## Qué se verificó al preparar el paquete

Se ejecutaron 28 comprobaciones aisladas de servicios/validación (12 de compras y 16 de catálogos) sin Spring, usando repositorios de prueba. Se comprobó sintaxis Java, imports internos y rutas de paquetes. Estas comprobaciones no ejecutan transacciones ni MapStruct.

No se pudo ejecutar Gradle con sus dependencias, ni las cinco pruebas H2, ni el SQL en PostgreSQL en el entorno de preparación. La compilación completa, las pruebas de integración y las peticiones HTTP quedan para tu computadora. El avance de catálogos ya probado sigue siendo la base.

## Guardar el avance del 27

Después de que compilen los archivos y pasen las pruebas, revisa primero:

```powershell
git status --short
```

Verifica el historial del 26 antes de mezclar avances. Deja las carpetas de respaldo fuera del repositorio y no agregues application-dev.properties, secretos ni archivos de IntelliJ. Después se pueden seleccionar los archivos de este avance y crear el commit:

```text
feat: implement transactional purchases with cascading details
```

El 28 queda la revisión integral de seguridad, anotaciones de Swagger exigidas por el profesor y pruebas de errores de toda la API.
