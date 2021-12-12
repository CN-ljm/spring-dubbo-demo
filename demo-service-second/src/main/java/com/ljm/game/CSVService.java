package com.ljm.game;

import com.ljm.helper.MybatisShardingHelper;
import com.ljm.pojo.Person;
import com.ljm.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author create by jiamingl on 下午2:05
 * @title
 * @desc
 */
@Service
@Slf4j
public class CSVService {
    // 线程池
    private static ExecutorService executorService = null;

    @Autowired
    private PersonService personService;

    static {
        executorService = Executors.newFixedThreadPool(4);
    }

    //处理
    public void dealWithCSVFile(String csvFilePath, int singleCount, int tableCount) {
        File file = new File(csvFilePath);

        try(BufferedReader reader = new BufferedReader(new FileReader(file));) {
            String line;
            int count = 0;
            int tableSeq = 0;
            List<String> lines = new ArrayList<>();
            while ((line = reader.readLine()) != null){
                lines.add(line);
                count +=1;
                // 到达要处理的阈值
                if (count >= singleCount) {
                    List<String> finalLines = lines;
                    int seq = tableSeq;
                    executorService.submit(() -> {
                        processCSVRecord(finalLines, seq);
                    });
                    // 重建建个容器
                    lines = new ArrayList<>();
                    // 计数归零
                    count = 0;
                    // 换表
                    tableSeq += 1;
                    if (tableSeq >= tableCount) {
                        tableSeq = 0;
                    }
                }
            }

            if (!ObjectUtils.isEmpty(lines)) {
                processCSVRecord(lines, tableSeq);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // 处理入库
    public void processCSVRecord(List<String> lines, int tableSeq) {
        log.info("处理入库，记录数：{}，数据库编号：{}", lines.size(), tableSeq);
        // 设置分表
        MybatisShardingHelper.setTableNO(tableSeq);

        log.info("设置上下文变量完成");

        List<Person> personList = new ArrayList<>();
        for (String line: lines) {
            Person p = new Person();
            personList.add(p);
            String[] fields = line.split(",");
            p.setName(fields[0]);
            p.setIdCard(fields[1]);
            p.setCellphone(fields[2]);
            p.setBirthDate(LocalDate.parse(fields[3], DateTimeFormatter.ofPattern("MM-dd-yyyy")));
            p.setAddress(fields[4]);
        }

        log.info("入库记录数:{}", personList.size());
        personService.saveBatch(personList);

        // 移除当前线程分表
        MybatisShardingHelper.removeTableNO();
    }

    public static void main(String[] args) {
        CSVService service = new CSVService();
        service.dealWithCSVFile("/Users/jiamingl/Desktop/person-6.csv", 10, 4);
    }

}
