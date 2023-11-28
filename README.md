# CrazyDisplay - Raspberri Websocket Server

## Action Protocol

This is an example of how we control the messages between server and client.

```json
WebSockets server, example of messages:

From server to client:
    - Confirm login       { "type": "login", "valid": true/false }

From client to server:
    - Login message         { "type": "login", "usr": "usrName", "pswd": "usrPassword" }
    - Send platform         { "type": "platform", "name": "usrPlatform" }
    - Send text             { "type": "message", "format": "text", "value": "usrText/usrImg" }
    - Send image            { "type": "message", "format": "img", "value": base64 encoded string:, "ext": img extension }
```
#### Usage/Examples
- Confirm login: When the server recive the login message from the client do a check and after checkin the usr and password send to the client if the login is valid or not.
- Login message: When the client confir a connection send to the server a usr and password to start sending messages.
- Send platform: When the server confirms login the client send to the server his platform.
- Send text or image: This message is used to send the info to display in the server.

## Authors

- Geanfranco Biagi - [@gbiagi](https://www.github.com/gbiagi)
- David Sanchez - [@MatillaSan](https://www.github.com/MatillaSan)

