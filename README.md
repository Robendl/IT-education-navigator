# Rijksoverheid


Run database met docker:
docker run --rm -p 5432:5432 -e "POSTGRES_PASSWORD=postgres" -e "POSTGRES_USER=postgres" -e "POSTGRES_DB=postgres" postgres

Run backend vanuit IntelliJ IDEA, wanneer je het project laad zou automatisch BackendApplication run configurations geladen moeten worden. Als die niet gebeurt kun je in backend/src/main/java/se/rijksoverheid het bestand BackendApplication vinden en hem via daar runnen.
