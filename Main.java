package threadingtest;



public class Main {

	public static void main(String[] args) {
		Thread t0 = new ThreadTest(true);
		Thread t1 = new ThreadTest(false);
		
		t0.start();
		t1.start();
		
		try {
			t0.join();
			t1.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
