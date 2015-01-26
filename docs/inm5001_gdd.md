I. Déroulement général du jeu :

Lancement du logiciel:

	Ouvre le GUI: Menu principal.

Déroulement d'un niveau:

1. Scène pré combat (dialogue textuel et animations scriptées)
2. Combat
3. Scène post combat (dialogue textuel et animations scriptées)
4. GUI : Fin de niveau


II. Éléments visuels pour l'utilisateur :

Descriptions des GUIs :

1. Menu principal :
1. New game :
Commence une nouvelle partie au niveau 1, sans perdre la partie sauvegardée.
2. Load game :
Continuer le jeu à partir du dernier niveau atteint.
3. Exit :
Fermer l'application.

2. Écran de jeu :
1. Bannière des tours :
Situé sur la bordure gauche de l'écran de façon permanente.  

Durant le combat, elle contient X portraits des personnages et monstres actuellement (en vie) dans l'espace de jeu et l'ordre des portraits correspond à l'ordre des tours (actuel et prochains).  Sous chaque portrait, il y a le nom de la créature ainsi qu'une barre chiffrée indiquant ses vies.  

En dehors du combat, la bannière contient X portraits vides, chacun sans nom et sans barre de vies.

2. Bannière des personnages :
Situé sur la bordure droite de l'écran de façon permanente.  Contient jusqu'à trois panneaux de personnage avec les éléments suivants :
1. Portrait du personnage
2. Health (barre chiffrée)
3. Energy (barre chiffrée)
4. 4 cases de Skills (boutons pour des habilités; peuvent avoir une image vide)
5. Move (bouton) 
6. End turn (bouton) 

3. Espace de jeu / le niveau :
Situé dans le centre de l'écran, de façon permanente.  L'action du jeu se passe ici.

4. Espace noir :
Simple couverture en noir de l'espace vide entre le niveau et les bannières de bord.

5. Boîte de dialogue :
Apparaît durant les scènes de dialogue avant et après le combat.  Situé au bas centre de l'écran, par dessus le niveau.  Contient le bouton « Next » pour faire avancer le dialogue.

6. Nom et vies des créatures :
		[ TODO ] 

3. Sommaire du niveau :
1. Nouvelles armes/habilités découvertes
2. Save and continue
3. Continue without saving
4. Exit

III. Système de combat :

Résumé :

Le combat se déroule à tour de rôle entre les différentes créatures (personnages et monstres) en vie dans l'espace de jeu.  À chaque tour, seulement une créature est permise de faire des actions, quelle soit contrôlée ou non par l'utilisateur.  Chaque créature est alloué un certain montant d'énergie par tour et toute action consomme de l'énergie, donc le contrôleur de la créature ne peut choisir qu'un nombre limité d'actions pendant le tour.  Quand l'utilisateur veut terminer le tour d'un de ses personnages, il doit explicitement choisir le bouton correspondant dans le panneau du personnage approprié; le déroulement des tours des monstres sera automatisé.


Condition de victoire :

Le combat est gagné pour l'utilisateur s'il ne reste plus de monstres vivants dans le niveau.  Dans ce cas, le combat se termine et donne lieu à une scène de dialogue et d'animation dans l'espace de jeu.


Condition de défaite :

L'utilisateur perd le combat s'il ne reste plus de personnages en vie dans son équipe.  Dans ce cas, le combat se termine et l'utilisateur retourne au GUI:Menu principale.


Jouer son tour (pour l'utilisateur) :

Avant de faire une action :

En plus de la disposition des créatures dans le niveau, l'utilisateur peut consulter plusieurs autres sources visuelles d'information avant de décider de faire une action avec un personnage.  En particulier :
La bannière des tours indique l'ordre des tours (courant et futurs) des créatures dans le niveau
Le nombre de vies (health) de tous les personnages (dont le personnage courant) sont affichés dans leurs panneaux correspondant
Le nombre de vies des monstres est affiché à la fois dans l'espace de jeu et dans la bannière des tours, ainsi que le nom des monstres (afin de facilité leur identification)
Le montant d’énergie du personnage courant est affiché dans son panneau correspondant

Actions :

Durant le tour d'un de ses personnage, l'utilisateur peut décider les actions de celui-ci en utilisant le panneau de personnage correspondant.  Comme chaque action consomme de l’énergie, l’utilisateur doit judicieusement choisir une ou plusieurs actions durant le tour.  Il doit aussi prendre en considération les vies de chaque ennemi, qui se retrouveront sous la forme d'une barre chiffrée à la fois en dessous de l'ennemi dans l'espace de jeu et en dessous du portrait correspondant dans la bannière des tours.

Le panneau du personnage contient tous les actions possibles durant le tour, dont :

1. Move :
Permet de déplacer le personnage d'un certain nombre de cases dans le terrain du niveau .  

Après un click de la souris sur le bouton Move, les cases auxquelles le personnage peut se rendre seront illuminés graphiquement dans l'espace de jeu.

Un click subséquent dans une des cases illuminées déplaceront ce personnage, et un click sur un autre bouton annulera l'illumination des cases (aucun déplacement du personnage).

2. End Turn :
Termine le tour courant, et donc le tour passe à la prochaine créature (contrôlé ou non par l'utilisateur) dans la bannière des tours.

3. Skill (1, 2, 3 ou 4)
Permet d'effectuer une attaque ou habilité spéciale d'un personnage.

	Après un click de la souris sur le bouton du Skill, les cases auxquelles le skill peut être 	appliquer seront illuminés graphiquement dans l'espace de jeu.

	Un click subséquent dans une des cases illuminées effectueront le skill, et un click sur un 	autre bouton annulera l'illumination des cases (aucune utilisation du skill).

IV. Attributs des créatures (personnages et monstres)

Chaque créature dans le jeu auront des valeurs précises pour les attributs suivants:

Health (nombre entier):
Représente le nombre de vies de la créature (la créature est éliminé à 0 vies)

Energy (nombre entier):
Limite les actions possibles pour la créature durant son tour.  Une créature doit toujours pouvoir payer le coût d'une action en energie pour l'effectuer.

Speed (nombre entier):
Détermine la position de la créature dans l'ordre des tours, ainsi que sa fréquence dans les tours.  En effet, si une créature est beaucoup plus rapide qu'une deuxième, il est possible que la première créature puisse jouer 2 tours avant même que la deuxième en joue un.

Defense (nombre réelle):
	Facteur de réduction appliqué au dommage que subit une créature lorsqu'elle est attaquée.

Attack (nombre réelle):
	Facteur d'augmentation appliqué au dommage qu'inflige une créature lorsqu'elle attaque une 	autre.

Level (nombre entier):
	Indique un rang auquel seront indexés les autres attributs d'une créature.

Skill 1:
	Une habilitée particulère de la créature, dont les habilités pour attaquer.

Skill 2, Skill 3, Skill 4:
Idem que Skill 1.


V. Niveaux, Personnages, Armes, Skills, Monstres

Les niveaux auront des dimensions de 8x8 cases.  

Pour plus de détails sur les niveaux, les personnages, les armes, les skills ou les monstres, consulter les images jpeg scannées dans le répertoire ''2015-01-21 INM 5001 G30 Projet'' dans Dropbox.  