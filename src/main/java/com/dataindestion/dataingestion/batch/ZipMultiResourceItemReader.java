package com.dataindestion.dataingestion.batch;


import com.dataindestion.dataingestion.domain.PersonInput;
import lombok.SneakyThrows;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.io.inputstream.ZipInputStream;
import net.lingala.zip4j.model.FileHeader;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class ZipMultiResourceItemReader<T extends PersonInput> extends MultiResourceItemReader<PersonInput> {

    @Value("${data.source}")
    private ZipFile zipFile;

    public ZipMultiResourceItemReader() {
        super();
    }

    @SneakyThrows
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        List<FileHeader> fileHeaders = zipFile.getFileHeaders();
        Resource[] resources = fileHeaders
                .stream()
                .map(this::getInputStreamResource).toArray(Resource[]::new);

        super.setResources(resources);

        // Compares resource descriptions.
        super.setComparator(Comparator.comparing(Resource::getDescription));
        
        super.setDelegate(reader());
        super.open(executionContext);
    }

    private InputStreamResource getInputStreamResource(FileHeader fileHeader) {
        try {
            ZipInputStream zipInputStream = zipFile.getInputStream(fileHeader);
            
            return new InputStreamResource(zipInputStream, fileHeader.getFileName());
        } catch (IOException e) {
            e.printStackTrace();
            
            return null;
        }
    }

    public FlatFileItemReader<T> reader()
    {
        //Create reader instance
        FlatFileItemReader<T> reader = new FlatFileItemReader<T>();

        //Set number of lines to skips, to ignore header row
        reader.setLinesToSkip(1);

        //Configure how each line will be parsed and mapped to different values
        reader.setLineMapper(new DefaultLineMapper() {
            {
                //3 columns in each row
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames("firstName", "lastName", "date");
                    }
                });
                //Set values in Person class
                setFieldSetMapper(new BeanWrapperFieldSetMapper<PersonInput>() {
                    {
                        setTargetType(PersonInput.class);
                    }
                });
            }
        });

        return reader;
    }
}