package de.dhbw.tree;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.w3c.dom.NamedNodeMap;

import de.dhbw.ml.Main;
import de.dhbw.ml.data.Attribute;
import de.dhbw.ml.data.AttributeSet;
import de.dhbw.ml.data.AttributeValuePair;
import de.dhbw.ml.data.DataItem;
import de.dhbw.ml.data.DataSet;

public class ID3 {
	protected DataSet dataSet;
	protected AttributeSet attribSet;
	
	protected static final double log2Base = Math.log(2);

	public ID3(DataSet dataSet, AttributeSet attribSet) {
		this.dataSet = dataSet;
		this.attribSet = attribSet;
	}

	public Node buildTree() {
		Node n = new Node();

		return buildTree(dataSet, attribSet, n, true);
	}

	public Node buildTree(DataSet ds, AttributeSet as, Node n, boolean defaultClassification) {
		if (ds.getNumberOfExamples() == 0) {
			n.setLeaf(true);
			n.setClassification(defaultClassification);
			return n;
		}
		// test if all belong to same class
		List<DataItem> items = ds.getItems();
		boolean lastClassification = items.get(0).getTeacher();
		boolean sameClassification = true;
		for (int i = 1; i < items.size(); i++) {
			if (items.get(i).getTeacher() != lastClassification) {
				sameClassification = false;
				break;
			}
		}

		if (sameClassification) {
			n.setLeaf(true);
			n.setClassification(lastClassification);
			return n;
		}

//		System.out.println(as.toString());
		
		// add new tree to node
//		Attribute a = selectRandomAttribute(as);
		Attribute a = selectBestAttribute(as, ds);
		n.setAttribute(a.getName());

		String[] attribValues = a.getValues();
		for (String attribValue : attribValues) {
			Node newNode = new Node();
			DataSet dsNode = new DataSet();
			dsNode.setItems(ds.getItemsWithAttribute(a.getName(), attribValue));
			dsNode.updateCounts();
			boolean nodeDefaultClassification = dsNode.getNumberOfPositives() > dsNode.getNumberOfNegatives();
			newNode.setParent(n);
			n.addChildren(attribValue, newNode);
			AttributeSet asClone = null;
			try {
				asClone = as.clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
				System.exit(0);
			}
			buildTree(dsNode, asClone, newNode, nodeDefaultClassification);
		}

		return n;
	}

	protected Attribute selectRandomAttribute(AttributeSet as) {
		int attrNum = ThreadLocalRandom.current().nextInt(as.getNumberOfAttributes() - 1);
		Attribute a = as.getAttributes().get(attrNum);
		as.getAttributes().remove(attrNum);
		as.setNumberOfAttributes(as.getNumberOfAttributes() - 1);
		return a;
	}

	protected Attribute selectBestAttribute(AttributeSet as, DataSet ds) {
		Attribute bestAttribute = null;			//bestes Attribut
		Double bestAttributeGain = 0.0;			//bestes H(A) - H(A|B)
		for (Attribute attr : as.getAttributes()) {
			Double gain = 0.0; //H(A) - H(A|B)
			// alle Attributwerte durchgehen
			for (String attrValue : attr.getValues()){
				// Elemente für die der Wert gesetzt ist
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
				
				// P(Ai | Bk), Ai = positiv oder negative Entscheidung (teacher)
				// Bk = für aktuelle Attribut + Attributwert Kombination
				Double pPosIfVal = numPos / (double) num; 
				Double pNegIfVal = numNeg / (double) num;

				//H(A | Bk) = - SUMi=1..n P(Ai | Bk)log2(P(Ai | Bk))
				Double h = 0.0;
				if(numNeg != num && numPos != num){	//wenn numNeg = num oder numPos = num, dann ist das andere 0 und daher bleibt dann nur -1 * log2(1) = 0	--> h = 0
					h -= pPosIfVal * Math.log(pPosIfVal) / log2Base;
					h -= pNegIfVal * Math.log(pNegIfVal) / log2Base;
				}
				
				//H(A | B) = SUMk=1..m P(Bk) H(A| Bk))
				Double pAttrMatch = num / (double) ds.getNumberOfExamples();
				gain += pAttrMatch * h;
			}
			gain = 1 - gain;	//H(A) - H(A|B) = 1 - H(A|B)
			if(gain > bestAttributeGain){
				bestAttributeGain = gain;
				bestAttribute = attr;
			}
		}
		
		return bestAttribute;
	}

	public boolean classify(Node node, DataItem entry) {
		Node n = node;
		while (!n.isLeaf()) {
			AttributeValuePair avp = entry.getAttributeByName(n.getAttribute());
			n = n.getChild(avp.getValue());
		}
		return n.getClassification();
	}
}
