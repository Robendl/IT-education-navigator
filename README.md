# Rijksoverheid


Run database met docker:
docker run --rm -p 5432:5432 -e "POSTGRES_PASSWORD=postgres" -e "POSTGRES_USER=postgres" -e "POSTGRES_DB=postgres" postgres

Run backend vanuit IntelliJ IDEA, wanneer je het project laad zou automatisch BackendApplication run configurations geladen moeten worden. Als die niet gebeurt kun je in backend/src/main/java/se/rijksoverheid het bestand BackendApplication vinden en hem via daar runnen.

base url:
http://localhost:8081/rijksoverheid/api

Voorbeeld CourseRequest:
```
{
    "name": "Introduction to Computer Science",
    "institution": "Rijksuniversiteit Groningen",
    "location": "Groningen",
    "level": "wo",
    "courseType": "Bachelor",
    "housekeepingRelated": false,
    "timeOccupation": "voltijd",
    "region": "oost",
    "collaboration": true,
    "responsibleTaskForce": "Cees",
    "professor": "John Smith",
    "contact": "csdept@rug.nl",
    "web": "https://www.rug.nl",
    "explanation": "This course covers the fundamentals of computer science, including programming, algorithms, and data structures."
}
```