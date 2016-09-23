package de.dhbw.tree;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import de.dhbw.ml.data.Attribute;
import de.dhbw.ml.data.AttributeSet;
import de.dhbw.ml.data.AttributeValuePair;
import de.dhbw.ml.data.DataItem;
import de.dhbw.ml.data.DataSet;

/**
 * Klasse zum Durchf�hren des ID3/TDIDT Algorithmus
 * @author Michael
 *
 */
public class ID3 {
	/**
	 * Trainings-Datens�tze
	 */
	protected DataSet dataSet;
	/**
	 * Alle Attribute
	 */
	protected AttributeSet attribSet;
	
	/**
	 * der Wert von log(2) wird mehrmals ben�tigt, es reicht aber diesen einmal zu berechnen
	 */
	protected static final double log2Base = Math.log(2);

	public ID3(DataSet dataSet, AttributeSet attribSet) {
		this.dataSet = dataSet;
		this.attribSet = attribSet;
	}

	/**
	 * erstellt den Entscheidungsbaum mit Hilfe der Instanzvariablen dataSet und attribSet
	 * @return
	 */
	public Node buildTree() {
		//Root Knoten
		Node n = new Node();

		//defaultClassification entspricht der h�ufigeren Klasse im dataSet
		return buildTree(dataSet, attribSet, n, dataSet.getNumberOfPositives() > dataSet.getNumberOfNegatives());
	}

	/**
	 * Erweitert den Entscheidungsbaum anhand der gegebenen Datens�tze und Attribute
	 * @param ds	Datens�tze
	 * @param as	Attribute
	 * @param n		Aktueller Knoten (wird entweder als Blatt markiert oder es werden weitere Knoten angeh�ngt)
	 * @param defaultClassification		Wert der Klassifikation f�r diesen Knoten
	 * @return
	 */
	public Node buildTree(DataSet ds, AttributeSet as, Node n, boolean defaultClassification) {
		//Wenn es keine Datens�tze mehr gibt ist der Knoten ein Blatt und erh�lt den defaultClassification Wert
		if (ds.getNumberOfExamples() == 0) {
			n.setLeaf(true);
			n.setClassification(defaultClassification);
			//Dieser Zweig des Baumes ist dann fertig
			return n;
		}
		//Geh�ren alle verbleibenden Datens�tze zur gleichen Klasse?
		List<DataItem> items = ds.getItems();
		boolean lastClassification = items.get(0).getTeacher();
		boolean sameClassification = true;
		for (int i = 1; i < items.size(); i++) {
			if (items.get(i).getTeacher() != lastClassification) {
				sameClassification = false;
				break;
			}
		}

		//Wenn ja, dann ist der Zwei hier auch zu Ende. Werte setzen und fertig.
		if (sameClassification) {
			n.setLeaf(true);
			n.setClassification(lastClassification);
			return n;
		}

		//Wenn nicht, dann muss der Baum erweitert werden
		//dazu muss das n�chste Attribut ausgew�hlt werden
		
		//Variante 1: Attribut zuf�llig w�hlen
//		Attribute a = selectRandomAttribute(as);
		
		//Variante 2: Attribut mit dem h�chsten Informationsgewinn
		Attribute a = selectBestAttribute(as, ds);
		
		//Attribut am Knoten speichern
		n.setAttribute(a.getName());

		//Alle Werte durchgehen und neue Kindknoten erstellen
		String[] attribValues = a.getValues();
		for (String attribValue : attribValues) {
			//Kindknoten erstellen
			Node newNode = new Node();
			//Datens�tze f�r Kindknoten ermitteln (passender Attributwert)
			DataSet dsNode = new DataSet();
			dsNode.setItems(ds.getItemsWithAttribute(a.getName(), attribValue));
			dsNode.updateCounts();
			//vorherrschende Klassifikation des Datensatzes ermitteln
			boolean nodeDefaultClassification = dsNode.getNumberOfPositives() > dsNode.getNumberOfNegatives();
			//Knoten in Baum einh�ngen
			newNode.setParent(n);
			n.addChildren(attribValue, newNode);
			//Attribut Datenstruktur klonen
			AttributeSet asClone = null;
			try {
				asClone = as.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
				System.exit(0);
			}
			//Rekursiver Aufruf zur Ermittlung der weiteren Kindknoten dieses neuen Zweiges
			buildTree(dsNode, asClone, newNode, nodeDefaultClassification);
		}

		//Knoten zur�ckgeben
		return n;
	}

	/**
	 * W�hlt ein zuf�lliges Attribut aus den gegebenen Attributen
	 * @param as
	 * @return
	 */
	protected Attribute selectRandomAttribute(AttributeSet as) {
		int attrNum = ThreadLocalRandom.current().nextInt(as.getNumberOfAttributes() - 1);
		Attribute a = as.getAttributes().get(attrNum);
		as.getAttributes().remove(attrNum);
		as.setNumberOfAttributes(as.getNumberOfAttributes() - 1);
		return a;
	}

	/**
	 * W�hlt das Attribut mit dem h�chsten Informationsgewinn aus den gegebenen Attributen
	 * @param as
	 * @param ds
	 * @return
	 */
	protected Attribute selectBestAttribute(AttributeSet as, DataSet ds) {
		Attribute bestAttribute = null;			//bestes Attribut
		Double bestAttributeGain = 0.0;			//bestes H(A) - H(A | B)
		//Alle Attribute durchgehen
		for (Attribute attr : as.getAttributes()) {
			Double gain = 0.0; //H(A) - H(A | B)
			// alle Attributwerte durchgehen
			for (String attrValue : attr.getValues()){
				// Elemente f�r die der Wert gesetzt ist
				List<DataItem> attrItems = ds.getItemsWithAttribute(attr.getName(), attrValue);
				int num = attrItems.size();	//Anzahl der passenden Elemente
				if(num == 0){
					continue;	//skip, auslassen wenn keine Elemente vorhanden sind
				}
				int numPos = 0;	//Anzahl der positiven Elemente
				int numNeg = 0;	//Anzahl der negativen Elemente
				for(DataItem di : attrItems){
					if(di.getTeacher()){
						numPos++;
					}
				}
				numNeg = num - numPos;
				
				// P(A_i | B_k), A_i = positiv oder negative Entscheidung (teacher)
				// B_k = f�r aktuelle Attribut + Attributwert Kombination
				Double pPosIfVal = numPos / (double) num; 	//Wahrscheinlichkeit f�r positive Entscheidung
				Double pNegIfVal = numNeg / (double) num;	//Wahrscheinlichkeit f�r negative Entscheidung

				//H(A | B_k) = - SUM_i=1..n P(A_i | B_k)log2(P(A_i | B_k))
				//hier n=2, da wir nur ja oder nein als Klassen haben
				Double h = 0.0;
				if(numNeg != num && numPos != num){	//wenn numNeg = num oder numPos = num, dann ist das andere 0 und daher bleibt dann nur -1 * log2(1) = 0	--> h = 0
					h -= pPosIfVal * Math.log(pPosIfVal) / log2Base;	//i=1: ja
					h -= pNegIfVal * Math.log(pNegIfVal) / log2Base;	//i=2: nein
				}
				
				//H(A | B) = SUM_k=1..m P(B_k) H(A | B_k))
				Double pAttrMatch = num / (double) ds.getNumberOfExamples();	//P(B_k)
				//da wir die Summe aller Attributwerte haben wollen, wird dieser Wert zu gain addiert
				gain += pAttrMatch * h;	//P(B_k) * H(A | B_k)
			}
			gain = 1 - gain;	//H(A) - H(A | B) = 1 - H(A | B)
			//Ist dieser Attribut besser als alle bisherigen?
			if(gain > bestAttributeGain){
				bestAttributeGain = gain;
				bestAttribute = attr;
			}
		}
		
		//Bestes Attribut mit dem h�chsten Informationsgewinn
		return bestAttribute;
	}

	/**
	 * Ermittelt die Klassifikation f�r einen Datensatz anhand des Entscheidungsbaumes
	 * @param node
	 * @param entry
	 * @return
	 */
	public boolean classify(Node node, DataItem entry) {
		Node n = node;
		//Baum solange durchgehen bis man an einem Blatt angekommen ist. Klasse des Blatts ist dann die Klasse des Datensatzes
		while (!n.isLeaf()) {
			AttributeValuePair avp = entry.getAttributeByName(n.getAttribute());
			n = n.getChild(avp.getValue());
		}
		return n.getClassification();
	}
}
