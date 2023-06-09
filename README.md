# Rijksoverheid

## Easy startup

Run the following command from the main directory:
``docker-compose up``

## Development startup guide
### DB
Installeer en open docker.
In een terminal, run:
```
docker run -d -p 5434:5432 -e "POSTGRES_PASSWORD=postgres" -e "POSTGRES_USER=postgres" -e "POSTGRES_DB=postgres" --name rijksoverheid-postgres postgres
```
### Backend
Open het project in IntelliJ IDEA. De ``BackendApplication`` Run Configuration zou automatisch geladen moeten worden. 
Als dit niet gebeurt kun je in ``backend/src/main/java/se/rijksoverheid`` het bestand ``BackendApplication`` vinden en hem via daar runnen.

### Frontend
In een terminal, navigeer naar de directory ``/frontend`` en run:
```
npm install
```
Om de ``npm`` te installeren. Run hierna:
```
npm start
```
De applicatie wordt automatisch geopend (http://localhost:3000).


Beschikbare frontend tests kunnen worden opgestart met:
```
npm test
```

### API
Root URL:
http://localhost:8081/rijksoverheid/api
