package com.sd.dfc.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import io.atomix.core.counter.AtomicCounter;

public class Database2 extends ArchiveManipulationImpl {

    private Map<BigInteger, byte[]> map = new HashMap<>();
    private AtomicCounter count;

    public Database2(String filename) {
        this.recoverData(filename);
    }

    public Database2(AtomicCounter counter, Map<BigInteger, byte[]> map) {
    	this.map = map;
    	this.count = counter;
    }
    
    public Map<BigInteger, byte[]> getMap(){
    	return this.map;
    }

    // insere o vetor de bytes e retorna o id mapeado para o mesmo
    public long create(byte[] value) {
        map.put(BigInteger.valueOf(count.get()), value);
        return count.getAndAdd(1);
    }

    public byte[] read(BigInteger id) {
        return map.get(id);
    }

    public Map<BigInteger, byte[]> readAll() {
        return map;
    }

    public byte[] update(BigInteger id, byte[] value) {
        return map.put(id, value);
    }

    public byte[] delete(BigInteger id) {
        return map.remove(id);
    }

    private void recoverData(String fileName) {

        try{
            FileReader file = new FileReader(fileName);
            BufferedReader readFile = new BufferedReader(file);
            String line;
            String[] splittedCommand;
            while((line = readFile.readLine())!=null){
                splittedCommand = line.split(" ");

                //lista com o comando subtraido do método e do nome do arquivo
                List<String> splittedList = Arrays.asList(splittedCommand);

                switch (splittedCommand[0]){
                    case "create":
                        this.create(String.join(" ", splittedList.subList(2, splittedList.size())).getBytes());
                        break;
                    case "update":
                        this.update(BigInteger.valueOf(Long.parseLong(splittedList.get(2))), String.join(" ", splittedList.subList(3,splittedList.size())).getBytes());
                        break;
                    case "delete":
                        this.delete(BigInteger.valueOf(Long.parseLong(splittedList.get(2))));
                        break;
                }
            }

        }catch (Exception e){
            System.err.println(Arrays.toString(e.getStackTrace()));
        }
    }
}
