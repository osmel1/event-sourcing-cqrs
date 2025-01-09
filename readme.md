# Démonstration de Spring Boot avec Axon Framework : Implémentation de CQRS et Event Sourcing

Ce projet propose un exemple simple pour appliquer les concepts de CQRS et Event Sourcing dans une application Spring Boot, en utilisant le framework Axon.

---

## Les concepts de CQRS et Event Sourcing

**CQRS (Command Query Responsibility Segregation)**: Une approche qui sépare la gestion des commandes (écriture) des requêtes (lecture). Cela permet d’optimiser chaque côté indépendamment :

- **Écriture** : Concentre les opérations modifiant l'état.
- **Lecture** : Se focalise sur l’accès et la présentation des données.

**Event Sourcing**: Au lieu de sauvegarder directement l’état final d’une entité, on conserve une séquence d’événements décrivant son évolution. L’état actuel est reconstitué en rejouant ces événements, ce qui offre des avantages comme l’auditabilité et la récupération des états passés.

---

## Configuration des dépendances

1. Ajout du framework Axon : Excluez le connecteur Axon Server si vous n'utilisez pas Axon Server.

```xml
<dependency>
    <groupId>org.axonframework</groupId>
    <artifactId>axon-spring-boot-starter</artifactId>
    <version>4.10.3</version>
    <exclusions>
        <exclusion>
            <groupId>org.axonframework</groupId>
            <artifactId>axon-server-connector</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

2. Ajouter Swagger pour tester facilement les API :

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.7.0</version>
</dependency>
```

---

## Structure et fonctionnement

### API Commune
Elle regroupe les éléments partagés entre les modules :
- **Commandes** .
- **Événements** .
- **DTOs** utilisés par les côtés commande et requête.
 
### Gestion des Commandes (Écriture)
- **Logique métier dans les Agrégats**: Where your domain logic lives.
  - Définis avec l’annotation  `@Aggregate`
  - **Command Handlers** : Gestion des commandes.
  - **Event Sourcing Handlers** : Mise à jour de l'état interne en appliquant les événements.

- **Exemple de contrôleur pour la création d’un compte**:
  
  ```java
    @PostMapping("/create")
    public CompletableFuture<String> createAccount(@RequestBody CreateAccountRequestDTO request) {
           return commandGateway.send(new CreateAccountCommand(
                UUID.randomUUID().toString(),
                request.getIniatialBalance(),
                request.getCurrency()
        ));
  }
  ```

  - Les commandes sont envoyées via le **CommandGateway** .
  - Optionnellement, on peut accéder à l’`EventStore` pour examiner les événements stockés.

### Gestion des Requêtes (Lecture) :

Les requêtes permettent de construire un modèle optimisé pour la lecture :

Les gestionnaires réagissent aux événements pour maintenir les données de lecture
Les entités sont stockées avec JPA/Hibernate (ex. : Account, Operation)
- **Controller** (example snippet):
  ```java
      @GetMapping("/account/{accountId}")
      public CompletableFuture<AccountLookupResponseDTO> getAccount(@PathVariable String accountId) {
          return queryGateway.query(
              new FindAccountQuery(accountId),
              ResponseTypes.instanceOf(AccountLookupResponseDTO.class)
          );
      }
  ```

Les commandes sont envoyées via CommandGateway. Optionnellement, utilisez l'EventStore pour examiner les événements stockés.

---

## Tester et exécuter l’application

1. **Lancer l’application**: `mvn spring-boot:run` (or run from your IDE).
2. Accéder à Swagger **Swagger UI** (by default at `http://localhost:8083/swagger-ui.html`) .
3. **Créer un compte** POST sur  `/commands/accounts/create` avec ce JSON:
   ```json
   {
     "iniatialBalance": 200,
     "currency": "MAD",
     "status": "CREATED"
   }
   ```
4. **Créditer un compte** PUT sur  `/commands/accounts/credit`:
   ```json
   {
     "id": "<the-account-id>",
     "amount": 999,
     "currency": "MAD"
   }
   ```
   
---

## Results
- **Création de compte**
![1](https://github.com/user-attachments/assets/13de06e3-e949-4555-9667-847298464b91)
Génération d’un ID après la commande.
- **Créditer un compte**
![3](https://github.com/user-attachments/assets/6cd97c1f-5899-4a05-81a5-eb4cd4bb1ed8)
 Confirmation après un PUT réussi.
- **Lecture des données**  
![3](https://github.com/user-attachments/assets/1d7216b9-2f79-446a-87b7-cf9ecdf7ffd7)
Visualisation des soldes et opérations via le modèle de requête.
