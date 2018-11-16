// Llamada a un metodo recursivo desde el metodo main

class Main {

	static public void main() {
		int num;
		num = (System.read());
		int fact;
		fact = (factorial(num));
		(System.printI(fact));
	}

	static private int factorial(int num) {
		if (num == 0) {
			return 1;
		} else {
			return num * (factorial(num - 1));
		}
	}

}