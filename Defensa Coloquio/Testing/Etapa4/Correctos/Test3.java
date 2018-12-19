// Uso de una variable local que tapa un atributo

class Main {

	static  void main() {}

}

class Clase {

	private char a;

	dynamic int m1() {
		int a;
		a = 1;
		return a;
	}

}