Intelligent Systems SoSe 2015
�bung 05
Armin Pistoor, Christoph K�pker
======
README
======
1. Quellcode liegt in Paketen im Verzeichnis /src. Das relevante Paket f�r diese Abgabe ist de.unioldenburg.jade.scheduling
2. Ausf�hren des Programms ist (um Classpath-Probleme zu umgehen) am einfachsten durch die bereitgestellte Jar. Die Abh�ngigkeiten (jade.jar) sollten automatisch durch das Unterverzeichnis /lib aufgel�st werden:
	java -jar ProcessPlanningProblem_PistoorKuepker.jar
3. Die Ausgabe der Planung erfolgt �ber die Konsole

Anmerkungen:
* Aus Zeitknappheit ist uns die Umsetzung des Constraints "Alle Produkte werden in korrekter Reihenfolge produziert" nicht mehr m�glich gewesen. Der Scheduler arbeitet aber genau nach diesem Prinzip