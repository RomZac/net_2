import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		int M, tau, b;
		boolean show = false;
		show = true;
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);

		System.out.print("M: ");
		M = scanner.nextInt();
		System.out.print("tau: ");
		tau = scanner.nextInt();
		System.out.print("buffer size: ");
		b = scanner.nextInt();

		double probability[] = new double[10]; // вероятности ошибки
		for (int i = 1; i < 10; i++)
			probability[i] = (double) i / 10 ;
 
		for (int p = 0; p < probability.length; p++) {//
			
			double counter = 0;
			
			ArrayList<Integer> list = new ArrayList<>(); // список сообщений
															// 0-1-2-3-4-...-(M-1)
			for (int i = 0; i < M; i++)
				list.add(i);

			ArrayList<Integer> buffer = new ArrayList<>(0); // пустой

			// System.out.println("list: " + list); System.out.println("buf: " +
			// buffer);

			ListIterator<Integer> i = list.listIterator();
			int N = 0, error = 0, j = 0; // N - кол-во попыток, error - была ли
											// ошибка "в серии", j - номер "в
											// пакете"
			
			System.out.println("\nР = " + probability[p]+ "\n");
			while (i.hasNext()) {
				int m = i.next();
				N++;
				//System.out.println("N = " + N);
				if (show) System.out.print( m + " ->");
				if (myRand(probability[p])) { // ошибка
					error++;
					if( show )  System.out.println(" error");
				} else if (error == 0) { // приняли успешно
					i.remove();
					if( show )  System.out.println(" receive");
					if (buffer.size() > 0)
						for (int shift = 1; shift < b + 1; shift++)
							if (!buffer.remove(new Integer(m + shift)))
								/*
								 * если такого элемента не было 
								 * напр, буфер [3, 4], m = 1 тогда примем 1 и не сбрасываем
								 * буфер, т.к. пропущено сообщ. №2
								 */
								break;
				} else if (error != 0) {
					if (buffer.size() < b) { // есть свободное место
						buffer.add(m);
						i.remove(); 
						if( show ) System.out.println(" in buffer");
					}
					else 
						if( show ) System.out.println(" receive + delete");
				}
				
				counter += buffer.size();
				/*
				 * System.out.println(); System.out.println("list: " + list);
				 * System.out.println("buf: " + buffer);//
				 */
				j++;
				if (!i.hasNext() && list.size() > 0) {
					N += tau + 1 - j;
					j = tau + 1;
				}
				if (j == tau + 1) {
					j = 0;
					error = 0;
					i = list.listIterator(); // переместили в начало списка
					// System.out.println("-------------------");
				}
				// если серия по j не кончилась, но сообщений для полной серии
				// не хватает
			}

			/*
			 * System.out.println("\nEND:"); System.out.println("list: " +
			 * list); System.out.println("buf: " + buffer);//
			 */
			System.out.println("-----Result-----");
			System.out.println("N = " + (double) M / N);
			double nReturn = (1 - probability[p]) / (1 + probability[p] * tau); // алг.
																				// с
																				// возвратом

			//System.out.println("cell avg= " + counter / N);
			if ( show ) System.out.println("N(return) = " + nReturn + "\n");
		}
	}

	public static boolean myRand(double p) {
		if (Math.random() <= p)
			return true;
		return false;
	}
}
