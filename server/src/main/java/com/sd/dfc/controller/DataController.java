package com.sd.dfc.controller;

import java.io.IOException;

public interface DataController {
    //insere o dado no database e escreve o comando no arquivo
    boolean insert(String[] splittedList) throws IOException;

    //le o dado no database indicado
    String readAll(String[] splittedMessage);

    //atualiza o dado no database e escreve o comando no arquivo
    byte[] update(String[] splittedMessage) throws IOException;

    //remove o dado no database e escreve o comando no arquivo
    byte[] delete(String[] splittedMessage) throws IOException;

    //verifica se é um comando válido. se for, retorna em qual base deve atuar.
    String validCommand(String input);

    //verifica o que o comando deseja fazer, em qual banco deseja, o faz, e retorna a resposta do chamado.
    String putData(String data) throws IOException;
}
