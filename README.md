# Trade Processing Api
It is a demo API service based on springboot2 to provide serveral endpoints to process trade message.Retrieve status of trade message and list of trade message.  
## API
### **class** TradeController  
#### `POST /trade/review`
It is public API to consume trade message. Once controller captures the message, the message will be routed to TradeProcessor service to save into database and further processing. I take an assumption that it will take time to process the message. so event listener is used for asynchonized process.  
A transaction Id will be returned such that UI can lookup the trade by transaction id.
#### `GET /trade/{txnId}`
It is public API to provide frontend endpoint to retrieve trade message by transaction id. The message is same as consumbed trade message with additional fields txnId and status.  
#### `GET /trade/query`

It is public API to provide additional frontend endpoint to retrieve list of trade message by user id.

### **class** AdminController  
#### `GET /admin/trade/list`
It is private API to list all trade messages. It is restricted who has ADMIN role

## What I did
I built this service with common approach endpoint <-> controller <-> service <-> repository <-> database and additionally Event Listener has been used to give immediate response once controller capture trade message.  
Controller is the endpoint and it's purpose to router request and response and it does not contain any business logic.
- Service is the business layer to handle the processing of trade. 
- Repository is the communication between phyiscal database and service.
- Event Listener is used to consume the event to have additional processing. When service publishes the event, the resource can be released and response to client immeidately.

I have also setup basic authorization that's restrict usage of specific endpoint however, more advance authorization (like oauth2) have to be used in real situation.

## What else
- Swagger UI has included for API doc
- CORS configuration (webconfig)
- Security configuration (WebSecurityConfig)
- Lombok annotation that reduce boilerplate code suc ash logging, getter/setter in POJO object
- In memory DB used for testing only
- Mockito uses to mock object in unit test
- springboot validation used to handle message validation

## Unit test
A simple unit test has been covered for controllers, service, components (excluded DTO, DAO, Repository).  

## HOW-TO
### Testing
1. Test the service: `./mvnw test`
2. Surefire test: `./mvnw surefire:test`  
### Run the service
`./mvnv spring-boot:run  `
### Build the service:
`./mvnw clean compile package`

## Swagger UI
`http://localhost:8080/swagger-ui.html`



