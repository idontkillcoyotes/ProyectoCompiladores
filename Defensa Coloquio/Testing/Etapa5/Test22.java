// Llamada a un metodo recursivo desde el metodo main
class Main {
	static void main() {
		int num = System.readI();
		int fact;
		fact = factorial(num);
		(System.printI(num));
		(System.printS("!="));
		(System.printIln(fact));
	}
	static int factorial(int num) {
		if (num == 0) {
			return 1;
		} else {
			return num * (factorial(num - 1));
		}
	}

}