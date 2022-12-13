
public class Tree {

	public static void main(String[] sarahIsTheBest) {
		int s = 200000;
		double[] list = new double[s];

		long start = System.currentTimeMillis();
		Tree ts = new Tree();

		System.out.print("The original un sorted array is: ");
		populazer(list);
		System.out.println();

		ts.addingToTheFamily(list);

		long end = System.currentTimeMillis();

		System.out.print("The array sorted by TreeSort is: ");
		ts.organizeFamilyPhoto(ts.parent);
		System.out.println();

		System.out.println();
		
		//timing the sorting
		System.out.println("TreeSort took: " + (end - start) + " milliseconds");

	}
	public static double[] populazer(double[] list) { //randomly populating the double array
		for (int i = 0; i < list.length; i++) {
			list[i] = (int)(Math.random() * list.length);
			System.out.printf(" %6.2f ", list[i]);
		}
		System.out.println();
		return list;
	}

	class FamilyMember { // Creating left and right children and num the value to be held

		double num; 
		FamilyMember leftChild;
		FamilyMember rightChild;

		public FamilyMember(double n) { // setting null to the kids and n to be passed
			num = n;
			leftChild = rightChild = null; //still find this method of assigning weird but it's super practical
		}
	}
	FamilyMember parent;

	Tree() { // constructor. Setting null to parent 
		parent = null;
	}

	// then parent will call method assembleFamilyTree to be set correctly
	void familyTree(double index) {
		parent = assembleFamilyTree(parent, index);
	}

	FamilyMember assembleFamilyTree(FamilyMember parent, double num) {

		//if the tree is empty, create new
		if (parent == null) {
			parent = new FamilyMember(num);
			return parent;
		}

		// if not, call itself down the tree
		if (num <= parent.num) { //placing on the left
			parent.leftChild = assembleFamilyTree(parent.leftChild, num);
		} else if (num >= parent.num) { //placing on the right
			parent.rightChild = assembleFamilyTree(parent.rightChild, num);
		}
		return parent;
	}

	// Organizing in order the members to print (aka Family Photo)
	void organizeFamilyPhoto(FamilyMember parent) { //aka inorder traversal
		if (parent != null) {
			organizeFamilyPhoto(parent.leftChild);
			System.out.printf(" %6.2f ", parent.num);
			organizeFamilyPhoto(parent.rightChild);
		}
	}

	//adding to the array (to the Family)
	void addingToTheFamily(double list[]) {
		for(int i = 0; i < list.length; i++) {
			familyTree(list[i]);
		}
	}

}
