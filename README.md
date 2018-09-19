# [8INF956] - TP1

## Description du projet

### Dépendances
Le projet utilise le niveau de langage Java 8.
On peut trouver le JDK [ici](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).

### Éxécution

Dans un terminal, éxécuter les commandes suivantes :  
`$ java -jar ./build/server.jar`, cela lance le serveur sur le port 8080 (voir section Fonctionalités pour plus de détails).  
`$ java -jar ./build/client.jar`, cela lance l'interface console du client. 

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
 optional.ifPresent((value) -> {
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
 optional.ifPresent((value) -> {
   //Insert code here
 });
 ```

Sans expression lambda : 
```java
optional.ifPresent(new Consumer<Type>() {
  @Override
  public void accept(Type value) {
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
 (voir [FileReceiver](./src/server/network/FileReceiver.java) pour plus de détails concernant l'imlémentation).
 
 Comme le fichier est déja compilé ici, on se charge uniquement de loader la classe, l'instancier et l'éxécuter.
 
 ##### ObjectColl
 Ici, on suppose que le serveur possède déja la classe (déja loadé) et qu'il sait comment la déserialiser. 
 On réceptionne l'ojet sérialisé envoyé depuis le client, on l'instancie et on exécute la méthode. 
 
 Comme précisé précedemment, le pattern Adapter à été mis en place pour réutiliser le code de loading et d'éxecution de méthode ! 
 
Une fois la classe instancié correctement, le serveur attend que le client lui envoie la méthode, avec les paramètres. 
La syntaxe suivante doit être employée :  
`add 5 2`
En considérant que la méthode de l'objet à le prototype `int add(Integer a, Integer b)`.
 
 **Nous faisons également la supposition que le type de retour et des paramètres de la méthode
 est `Integer`.** 
 
 Le nombre d'arguments peut être supérieur à 2.
 
 #### Client
 
 Le client possède une interface de type console où différentes intéractions peuvent être éffectuées. 
 En premier lieu, il est demandé de renseigner l'adresse IP (IPv4) du serveur et le port 
 sur lequel l'application tourne. 
 Par défaut, l'adresse IP du serveur est "127.0.0.1" et le port est "8080". 
 
 Ensuite, on demande à l'utilisateur quel action il veut effectuer sur le serveur. 
 L'encapsulation des appels socket permet de rendre l'utilisation de l'application plus facile pour les utilisateurs.  
 Selon le choix, il peut être demandé de choisir le fichier à envoyé. 
 Bien que le choix soit laissé libre ici (à condition de respecter l'architecture de package clientMaClasse)
 il est conseillé d'envoyer le fichier '/src/client/IntegerCalculator' car aucun test n'a été effectué pour s'assurer du fonctionnement. 
 Si ObjectColl est choisi, une nouvelle instance de IntegerCalculator est crée et l'objet est envoyé sérialisé au serveur.
 La sérialization se fait avec celle de Java, en implémentant la méthode `Serialize` dans `IntegerCalculator`.
 
 
 Enfin, on demande quelle méthode appeler avec quels paramètres. 
 Liste des méthodes appelées avec un exemple d'utilisation : 
 `IntegerCalculator.add(Integer a, Integer b)` appelée avec `add 5 2`
 `IntegerCalculator.add(Integer a, Integer b, Integer c)` appelée avec `add 3 7 78`