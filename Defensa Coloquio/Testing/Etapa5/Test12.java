// El metodo main con while anidados
class Main {
	static void main() {
		int i, j, n;
		i = 1;
		j = 1;
		n = 10;
		while (i < n) {
			(System.printS("Tabla del "));
			(System.printI(i));
			(System.printSln(":"));
			while (j < n) {
				(System.printI(i));
				(System.printS(" x "));
				(System.printI(j));
				(System.printS(" = "));
				(System.printIln(i * j));
				j = j + 1;
			}
			if (i < n - 1) {
				(System.printSln(" "));
			}
			j = 1;
			i = i + 1;
		}
	}

}