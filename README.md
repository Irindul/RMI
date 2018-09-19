# [8INF956] - TP1

## Description du projet

### Dépendances
Le projet utilise le niveau de langage Java 8.
On peut trouver le JDK [ici](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).

### Détails

Le projet est architecturé dans deux packages : 
 - client : Contient tout le code lié au client, c'est à dire
 l'envoie de fichier en TCP, l'envoie d'un objet sérialisé etc...
 - server : Contient le code lié au serveur : Gestions des connexions, 
 parralélisation des différents utilisateurs, récéption de fichiers etc.. 
 
 Plusieurs pattrons de conception ont été utilisés ici : 
  - Adapter, pour éviter la duplication de code entre par exemple SourceColl et ByteColl
  - State, pour gérer les différents états du serveur (Attente, Récéption de fichier .java, récéption de fichier .class etc...)
  - Factory, pour instancier les différents états
 
 Ce projet utilise les interface fonctionnels de Java 8, ainsi que les 
 `Optional`. Le pattron `Optional` permet d'encapsuler une variable lorsque l'on est pas sur
 qu'elle contienne une valeur. 
 On l'utilisera de la forme suivante :   
 `Optional<Type> optional = Optional.of(value);`   
 ou   
 `Optional<Type> optional = Optional.empty();`
 
 cela nous permet ensuite d'utiliser la programmation fonctionelle pour faire par exemple ce genre d'opérations : 
 ```java
 optional.ifPresent((t) -> {
   //Insert code here
 });
 ```
 
 Ce code est équivalent au suivant : 
 ```java
 if(value != null) {
  //Insert code here
 }
 ```
 
Les expressions lambda de Java 8 permettent de limiter la verbosité :  

Avec expression lambda : 
 ```java
 optional.ifPresent((t) -> {
   //Insert code here
 });
 ```

Sans expression lambda : 
```java
optional.ifPresent(new Consumer<Type>() {
  public void accept(Type t) {
    //Insert code here
  }
});
```

### Fonctionalités 

#### Serveur 
Le serveur se lance sur un port (8080 par défaut). 
Le port peut être spécifié grâce à la variable 
d'environnement PORT. Le PORT doit être supérieur à 1024. 

```bash
$ export PORT=8081
```

Le serveur attend ensuite les connections. Chaque nouvelle connection est lancée 
dans un thread séparé. 

Une fois la connection établie, on attend une commande du client. Celui ci peut envoyer les commandes suivantes : 
- sourcecoll
- bytecoll
- objectcoll
- quit

Toute autre commande sera apparentée à la commande `quit` et terminera la connection. 

##### SourceColl
 Le serveur se prépare à recevoir un fichier et renvoie un ACK sous la forme d'un message dans le flux.
 Une fois ce message reçu par le client, l'envoi peut commencer. 
 Le client doit envoyer les métadonnées du fichier (nom et taille en bytes).  
 On prépare un buffer de 4096 bytes et on lis depuis le flux entrant cette quantité, on l'écrit directement dans 
 un `FileOutputStream` afin d'obtenir un flux sur le fichier.
 On accuse la récéption du fichier grâce à un second ACK. 
 
 Le fichier est ensuite compilé et loadé dans l'environnement. Cela nous permet de créer une instance de la classe, et on peut 
 donc invoquer des méthodes dessus. 
 
 L'architecture de package de java nous oblige à créer la même hierarchie que celle du client. 
 Ici, nous avons supposé que le fichier envoyé est dans un package client : 
 `client.IntegerCalculator`
 
 ##### ByteColl
 La récéption de fichier se passe exactement de la même manière qu'avec SourceColl 
 (voire [FileReceiver](./src/server/network/FileReceiver.java) pour plus de détails concernant l'imlémentation).
 
 Comme le fichier est déja compilé ici, on se charge uniquement de loader la classe, l'instancier et l'éxécuter.
  