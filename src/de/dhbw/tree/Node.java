package de.dhbw.tree;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class Node {
	protected String attribute = null;
	protected HashMap<String, Node> children = new HashMap<>();
	protected Node parent = null;
	protected boolean isLeaf = false;
	protected boolean classification = true;
	
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public HashMap<String, Node> getChildren() {
		return children;
	}
	public void setChildren(HashMap<String, Node> children) {
		this.children = children;
	}
	public void addChildren(String attribValue, Node child){
		this.children.put(attribValue, child);
	}
	public boolean isLeaf() {
		return isLeaf;
	}
	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}
	public boolean getClassification() {
		return classification;
	}
	public void setClassification(boolean classification) {
		this.classification = classification;
	}
	public Node getParent() {
		return parent;
	}
	public void setParent(Node parent) {
		this.parent = parent;
	}
	public boolean isRoot(){
		return parent == null;
	}
	public Node getChild(String value) {
		return children.get(value);
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		if(isRoot()){
			sb.append("(Root) ");
		}
		if(isLeaf()){
			sb.append("(Leaf) ");
		}
		
		sb.append("Attribute: ");
		sb.append(getAttribute());
		
		sb.append(" Classification: ");
		sb.append(classification ? "1" : "0");
		
		sb.append(" Child count: ");
		sb.append(children.size());
		
		return sb.toString();
	}
	
	public void print() {
        print("", true);
    }

    private void print(String prefix, boolean isTail) {
    	if(attribute != null){
    		System.out.println(prefix + attribute + ":");
    	}
    	if(isLeaf){
    		System.out.println(prefix + " --> " + (classification ? "1" : "0"));
    	}
        
        Iterator<Entry<String, Node>> it = children.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, Node> pair = it.next();
            System.out.println(prefix + "  " + pair.getKey());
            if(it.hasNext()){
            	((Node) pair.getValue()).print(prefix + "  |  ", false);
            }else{
            	((Node) pair.getValue()).print(prefix + "  |  ", true);
            }
        }
    }
}
