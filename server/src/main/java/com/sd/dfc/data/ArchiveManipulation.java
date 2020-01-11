package com.sd.dfc.data;

import java.io.IOException;

public interface ArchiveManipulation {
     void write(String text) throws IOException;
     String getDataSource();
     void setDataSource(String dataSource);
}
