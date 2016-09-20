# TM40507-TDIDT
ID3 (TDIDT) Algorithmus zur Erstellung eines Entscheidungsbaumes.

## Aufruf
Das Programm ist über folgenden Befehl aufzurufen:
```
java -jar tdidt.jar
```
Ohne Parameter wird versucht die Datei _mietkartei.CSV_ als Datenquelle und die Datei _Attribute_Mietkartei.txt_ als Quelle der Attributauflistung zu laden.

Diese Werte können beim Aufruf überschrieben werden, so dass selbst gewählte Datei eingelesen werden können.
Beispiel:
```
# nur die Datei mit den Wohnungs Datensätzen wird überschrieben
java -jar tdidt.jar g001-1.CSV 
# beide Dateien werden überschrieben
java -jar tdidt.jar g001-1.CSV mietkartei_attribute.csv
```

Um die DEBUG Ausgaben (der errechnete Baum, einzelne Ausgabe der Testfälle) zu aktiveren, muss die Umgebungsvariable *TDIDT_DEBUG* auf den Wert *true* gesetz werden.
```
# Windows
set TDIDT_DEBUG=true && java -jar tdidt.jar
# Linux/Unix
TDIDT_DEBUG=true java -jar tdidt.jar
```