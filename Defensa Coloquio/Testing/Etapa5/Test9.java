// El metodo main con if anidados

class Main {

	static void main() {
		int a, b, c;
		a = System.readI();
		b = System.readI();
		c = System.readI();//se leen todos los enteros de una vez
		//hay que ingresar tres enteros la primera vez
		if (c < 5) {
			(System.printSln("c es menor a 5"));
			if (a > b) {
				(System.printSln("a es mayor que b"));
			} else {
				(System.printSln("a es menor o igual que b"));
			}
		} else {
			(System.printSln("c no es menor a 5"));
		}
		(System.printIln(a));
		(System.printIln(b));
		(System.printIln(c));
	}

}