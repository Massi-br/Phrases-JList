Le but de cette série est de créer un modèle particulier pour adapter une JList qui vit à l'intérieur d'un bean Java. On en profitera pour rajouter un petit renderer qui permet d'afficher différemment les éléments de la JList sur lesquels on aura double cliqué.

Préliminaires
On veut définir un nouveau bean affichant une liste de phrases, et permettant de ne sélectionner que celles qui vérifient un critère donné (par exemple celles qui contiennent le facteur « ph »). Le mécanisme de sélection est mis en place à l'aide d'une boîte combo permettant de choisir le type de filtre que l'on veut appliquer (facteur interne, préfixe, suffixe, etc.), et d'un champ de texte permettant d'indiquer la valeur par rapport à laquelle on veut filtrer. Sur l'image ci-dessous vous pouvez voir que l'application simplissime affiche un tel composant avec de plus un bouton qui permet de charger un fichier de phrases pour initialiser le contenu de la liste :

screenshoot.jpg

Description des concepts et travail à faire
s1.jpg

### Présentation des paquetages :
phrase contient la classe racine de l'application : Main.
phrase.model contient l'interface Filter qui spécifie un type global pour les différentes sortes de filtres utilisables sur des chaînes de caractères. Il contient aussi l'interface FilteringListModel qui spécifie un nouveau type de modèles de JList permettant de filtrer ses éléments, qui sont des chaînes de caractères, en fonction d'un filtre donné. Cette dernière interface sera implantée dans les classes StdFilteringListModel et MarkableFilteringListModel, la première étant une implantation standard de l'interface FilteringListModel et la seconde une autre implantation qui, au surplus de ce que fait la précédente, mémorise le marquage des éléments de la liste graphique.
phrase.model.filters contient les différentes réalisations disponibles de l'interface Filter.
phrase.view contient une nouvelle classe de bean : FilteringPane. Il s'agit d'une sorte de JPanel dotée d'une JList dont le modèle est un FilteringListModel (à la fin de ce TP, ce sera un MarkableFilteringListModel), d'un JTextField et d'une JComboBox dont le modèle est constitué d'un filtre pour chacun des types disponibles dans phrase.model.filters. On trouve aussi une nouvelle sorte de renderer de cellules de listes graphiques : ItalicListCellRenderer (qui affiche en italique les éléments de la liste qui sont marqués dans son modèle).
phrase.gui contient la classe PhraseAppli qui code l'application graphique permettant d'expérimenter le comportement d'un FilteringPane.
Vous trouverez aussi un répertoire serie07/data (qu'il n'est pas utile de placer dans le chemin de génération) contenant un fichier de texte contenu.txt. Il s'agit d'une séquence de phrases générées aléatoirement. Vous pouvez l'utiliser pour vous amuser, mais tout autre fichier de texte fonctionnera aussi, bien évidemment.

### Présentation des filtres
Un filtre f est un objet permettant de sélectionner, parmi une liste de chaines de caractères, celles qui vérifient une certaine propriété comme, par exemple : « la chaîne s contient le mot v ». On peut définir formellement la propriété associée à f par une fonction booléenne p : S × S -> B telle que p(s, v) = VRAI si et seulement si la chaîne s contient la chaîne v (où S est l'ensemble des chaînes et B l'ensemble des booléens). Appliquer f sur tous les éléments d'une liste de chaînes, consiste à fixer le paramètre v de p et à faire varier le paramètre s sur tous les éléments de cette liste, en retenant seulement ceux pour lesquels p(s, v) retourne VRAI.

En Java, on réalisera cela à l'aide d'une interface dotée des méthodes suivantes :

String getValue() : indique le mot sur lequel on se base pour filtrer (c'est le paramètre v du paragraphe précédent) ;
void setValue(String v) : change le mot sur lequel on se base pour filtrer ;
boolean accept(String s) : applique la propriété p sur la chaîne s et sur le mot getValue() ; dit autrement, après exécution de setValue(v), si accept(s) == true, cela est équivalent à p(s, v) = VRAI ;
List<String> filter(List<String> lst) : filtre la liste argument pour produire une nouvelle liste constituée uniquement des éléments de lst qui vérifient la propriété p lorsqu'on se base sur le mot que représente getValue() au moment de l'exécution.
Modèle de liste filtrante
Le modèle de listes filtrantes devra pouvoir être associé à une JList au sein du bean (FilteringPane) que nous allons développer bientôt. Ce type de modèles est spécifié dans l'interface FilteringListModel qui étend donc tout naturellement l'interface javax.swing.ListModel.

Il faut concevoir un modèle de liste filtrante comme une extension des modèles généraux de listes (les instances de ListModel). Un modèle général de liste permet essentiellement d'accéder à un élément par son rang et de connaître le nombre total d'éléments. Lorsqu'une JList possède un tel modèle, elle en affiche tous les éléments. Nous enrichissons le comportement des modèles généraux dans l'interface FilteringListModel en considérant que ces nouveaux modèles contiennent non pas une séquence d'éléments mais deux. La première est la séquence de tous les éléments disponibles dans le modèle, la seconde est la sous-séquence des éléments (de la première séquence) qui sont acceptés par un filtre agrégé au modèle. Et c'est cette deuxième séquence d'éléments (et non la première) que devra afficher la JList.

Résumons. Pour pouvoir fonctionner, nos nouveaux modèles nécessitent donc un filtre et deux séquences d'éléments : la séquence complète de tous les éléments du modèle, et la sous-séquence des éléments acceptés par le filtre du modèle. Quand le filtre change de valeur, ou quand le modèle change de filtre, la première séquence ne varie pas, mais la seconde est reconstruite par le modèle. Une JList associée à un tel modèle présente toujours la sous-séquence, jamais la séquence complète. Toutefois, il se peut que les deux séquence d'un modèle coïncident (si le filtre est null ou si sa valeur accepte tous les éléments de la séquence complète).

Les requêtes définies dans ListModel sont getElementAt et getSize. Ce sont elles qui permettent à la liste de présenter ses éléments. Dans FilteringListModel, elles devront donc permettre d'accéder aux éléments de la séquence d'éléments filtrés ainsi qu'à la taille de cette sous-séquence. De cette manière, la JList n'affichera que les éléments filtrés. On pourra toujours accéder à la séquence complète, mais par utilisation des nouvelles méthodes getUnfilteredElementAt et getUnfilteredSize.

Un modèle de liste filtrante doit être mutable. On peut lui ajouter un élément par la méthode void addElement. Celle-ci ajoute l'élément inconditionnellement à la fin de la séquence complète, et éventuellement à la fin de la sous-séquence filtrée (si cet élément est accepté par le filtre actuel). On peut aussi remplacer la totalité des éléments d'un modèle de listes filtrantes avec la méthode void setElements qui prend une collection d'éléments en paramètre.

Nos modèles de listes filtrantes possèdent encore une propriété non liée, accessible en lecture et en écriture, de nom filter et de type Filter, qui représente le filtre actuellement utilisé par le modèle.

Et pour finir sur ce sujet, il faut parler un peu de la classe d'implantation StdFilteringListModel que vous allez coder. Cette classe étendra javax.swing.AbstractListModel car celle-ci contient des méthodes fire\*() qui facilitent grandement les notifications de ListDataEvent. Je vous rappelle qu'une JList contient un ListDataListener qui observe ces notifications et rafraîchit correctement le ui-delegate de la JList.

La question suivante est : quand le modèle doit-il se rafraîchir ? Il doit observer les changements de valeur de la propriété value de son filtre pour pouvoir filtrer sa liste complète à chaque notification. Voici un diagramme de séquence qui illustre l'enchaînement des actions au cours de la modification de la valeur du filtre :

setValue.svg

La partie de gauche du diagramme (concernant les objets :Filter jusqu'à :StdFilteringListModel) indique ce qu'il se passe lorsqu'on change la valeur du filtre associé au modèle. La partie droite (concernant les objets :ListDataListener et :JList) n'est là que pour vous rappeler comment la JList se rafraîchit lorsque son modèle change d'état.

#### Travail à faire
Codez une classe StdFilteringListModel qui implante l'interface FilteringModel.
Le bean FilteringPane
Notre bean est une sorte de JPanel qui agrège une JList, une JComboBox et un JTextField. Vous repèrerez facilement les différentes classes en présence, ainsi que les noms des attributs, à l'aide du diagramme suivant :

diagClassesFilteringPane.svg

La note attachée entre les classes FilteringPane et StdFilteringListModel indique que le filtre du modèle de liste coïncide toujours avec l'élément sélectionné dans la boîte combo.

La partie “contrôleur” du bean
En tant que bean, notre composant détient un certain nombre d'écouteurs internes qui assurent la liaison entre sa vue et les différentes parties de son modèle.

Lorsque le client choisit tel ou tel filtre dans la boîte combo, un écouteur branché sur la boite combo, qui écoute les changements d'item (ItemListener), récupère l'instance de filtre qui devra être attachée au modèle de la liste puis réagit comme indiqué sur le schéma suivant :

itemSelected.svg

Lorsque le client modifie le contenu du champ de texte, il faut modifier aussitôt (sans attendre la frappe d'un retour-chariot) la valeur associée au filtre du modèle de liste filtrante. Souvenez-vous de ce que nous avons fait dans la série 2 : nous avons branché un DocumentListener directement sur le modèle du composant de texte pour être notifié “en temps réel” de tout changement du texte :

keyPressed.svg

#### Travail à faire dans la classe FilteringPane fournie
Ajoutez un DocumentListener sur le modèle du champ de texte permettant de modifier la valeur du filtre du modèle de liste filtrante.
Ajoutez un ItemListener sur la boite combo permettant de changer le filtre du modèle de liste filtrante. Vous utiliserez (après l'avoir complétée) la méthode setCurrentFilterFromSelectedItem.
L'application graphique
Elle est disponible dans la classe PhraseAppli. À la lecture du code (fourni) de cette classe, vous observerez qu'il ne reste plus qu'une méthode à implémenter (populateFilteringPane) pour que l'application soit utilisable.

Le rôle de cette méthode est d'ajouter une à une toutes les phrases qui se trouvent dans le fichier in au modèle de la liste filtrante. Il s'agit d'une opération qui s'exécutera assez rapidement la plupart du temps, mais que je vais vous demander de ralentir exagérément (par appel à delayAction()) pour simuler par exemple la lenteur d'une communication à travers un réseau...

Pour cela, vous pouvez utiliser un SwingWorker dont la tâche de fond lit le fichier ligne par ligne et publie chaque ligne lue quand elle n'est pas vide. L'action exécutée sur EDT suite à publication d'une ligne consiste tout simplement à l'ajouter au bean. L'utilisation d'un SwingWorker dans cette situation est tout à fait indiquée, car la tâche à effectuer est ici très simple et plus facile à mettre en œuvre avec un worker qu'avec un thread.

Un fihcier de phrases alétaoirement générées est fourni avec les ressources (répertoire serie07/data/contenu.txt), mais tout fichier de texte de votre choix fera aussi bien l'affaire.

Travail à faire dans la classe PhraseAppli fournie
Implémentez la méthode populateFilteringPane à l'aide d'un SwingWorker comme indiqué ci-dessus. Vous retarderez chaque lecture de ligne dans le fichier par un appel à la méthode privée delayAction() déjà fournie dans cette classe.
Amélioration du bean FilteringPane
Nous allons rajouter une fonctionnalité supplémentaire à notre composant : en double cliquant sur l'une des lignes affichées, cela modifie la fonte de cette ligne qui devient non grasse et italique. Par exemple, sur l'image ci-dessous, j'ai d'abord recherché les phrases contenant le facteur « graphiquement ». J'ai ensuite double-cliqué sur chacune des phrases obtenues, et elles ont été affichées en italique. Ensuite j'ai recherché les phrases contenant le facteur « phi » (cette recherche englobe la précédente), et voici ce que j'ai obtenu :

applirenderer.png

Pour mener ce travail à bien, il faut étendre la classe StdFilteringListModel dans une nouvelle classe permettant en plus de marquer et démarquer les éléments du modèle. Par exemple, vous pourriez rajouter à vos nouveaux modèles :

un nouvel attribut de type Set pour mémoriser les éléments marqués dans le modèle ;
deux nouvelles méthodes : boolean isMarked(String) et void toggleMark(String), la première indiquant si un élément est marqué dans le modèle, la seconde permettant de marquer un élément s'il ne l'était pas encore ou de le démarquer s'il l'était déjà.
Ensuite, il va falloir afficher différemment les lignes qui ont été marquées de celles qui ne l'ont pas été, et ça, c'est le boulot d'un renderer... Cet objet est une sorte de ListCellRenderer qui gère l'affichage des éléments de la liste, charge à lui de les afficher en italique s'ils sont marqués dans le modèle, ou de les afficher normalement dans le cas contraire.

Il faudra ensuite rectifier le code de FilteringPane afin d'associer un tel renderer à la liste, et de brancher un écouteur de clics souris sur cette même liste pour marquer/démarquer l'élément visé par double-clic. Cela doit fonctionner comme une bascule : un premier double-clic marque la ligne, un second la démarque, et ainsi de suite.

Travail à faire dans cette partie
Codez une nouvelle classe de modèles de listes filtrantes, MarkableFilteringListModel qui étend StdFilterableListModel en respectant les explications données ci-dessus.
Codez une nouvelle classe de renderer, ItalicListCellRenderer, permettant d'afficher en fonte maigre et en italique les éléments donnés pour traitement qui correspondent à des lignes marquées dans le modèle.
Il reste à modifier le code de la classe FilteringPane pour y adapter le modèle de liste filtrante et le contrôleur :
modifiez la section MODELE du constructeur pour utiliser un MarkableFilteringListModel à la place d'un StdFilteringListModel ;
modifiez la section VUE du constructeur pour associer un ItalicListCellRenderer à la liste filtrante ;
complétez la méthode connectControllers pour ajouter un écouteur de double-clics sur les lignes de la liste, qui fait basculer l'état marqué de cette ligne dans le modèle.
