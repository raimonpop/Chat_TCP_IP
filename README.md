# Chat TCP/IP en Java con JavaFX

Este proyecto es una aplicación de chat simple que utiliza sockets TCP/IP para la comunicación entre el servidor y los clientes. La interfaz gráfica de usuario está construida con JavaFX y el proyecto está estructurado utilizando el patrón de diseño MVC (Modelo-Vista-Controlador).

## Características

- Iniciar un servidor de chat.
- Conectar a un servidor de chat existente.
- Enviar y recibir mensajes.
- Notificar a los clientes cuando el servidor se detiene.
- Control de excepciones y errores básicos en la interfaz gráfica.

## Requisitos

- JDK 11 o superior.
- JavaFX SDK 11 o superior.
- IntelliJ IDEA (recomendado) u otro IDE compatible con JavaFX.

## Configuración del proyecto

1. Clona el repositorio en tu computadora.
2. Abre el proyecto con tu IDE favorito (se recomienda IntelliJ IDEA).
3. Configura el SDK de JavaFX en tu IDE y asegúrate de agregar las bibliotecas de JavaFX al classpath del proyecto.
4. Ejecuta la aplicación desde el IDE. Si es necesario, crea una configuración de ejecución para ejecutar la clase principal (por ejemplo, `HelloApplication`).

## Uso

1. Ejecuta la aplicación.
2. Ingresa la dirección IP del servidor en el campo "IP del servidor" y presiona el botón "Conectar" para conectarte a un servidor existente.
3. Si deseas iniciar un servidor de chat, simplemente presiona el botón "Iniciar servidor". El servidor se iniciará y escuchará las conexiones entrantes.
4. Para enviar un mensaje, escribe el mensaje en el campo de texto y presiona el botón "Enviar".
5. Los mensajes recibidos y enviados se mostrarán en el área de mensajes.
6. Para detener el servidor, presiona el botón "Detener servidor". Esto cerrará el servidor y notificará a los clientes conectados.

## Contribuciones

Las contribuciones son bienvenidas. Siéntete libre de abrir un issue o crear un Pull Request para proponer mejoras o solucionar errores en el proyecto.
