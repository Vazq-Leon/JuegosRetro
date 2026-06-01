# JuegosRetro

Proyecto Java con Swing que incluye login/registro, base de datos local y tres juegos clásicos: Snake, Tetris y Buscaminas.

## Requisitos
- Java 17+ (NetBeans recomendado)
- MySQL (XAMPP) en `localhost:3306`
- MySQL Connector/J (agregar el .jar al proyecto en NetBeans)

## Configuración de base de datos
1. Inicia MySQL desde XAMPP.
2. Ejecuta el script `db/schema.sql` para crear la base de datos y tablas.
3. Asegúrate de tener permisos para crear la base de datos.

### Parámetros por defecto
- Base de datos: `juegos_retro_db`
- Usuario: `root`
- Contraseña: (vacía)
- Puerto: `3306`

> ⚠️ La contraseña vacía de `root` es solo para desarrollo local con XAMPP. En entornos reales define una contraseña segura y actualiza la configuración.

### Personalizar conexión
Puedes cambiar la conexión con propiedades del sistema o variables de entorno:

| Propiedad del sistema | Variable de entorno | Descripción |
| --- | --- | --- |
| `juegosretro.db.host` | `JUEGOSRETRO_DB_HOST` | Host de MySQL |
| `juegosretro.db.port` | `JUEGOSRETRO_DB_PORT` | Puerto |
| `juegosretro.db.name` | `JUEGOSRETRO_DB_NAME` | Base de datos |
| `juegosretro.db.user` | `JUEGOSRETRO_DB_USER` | Usuario |
| `juegosretro.db.password` | `JUEGOSRETRO_DB_PASSWORD` | Contraseña |

## Ejecutar
1. Abre el proyecto en NetBeans.
2. Agrega el MySQL Connector/J al classpath del proyecto.
3. Ejecuta `JuegosRetro`.
4. Regístrate y guarda tus puntajes.

## Juegos
- **Snake**: usa las flechas para moverte.
- **Tetris**: flechas izquierda/derecha para mover, arriba para rotar, abajo para bajar y barra espaciadora para caída rápida.
- **Buscaminas**: clic izquierdo para revelar, clic derecho para marcar una bomba.
