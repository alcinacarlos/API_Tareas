# TaskMaster
## Descripción de los documentos
## Direccion

| Campo         | Tipo   | Descripción               |
|---------------|--------|---------------------------|
| **calle**     | String | Nombre de la calle        |
| **num**       | String | Número de la vivienda     |
| **municipio** | String | Municipio de la dirección |
| **provincia** | String | Provincia de la dirección |
| **cp**        | String | Código postal             |

---
## Tarea

| Campo           | Tipo    | Descripción                           |
|-----------------|---------|---------------------------------------|
| **_id**         | String? | Identificador único en MongoDB        |
| **titulo**      | String  | Título de la tarea                    |
| **descripcion** | String  | Descripción detallada                 |
| **usuarioId**   | String  | Identificador del usuario que la creó |
| **completada**  | Boolean | Indica si ha sido completada          |


---

## Usuario

| Campo         | Tipo      | Descripción                                |
|---------------|-----------|--------------------------------------------|
| **_id**       | String?   | Identificador único en MongoDB             |
| **username**  | String    | Nombre de usuario (único)                  |
| **password**  | String    | Contraseña del usuario                     |
| **email**     | String    | Correo electrónico del usuario (único)     |
| **roles**     | String?   | Rol asignado al usuario (USER por defecto) |
| **direccion** | Direccion | Dirección del usuario                      |


---

# Endpoints y Descripción

### Autenticación (Accesible para todos)
- **POST /login** - Inicia sesión y devuelve un token de autenticación
- **POST /registro** - Registra un nuevo usuario

### Endpoints para Usuarios con Rol `USER`
- **GET /tareas** - Obtiene todas las tareas del usuario autenticado
- **POST /tareas** - Crea una nueva tarea para el usuario autenticado
- **PUT /tareas/{id}/completar** - Marca una tarea como completada
- **DELETE /tareas/{id}** - Elimina una tarea propia

### Endpoints para Usuarios con Rol `ADMIN`
- **GET /admin/tareas** - Obtiene todas las tareas de todos los usuarios
- **POST /admin/tareas** - Crea una nueva tarea para cualquier usuario
- **DELETE /admin/tareas/{id}** - Elimina cualquier tarea de cualquier usuario

---

## Lógica de Negocio
- Un usuario `USER` solo puede gestionar sus propias tareas
- Un usuario `ADMIN` puede gestionar tareas de todos los usuarios
- No se puede eliminar una tarea que no existe
- Una tarea solo puede marcarse como completada una vez

---

# Excepciones y Códigos de Estado

| Código HTTP                   | Descripción                         |
|-------------------------------|-------------------------------------|
| **200** OK                    | Operación exitosa                   |
| **201** Created               | Recurso creado con éxito            |
| **400** Bad Request           | Datos de entrada inválidos          |
| **401** Unauthorized          | Usuario no autenticado              |
| **403** Forbidden             | Usuario sin permisos para la acción |
| **404** Not Found             | Recurso no encontrado               |
| **500** Internal Server Error | Error inesperado del servidor       |

---


# Seguridad en la API REST
- Uso de **JWT** para autenticación y autorización
- Encriptación de contraseñas con **BCryptPasswordEncoder**
- Restricción de acceso a endpoints según el rol del usuario
- Validación de datos en las solicitudes

---

# Pruebas gestión usuarios
- Contraseña no coincide
  ![img.png](images/ui/img.png)
  ![Imagen](images/insomnia/passworddontmatch.png)
- Usuario ya registrado
  ![yaregistrado.png](images/ui/yaregistrado.png)
  ![Imagen](images/insomnia/yaregistrado.png)
- Email inválido
  ![img_1.png](images/ui/img_1.png)
  ![Imagen](images/insomnia/errormail.png)
- Municipio inválido
  ![img_2.png](images/ui/img_2.png)
  ![Imagen](images/insomnia/errormunicipio.png)
- Usuario registrado correctamente
  ![Imagen](images/insomnia/usuariocreado.png)
  ![Imagen](images/insomnia/usuarioenmongo.png)
- Usuario logeado
    ![Imagen](images/insomnia/usuariologeado.png)
- Credenciales inválidas
    ![Imagen](images/insomnia/credencialesincorrectas.png)

# PRUEBAS GESTIÓN TAREAS
## Pruebas para Usuario con Rol `USER`

### 1. Ver todas sus tareas
**Descripción:** Se realiza una solicitud para obtener todas las tareas del usuario autenticado.
- **Método:** `GET /tareas`
- **Respuesta esperada:** Lista de tareas pertenecientes al usuario.
- **Código de estado esperado:** `200 OK`

  ![Imagen](images/insomnia/vertareas.png)
#### ❌ Errores posibles
Esto pasa en todas la ruta `/tareas` y solo lo voy a incluir aquí por no repetir lo mismo todo el rato
1. **Usuario no autenticado**
- **Código de estado esperado:** `401 Unathorized`

  ![Imagen](images/insomnia/vertareaserror.png)

### 2. Marcar como hecha una tarea propia
**Descripción:** Un usuario intenta marcar como completada una de sus tareas.
- **Método:** `PUT /tareas/{id}/completar`
- **Respuesta esperada:** Confirmación de la actualización.
- **Código de estado esperado:** `200 OK`

  ![Imagen](images/insomnia/completada.png)


#### ❌ Errores posibles
1. **Tarea no encontrada**
  - **Código de estado esperado:** `400 Bad Request`
  - **Mensaje esperado:** "Tarea no encontrada"

    ![Imagen](images/insomnia/completadanoencontrada.png)


2. **Intento de marcar como hecha una tarea que no es suya**
  - **Código de estado esperado:** `401 Unauthorized`
  - **Mensaje esperado:** "No puedes marcar como hecha una tarea que no es tuya"

    ![Imagen](images/insomnia/completadanotuya.png)

### 3. Eliminar una tarea propia
**Descripción:** Un usuario intenta eliminar una de sus tareas.
- **Método:** `DELETE /tareas/{id}`
- **Respuesta esperada:** Confirmación de eliminación.
- **Código de estado esperado:** `200 OK`

  ![Imagen](images/insomnia/eliminar.png)

#### ❌ Errores posibles
1. **Tarea no encontrada**
  - **Código de estado esperado:** `400 Bad Request`
  - **Mensaje esperado:** "Tarea no encontrada"

    ![Imagen](images/insomnia/eliminarnoencontrada.png)


2. **Intento de eliminar tarea de otro usuario**
  - **Código de estado esperado:** `403 Forbidden`
  - **Mensaje esperado:** "No puedes eliminar una tarea que no es tuya"

    ![Imagen](images/insomnia/eliminarnotuya.png)

### 4. Darse de alta a sí mismo una tarea
**Descripción:** Un usuario crea una nueva tarea para sí mismo.
- **Método:** `POST /tareas`
- **Cuerpo:** `{ "titulo": "Nueva tarea", "descripcion": "Descripción de la tarea" }`
- **Respuesta esperada:** Confirmación de creación.
- **Código de estado esperado:** `201 Created`

  ![Imagen](images/insomnia/alta.png)


#### ❌ Errores posibles
1. **Intento de crear una tarea para otro usuario**
  - **Código de estado esperado:** `401 Unauthorized`
  - **Mensaje esperado:** "No puedes asignar tareas a otro usuario"

    ![Imagen](images/insomnia/altanotuya.png)

---

## Pruebas para Usuario con Rol `ADMIN`

### 1. Ver todas las tareas
**Descripción:** Un administrador obtiene todas las tareas de todos los usuarios.
- **Método:** `GET /tareas`
- **Respuesta esperada:** Lista de todas las tareas en la base de datos.
- **Código de estado esperado:** `200 OK`

  ![Imagen](images/insomnia/vertareasadmin.png)


### 2. Eliminar cualquier tarea de cualquier usuario
**Descripción:** Un administrador elimina una tarea específica, en este caso un admin elimina la tarea de un NO ADMIN
- **Método:** `DELETE /tareas/{id}`
- **Respuesta esperada:** Confirmación de eliminación.
- **Código de estado esperado:** `200 OK`

  ![Imagen](images/insomnia/eliminar.png)


#### ❌ Errores posibles
1. **Tarea no encontrada**
  - **Código de estado esperado:** `400 Bad Request`
  - **Mensaje esperado:** "Tarea no encontrada"

    ![Imagen](images/insomnia/eliminarnoencontrada.png)

### 3. Dar de alta tareas a cualquier usuario
**Descripción:** Un administrador crea una tarea para un usuario específico, en este caso un usuario no admin
- **Método:** `POST /tareas`
- **Cuerpo:** `TareaInsertDTO`
- **Respuesta esperada:** Confirmación de creación.
- **Código de estado esperado:** `201 Created`

  ![Imagen](images/insomnia/altaadmin.png)


