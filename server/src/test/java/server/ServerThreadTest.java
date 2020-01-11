package server;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.sd.dfc.server.ExitException;
import com.sd.dfc.server.ServerThread;

public class ServerThreadTest {

	private CountDownLatch lock = new CountDownLatch(1);

	@Test
	// verifica se a aplicação lida corretamente com os argumentos enviados.
	public void rightArgsTest() throws InterruptedException {

		// valid args, 3 servers, server_id = 0
		String[] args = { "0", "127.0.0.1", "5000", "127.0.0.1", "5001", "127.0.0.1", "5002" };

		MainRunnable mainRunnable = new MainRunnable(args);
		Thread t = new Thread(mainRunnable);
		t.start();
		// se nenhuma exceção foi gerada em 1 segundo, os argumentos foram processados
		// corretamente.
		lock.await(1000, TimeUnit.MILLISECONDS);
	}

	@Test
	// nenhuma exceção é retornada quando faltam argumentos, e o server não se
	// inicia.
	public void wrongArgs1Test() {
		// falta um parametro
		String[] args = { "0", "127.0.0.1", "127.0.0.1", "5001", "127.0.0.1", "5002" };

		MainRunnable mainRunnable = new MainRunnable(args);
		Thread t = new Thread(mainRunnable);
		t.start();
	}

	@Test
	// nenhuma exceção é retornada quando não há argumentos, e o server nao se
	// inicia.
	public void noArgsTest() {
		MainRunnable mainRunnable = new MainRunnable();
		Thread t = new Thread(mainRunnable);
		t.start();
	}
}

//thread para rodar a main separadamente do teste, para poder ser interrompido se necessário.
class MainRunnable implements Runnable {
	
	//parâmetro args da main nunca pode ser nulo!
	private String[] args = {};

	public MainRunnable(String[] args) {
		this.args = args;
	}

	public MainRunnable() {
	}

	public void run() {
		try {
			ServerThread.main(args);
		} catch (ExitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}