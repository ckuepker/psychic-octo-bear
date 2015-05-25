Intelligent Systems SoSe 2015
Übung 05
Armin Pistoor, Christoph Küpker
======
README
======
1. Quellcode liegt in Paketen im Verzeichnis /src. Das relevante Paket für diese Abgabe ist de.unioldenburg.jade.scheduling
2. Ausführen des Programms ist (um Classpath-Probleme zu umgehen) am einfachsten durch die bereitgestellte Jar. Die Abhängigkeiten (jade.jar) sollten automatisch durch das Unterverzeichnis /lib aufgelöst werden:
	java -jar ProcessPlanningProblem_PistoorKuepker.jar
3. Die Ausgabe der Planung erfolgt über die Konsole

Anmerkungen:
* Aus Zeitknappheit ist uns die Umsetzung des Constraints "Alle Produkte werden in korrekter Reihenfolge produziert" nicht mehr möglich gewesen. Der Scheduler arbeitet aber genau nach diesem Prinzip