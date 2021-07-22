package com.example.serch.serviceimpl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.serch.mapper.Mapper;
import com.example.serch.pojo.Bean;
import com.example.serch.service.Inservice;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 亦梦里 亦书予
 * @version 1.0
 * @date 2021/7/7 10:01
 */
@Service
public class Inserviceimpl implements Inservice {
    private static final String Exchange_name = "test";
    @Autowired
    private Mapper mapper;
    //读取json文件
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Override
    public int readJsonFile(String fileName) {

        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), StandardCharsets.UTF_8);
            int ch = 0;
            StringBuilder sb = new StringBuilder();

            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }


            JSONObject root = JSON.parseObject(sb.toString());
            JSONArray records = root.getJSONArray("RECORDS");

            List<Bean> beanList = JSON.parseArray(records.toJSONString(), Bean.class);

            int pointsDataLimit = 500;
            int listSize = beanList.size();
            int maxSize = listSize - 1;
            List<Bean> newList = new ArrayList<Bean>();//新建一个载体list
            for (int i = 0; i < listSize; i++) {
                //分批次处理
                newList.add(beanList.get(i));//循环将数据填入载体list
                if (pointsDataLimit == newList.size() || i == maxSize) {  //载体list达到要求,进行批量操作
                    //调用批量插入
                    mapper.Saveadd(newList);
                    newList.clear();//每次批量操作后,清空载体list,等待下次的数据填入
                }
            }

            return 1;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }
    @Override
    public boolean getData() {
        List<Map> list = mapper.Gatdata();

//        for (Object a:list){

//            System.out.println(a);
//        }
        rabbitTemplate.convertAndSend(Exchange_name, "*", list);
        return true;
    }
}



