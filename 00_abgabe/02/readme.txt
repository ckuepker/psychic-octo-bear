Intelligent Systems SoSe 2015
�bung 02
Armin Pistoor, Christoph K�pker
======
README
======
1. Quellcode liegt in Paketen im Verzeichnis /src
2. Ausf�hren des Programms ist (um Classpath-Probleme zu umgehen) am einfachsten durch die bereitgestellte Jar. Die Abh�ngigkeiten (jade.jar) sollten automatisch durch das Unterverzeichnis /lib aufgel�st werden:
	java -jar ContractNet_Pistoor_Kuepker.jar
3. Nach Start des Programms �ffnet sich wie in der Methode im StudIP beschrieben die GUI von Jade. Hier�ber muss an den Agenten mit dem local name 'admin' eine Nachricht mit dem Inhalt 'ContractNet GO' geschickt werden. Alle anderen Felder der Nachricht sind hierf�r egal.
4. Der Ablauf der Auktionen kann �ber die Konsolenausgabe verfolgt werden.