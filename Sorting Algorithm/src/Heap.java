
public class Heap {
	//s -> size of array to populate
	//size -> size of array but with array.length to be easier to change in the future
	//list -> array of doubles to be sorted by HeapSort method
	//index -> the index/position of each element to be compared and then arranged to in sorting order

	public static void main(String[] sarahTeachSecondYearPlease) {
		int s = 1000;
		double[] list = new double[s];

		System.out.print("The original un sorted array is: ");
		populazer(list);
		System.out.println();

		int size = list.length;
		long start = System.currentTimeMillis();
		Heap hp = new Heap();
		hp.sorting(list);
		long end = System.currentTimeMillis();
		
		System.out.print("The index of each element is:    ");
		printPosition(list);
		System.out.println();

		System.out.print("The array sorted by HeapSort is: ");
		print(list);
		
		System.out.println();
		
		//timing the sorting
		System.out.println("HeapSort took: " + (end - start) + " milliseconds");

	}
	public static double[] populazer(double[] list) { //randomly populating the double array
		for (int i = 0; i < list.length; i++) {
			list[i] = (int)(Math.random() * list.length);
			System.out.printf(" %6.2f ", list[i]);
		}
		System.out.println();
		return list;
	}
	public static double[] print(double[] list) { //printiing the array
		for (int i = 0; i < list.length; i++) {
			System.out.printf(" %6.2f ", list[i]);
		}
		System.out.println();
		return list;
	}
	public static void printPosition(double[] list) { //printing the index of each element
		int position = 0;
		for (int i = 0; i < list.length; i++) {
			System.out.printf(" %6d ", position);
			position++;
		}
		System.out.println();
	}
	public void heapify(double[] list, int size, int index) { //comparing root node with its children (left and right)
		int largest = index; // Initialize largest as root parent
		int left = 2 * index + 1; // getting the left child
		int right = 2 * index + 2; // getting the right child


		if (left < size && list[left] > list[largest]) { // If left child is larger than parent node
			largest = left; //then parent will get the left child's value
		}

		if (right < size && list[right] > list[largest]) { // If right child is larger than parent 
			largest = right; //then parent will get the right child's value
		}

		if (largest != index) { // If largest is not root (positioned at index)
			//we create a temp variable (swap) to hold the value and then change them
			double swap = list[index];
			list[index] = list[largest];
			list[largest] = swap;

			// Call heapify recursively until each /////////// the affected sub-tree
			heapify(list, size, largest);
		}
	}
	public void sorting(double[] list) {
		int size = list.length;

		// Going through each index and then calling heapify to check their values and swap them accordingly (build tree)
		for (int i = size / 2 - 1; i >= 0; i--) {
			heapify(list, size, i);
		}
		
		// When largest value reaches the root, place them in the end of the array and take them out of heapify
		for (int i = size - 1; i > 0; i--) {
			// Move current root to end
			// create a temp to store their value
			double temp = list[0];
			list[0] = list[i];
			list[i] = temp;

			// Call heapify on the reduced array (without the largest root removed above)
			heapify(list, i, 0);
		}
	}
}
