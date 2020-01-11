package com.sd.dfc.client;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import com.sd.dfc.principal.Menu;

import io.atomix.cluster.MemberId;
import io.atomix.cluster.Node;
import io.atomix.cluster.discovery.BootstrapDiscoveryProvider;
import io.atomix.core.Atomix;
import io.atomix.core.AtomixBuilder;
import io.atomix.utils.net.Address;

public class SocketClient {

	public static final String INSERT = "insert";
	public static final String CREATE = "create";
	public static final String INSERIR = "inserir";
	public static final String READ_ALL = "readall";
	public static final String LER_TODOS = "lertodos";
	public static final String UPDATE = "update";
	public static final String CHANGE = "change";
	public static final String ALTERAR = "alterar";
	public static final String DELETE = "delete";
	public static final String DELETAR = "deletar";

	public static void main(String[] args) {

		int myId = Integer.parseInt(args[0]);
		List<Address> addresses = new LinkedList<>();
		

		for (int i = 2; i < args.length; i += 2) {
			Address address = new Address(args[i], Integer.parseInt(args[i + 1]));
			addresses.add(address);
		}

		AtomixBuilder builder = Atomix.builder();

		Atomix atomix = builder.withMemberId("client-" + myId).withAddress(new Address("127.0.0.1", (6000 + myId)))
				.withMembershipProvider(BootstrapDiscoveryProvider.builder()
						.withNodes(Node.builder().withId("member-0").withAddress(addresses.get(0)).build(),
								Node.builder().withId("member-1").withAddress(addresses.get(1)).build(),
								Node.builder().withId("member-2").withAddress(addresses.get(2)).build())
						.build())
				.build();
		
		atomix.start().join();

		System.out.println("Cluster joined");
		
		Menu menu = new Menu();
		Scanner s = new Scanner(System.in);
		System.out.println(menu.presentMenu());
		while (true) {
			  
			  String text = s.nextLine();
			  if (!(text).equals("sair") && !(text).equals("quit") &&
					  !(text).equals("exit")) {
				  atomix.getCommunicationService().send("message", text,
							MemberId.from("member-" + args[1])).thenAccept(response -> {
								System.out.println((response));
							});
			  } else 
				  break;
		}
	}
}