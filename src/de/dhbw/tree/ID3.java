package de.dhbw.tree;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.w3c.dom.NamedNodeMap;

import de.dhbw.ml.data.Attribute;
import de.dhbw.ml.data.AttributeSet;
import de.dhbw.ml.data.AttributeValuePair;
import de.dhbw.ml.data.DataItem;
import de.dhbw.ml.data.DataSet;

public class ID3 {
	protected DataSet dataSet;
	protected AttributeSet attribSet;

	public ID3(DataSet dataSet, AttributeSet attribSet) {
		this.dataSet = dataSet;
		this.attribSet = attribSet;
	}
	
	public Node buildTree(){
		Node n = new Node();
		
		return buildTree(dataSet, attribSet, n, true);
	}
	
	public Node buildTree(DataSet ds, AttributeSet as, Node n, boolean defaultClassification){
		if(ds.getNumberOfExamples() == 0){
			n.setLeaf(true);
			n.setClassification(defaultClassification);
			return n;
		}
		//test if all belong to same class
		List<DataItem> items = ds.getItems();
		boolean lastClassification = items.get(0).getTeacher();
		boolean sameClassification = true;
		for(int i = 1; i < items.size(); i++){
			if(items.get(i).getTeacher() != lastClassification){
				sameClassification = false;
				break;
			}
		}
		
		if(sameClassification){
			n.setLeaf(true);
			n.setClassification(lastClassification);
			return n;
		}

		//add new tree to node
		//TODO get best attribute instead of first one
		System.out.println(as.toString());
		int attrNum = ThreadLocalRandom.current().nextInt(as.getNumberOfAttributes() - 1);
		Attribute a = as.getAttributes().get(attrNum);
		as.getAttributes().remove(attrNum);
		as.setNumberOfAttributes(as.getNumberOfAttributes() - 1);
		n.setAttribute(a.getName());
		
		String[] attribValues = a.getValues();
		for(String attribValue : attribValues){
			Node newNode = new Node();
			DataSet dsNode = new DataSet();
			dsNode.setItems(ds.getItemsWithAttribute(a.getName(), attribValue));
			dsNode.updateCounts();
			boolean nodeDefaultClassification = dsNode.getNumberOfPositives() > dsNode.getNumberOfNegatives();
			newNode.setParent(n);
			n.addChildren(attribValue, newNode);
			AttributeSet asClone = null;
			try{
				asClone = as.clone();
			}catch(CloneNotSupportedException e){
				e.printStackTrace();
				System.exit(0);
			}
			buildTree(dsNode, asClone, newNode, nodeDefaultClassification);
		}
		
		return n;
	}
	
	public boolean classify(Node node, DataItem entry){
		Node n = node;
		while(!n.isLeaf()){
			AttributeValuePair avp = entry.getAttributeByName(n.getAttribute());
			n = n.getChild(avp.getValue());
		}
		return n.getClassification();
	}
}
