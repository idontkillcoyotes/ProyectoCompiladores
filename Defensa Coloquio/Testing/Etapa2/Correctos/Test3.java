//Varias sentencias que son expresiones entre parentesis dobles
class Clase {

	dynamic void metodo() {
		((a == b));
		((a && b));
		((a || b));
		((a > b));
		(((a || b) || (a || b)));
		((+a));
		((-b));
		((a / b));
		((a + - ! - + b));
		(((a / b) + (a * b)));
		(((a > b) || (b >= a)));
		(((a && b) || (true)));
		((a || (a && b)));
	}

}