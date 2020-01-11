package com.sd.dfc.principal;

public class Menu {

	public String presentMenu() {
		return "-----Command Menu-----\n" + "1 - create [cep, transportadora]\n" + "2 - readall [cep, transportadora]\n" + "3 - update [cep, transportadora]\n" + "4 - delete [cep, transportadora]\n"
				+ "Type 'exit' for quit.";
	}

}
