package com.sd.dfc.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.sd.dfc.data.ArchiveManipulation;
import com.sd.dfc.data.ArchiveManipulationImpl;
import com.sd.dfc.data.Database2;
import com.sd.dfc.model.Ceps;
import com.sd.dfc.model.Transportadora;
import com.sd.dfc.server.ServerThread;

import io.atomix.core.counter.AtomicCounter;

public class DataControllerImpl implements DataController {
	private String name;

	private ArchiveManipulation archive = new ArchiveManipulationImpl(name + ".txt");

	private Database2 map;

	public DataControllerImpl(String name, Map<BigInteger, byte[]> map, AtomicCounter counter) {
		this.name = name;
		this.map = new Database2(counter, map);
	}

	public void setDatabase(AtomicCounter counter, Map<BigInteger, byte[]> map) {
		this.map = new Database2(counter, map);
	}

	public Map<BigInteger, byte[]> getDatabase() {
		return this.map.getMap();
	}

	@Override
	public boolean insert(String[] splittedMessage) throws IOException {
		try {
			List<String> splittedList = new ArrayList<>(Arrays.asList(splittedMessage));
			this.map.create(String.join(" ", splittedList.subList(2, splittedList.size())).getBytes());
			archive.write(String.join(" ", splittedList));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public String readAll(String[] splittedMessage) {
		Map<BigInteger, byte[]> map;
		StringBuilder result = new StringBuilder();

		map = this.map.readAll();
		if (splittedMessage[1].equals("cep")) {
			for (Map.Entry<BigInteger, byte[]> entry : map.entrySet()) {
				String[] values = new String(entry.getValue()).split(" ");
				Ceps ceps = new Ceps(Long.parseLong(entry.getKey().toString()), Long.parseLong(values[0]),
						Long.parseLong(values[1]));
				result.append(ceps.getId()).append(": de ").append(ceps.getCepInicio()).append(" até ")
						.append(ceps.getCepFim()).append(", ");
			}
		} else if (splittedMessage[1].equals("transportadora")) {

			for (Map.Entry<BigInteger, byte[]> entry : map.entrySet()) {
				String[] transportadoraValues = new String(entry.getValue()).split(" ");

				Transportadora transportadora = new Transportadora(Long.parseLong(entry.getKey().toString()),
						transportadoraValues[0], new Ceps(), Double.parseDouble(transportadoraValues[2]));

				result.append(transportadora.getId()).append(": ").append(transportadora.getNome()).append(", peso ")
						.append(transportadora.getPeso()).append(" e abrangência de id ")
						.append(transportadoraValues[1]).append(", ");
			}
		}
		return result.toString();
	}

	@Override
	public byte[] update(String[] splittedMessage) throws IOException {
		List<String> splittedList = new ArrayList<>(Arrays.asList(splittedMessage));

		if (splittedMessage[1].equals("cep")) {
			archive.write(String.join(" ", splittedList));
			return this.map.update(BigInteger.valueOf(Long.parseLong(splittedList.get(2))),
					String.join(" ", splittedList.subList(3, splittedList.size())).getBytes());
		} else if (splittedMessage[1].equals("transportadora")) {
			archive.write(String.join(" ", splittedList));
			return this.map.update(BigInteger.valueOf(Long.parseLong(splittedList.get(2))),
					String.join(" ", splittedList.subList(3, splittedList.size())).getBytes());
		}
		return null;
	}

	@Override
	public byte[] delete(String[] splittedMessage) throws IOException {
		List<String> splittedList = new ArrayList<>(Arrays.asList(splittedMessage));
		archive.write(String.join(" ", splittedList));
		return this.map.delete(BigInteger.valueOf(Long.parseLong(splittedList.get(2))));
	}

	@Override
	public String validCommand(String input) {
		List<String> validCommands;
		validCommands = Arrays.asList(
				// create
				ServerThread.INSERT, ServerThread.CREATE, ServerThread.INSERIR,
				// read all
				ServerThread.READ_ALL, ServerThread.LER_TODOS,
				// update
				ServerThread.UPDATE, ServerThread.CHANGE, ServerThread.ALTERAR,
				// delete
				ServerThread.DELETE, ServerThread.DELETAR);

		if (validCommands.stream().anyMatch(str -> str.trim().equals(input.split(" ")[0]))) {
			String[] splittedCommand = input.split(" ");
			List<String> splittedList = new ArrayList<>(Arrays.asList(splittedCommand));

			// command has sufficient parameters?
			switch (splittedList.get(0).toLowerCase()) {
			case ServerThread.INSERT:
			case ServerThread.CREATE:
			case ServerThread.INSERIR:
				if ((splittedList.size() == 4 && (splittedCommand[1].equals("cep"))
						|| (splittedList.size() == 5 && splittedCommand[1].equals("transportadora")))) {
					// retorna qual database comando atuará
					return splittedCommand[1];
				}
			case ServerThread.READ_ALL:
			case ServerThread.LER_TODOS:
				if (splittedList.size() == 2
						&& (splittedCommand[1].equals("cep") || splittedCommand[1].equals("transportadora"))) {
					// retorna qual database comando atuará
					return splittedCommand[1];
				}
			case ServerThread.ALTERAR:
			case ServerThread.CHANGE:
			case ServerThread.UPDATE:

				if (!((splittedList.size() == 5 && (splittedCommand[1].equals("cep")) || (splittedList.size() == 6 && splittedCommand[1].equals("transportadora")))))
					return null;

				// o segundo parâmetro deve poder ser convertido para float
				try {
					Long.parseLong(splittedCommand[2]);
				} catch (Exception e) {
					return null;
				}

				return splittedCommand[1];

			case ServerThread.DELETE:
			case ServerThread.DELETAR:
				if (!(splittedList.size() == 3
						&& (splittedCommand[1].equals("cep") || splittedCommand[1].equals("transportadora"))))
					return null;
				// o segundo parâmetro deve poder ser convertido para float
				try {
					Long.parseLong(splittedCommand[2]);
				} catch (Exception e) {
					return null;
				}

				return splittedCommand[1];

			default:
				return null;
			}
		}
		return null;
	}

	@Override
	public String putData(String data) {
		String[] splittedMessage = data.split(" ");

		byte[] response = null;
		switch (splittedMessage[0]) {
		case ServerThread.INSERT:
		case ServerThread.CREATE:
		case ServerThread.INSERIR:
			try {
				if (this.insert(splittedMessage)) {
					return "Message inserted: " + String.join(" ", splittedMessage);
				} else {
					return "Fail on insert message";
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case ServerThread.CHANGE:
		case ServerThread.ALTERAR:
		case ServerThread.UPDATE:
			try {
				response = this.update(splittedMessage);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (response != null) {
				return ("Previous message: " + new String(response) + ". Message updated!");
			} else {
				return "Fail on update message.";
			}
		case ServerThread.DELETAR:
		case ServerThread.DELETE:
			try {
				response = this.delete(splittedMessage);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (response != null) {
				return ("Message removed: " + new String(response));
			} else {
				return "Fail on removing item.";
			}
		case ServerThread.READ_ALL:
		case ServerThread.LER_TODOS:
			String returnedData = this.readAll(splittedMessage);

			// remove the last comma
			if (returnedData.length() != 0) {
				return returnedData.substring(0, returnedData.length() - 2);
			} else {
				return "Database vazio";
			}
		default:
			break;
		}
		return null;
	}

}
