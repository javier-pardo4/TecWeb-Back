# TecWeb-Back

Autores: Carlos Sánchez Diaz, Javier Pardo González y Álvaro García Caballero

---

## ✨ *Cambios Realizados*

### 🛠️ Configuracion de .env para las variables:
- Hay que crear un archivo .env que debe de tener el siguiente formato:
  DB_USER= "Usuario de la base de datos"
  DB_PASSWORD= "Contraseña de la Base de Datos"
  STRIPE_SECRET_KEY = "Api secreta de stripe"


---

### 🛠️ Configuración de MySQL: Eliminación en cascada
- En el archivo propperties en la urla hay que poner la url a la base de datos.
- Se configuró MySQL para que el *ON DELETE* sea en cascada:
  - ✅ Al eliminar un usuario, se eliminan automáticamente sus listas y productos asociados.
  - ✅ De igual manera, si se elimina una lista, se eliminan automáticamente todos los productos relacionados.

---

### 📧 Implementación de correos electrónicos
- Ahora se envía un correo en los siguientes casos:
  - *Confirmación de cuenta:* Cuando el usuario completa su registro.
  - *Aceptación de invitaciones:* Cuando el usuario debe aceptar una invitación para una lista compartida.

- *Requisito:*
  ⚠️ Es necesario registrarse con un correo real de Gmail para garantizar la recepción de estos correos.

- *En los tests con Selenium:*
  - Durante las pruebas, el enlace del correo electrónico se muestra por pantalla para facilitar el proceso y evitar tener que ingresar al correo.
  - 🔒 Esta funcionalidad es *temporal*
 
- *Configuracion:*
   - Actualmente esta configurado con un correo creado para poder enviar los correos y su correspondiente contraseña de aplicacion

---

### 💳 Configuración de Stripe
- Para la funcionalidad de pagos, es necesario añadir tu *Secret API Key* de Stripe en el archivo de configuración correspondiente para que el sistema pueda procesar pagos correctamente.
  - 🛠️ *Nota:* Si no tienes la clave, puedes generarla en el [Dashboard de Stripe](https://dashboard.stripe.com).

---

### 🔄 Limitaciones en la actualización automática (WebSockets)
- Se implementaron *WebSockets* en algunos componentes del sistema, pero hay escenarios que aún no se actualizan automáticamente:
  - *Invitaciones a listas compartidas:* Cuando un usuario es invitado a una lista, la página no se actualiza automáticamente; es necesario recargarla manualmente.
  - *Eliminaciones de listas compartidas:* Lo mismo aplica cuando se elimina una lista compartida.

🔹 Estas limitaciones están identificadas y podrían resolverse en futuras iteraciones.

---

### 🔍 Implementación de chat en listas compartidas
- Se añadió un *chat en tiempo real* para que los miembros de una lista compartida puedan comunicarse directamente desde la plataforma.

---

### ⚙️ Pruebas de rendimiento con JMeter
- Siguiendo las recomendaciones del libro de referencia, realizamos pruebas de rendimiento con *JMeter*:
  - Se simularon acciones como registrar cuentas, iniciar sesión y crear listas utilizando numerosos grupos de hilos.
- Estas pruebas están diseñadas específicamente para medir el rendimiento del sistema bajo carga y no reemplazan las pruebas funcionales de Selenium.

---

### 🔧 Detalles sobre los tests con Selenium
- En general, las pruebas con Selenium funcionan correctamente, pero se identificaron algunos errores ocasionales:
  - En ciertas ejecuciones, el tiempo de pausa no es suficiente para cargar.
