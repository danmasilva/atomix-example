package com.sd.dfc.data;


import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.IOException;


public class ArchiveManipulationImpl implements ArchiveManipulation {

    ArchiveManipulationImpl(){}

    private String dataSource = "";
    //static private Map<BigInteger, byte[]> map = new HashMap<>();

    public ArchiveManipulationImpl(String dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public synchronized void write(String text) throws IOException {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream( dataSource, true)))) {
            writer.append(text).append("\n");
        }
    }

    @Override
    public String getDataSource() {
        return dataSource;
    }

    @Override
    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }


}
