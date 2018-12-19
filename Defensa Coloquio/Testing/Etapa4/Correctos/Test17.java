// Uso de un atributo en un metodo sin inicializar en el mismo metodo

class Main {

	static  void main() {}

}

class A {

	private int a;

	dynamic int m1() {
		return a;
	}

}