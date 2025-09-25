# Gestor de contraseñas - UCB

AEA-vault, es un gestor de contraseñas diseñado como práctica final del módulo 7 "Cybersecurity Fundamentals" de la Maestría en Ingeniería de Software Avanzada de la Universidad Católica Boliviana San Pablo, el objetivo es crear un gestor de contraseñas, que combine los principios de confidencialidad, Integridad y disponibilidad a través de distintos métodos de criptografía, con el fin de garantizar la seguridad y confidencialidad de las contraseñas almacenadas.

## Herramientas y Dependencias

Este proyecto está siendo desarrollado con las siguientes herramientas y gestiona sus dependencias a través de Maven:

*   **Java Development Kit (JDK):** Versión 21 (OpenJDK LTS). Se recomienda [Azul Zulu JDK 21](https://www.azul.com/downloads/?package=jdk) para un rendimiento óptimo.
*   **Apache Maven:** Para la gestión de dependencias, compilación y ejecución del proyecto.

Las principales dependencias del proyecto, gestionadas por Maven, incluyen:
*   **JavaFX:** Versión 21 (para la interfaz gráfica de usuario).
*   **Jackson:** Versión 2.19.2 (para la serialización/deserialización JSON).
*   **JUnit 5:** (para pruebas unitarias).

## Cómo Ejecutar el Proyecto

Para compilar y ejecutar la aplicación, asegúrate de tener instalado el JDK 21 y Maven.

1.  **Clonar el Repositorio:**
    ```bash
    git clone [URL_DEL_REPOSITORIO]
    cd gestor_contrasenias
    ```

2.  **Compilar el Proyecto:**
    ```bash
    mvn clean install
    ```
    Este comando compilará el código, ejecutará las pruebas y empaquetará la aplicación en un archivo JAR ejecutable.

3.  **Ejecutar la Aplicación:**
    *   **Desde Maven (para desarrollo):**
        ```bash
        mvn javafx:run
        ```

## Uso

Al iniciar la aplicación, podrás crear una nueva bóveda o abrir una existente. Una vez dentro, podrás gestionar tus contraseñas, nombres de usuario y URLs. La aplicación permite buscar, añadir, editar y eliminar entradas, así como cambiar la contraseña maestra de tu bóveda.

## Licencia

Este proyecto está bajo la licencia Apache 2.0. Consulta el archivo `LICENSE` para más detalles.