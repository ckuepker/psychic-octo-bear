Intelligent Systems SoSe 2015
Übung 02
Armin Pistoor, Christoph Küpker
======
README
======
1. Quellcode liegt in Paketen im Verzeichnis /src
2. Ausführen des Programms ist (um Classpath-Probleme zu umgehen) am einfachsten durch die bereitgestellte Jar. Die Abhängigkeiten (jade.jar) sollten automatisch durch das Unterverzeichnis /lib aufgelöst werden:
	java -jar ContractNet_Pistoor_Kuepker.jar
3. Nach Start des Programms öffnet sich wie in der Methode im StudIP beschrieben die GUI von Jade. Hierüber muss an den Agenten mit dem local name 'admin' eine Nachricht mit dem Inhalt 'ContractNet GO' geschickt werden. Alle anderen Felder der Nachricht sind hierfür egal.
4. Der Ablauf der Auktionen kann über die Konsolenausgabe verfolgt werden.