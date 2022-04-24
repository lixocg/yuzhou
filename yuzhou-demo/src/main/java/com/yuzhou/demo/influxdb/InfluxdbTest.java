package com.yuzhou.demo.influxdb;

import org.influxdb.BatchOptions;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: lixiongcheng
 * @date: 2022/4/14 下午3:20
 */
public class InfluxdbTest {

    public static void main(String[] args) throws InterruptedException {
        InfluxDB influxDB = InfluxDBFactory.connect("http://localhost:8086", "root", "lee525079");//链接influxdb

        String dbName = "aTimeSeries";//数据库名

        influxDB.createDatabase(dbName);//创建数据库

        influxDB.setDatabase(dbName);//设置数据库

        String rpName = "aRetentionPolicy";//数据保留策略名称

        influxDB.createRetentionPolicy(rpName, dbName, "30d", 1, true);

//rpName:保留策略，dbName:数据库 ，30d:数据保留30天， 1:副本个数，  true:设为当前数据库默认的保留策略

        influxDB.setRetentionPolicy(rpName);//将创建的保留策略设置到当前数据库

//向influxdb写入一条数据方式一:

        String measure = "user_access_list1";
        influxDB.write(Point.measurement(measure)

                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)

                .tag("ip", "192.168.1.160")

                .tag("account_id", "zhangjunheng")

                .tag("path", "/item/search")

                .addField("user_id", 1)

                .addField("access_count", 1)

                .build());



        influxDB.enableBatch(BatchOptions.DEFAULTS);//开启批量写入，默认每1000点刷新一次，至少每1000毫秒刷新一次。

        Random r = new Random();
        for (int i = 0; i < 10000; i++) {
            int i1 = r.nextInt(200);

            int i2 = r.nextInt(10);
            Thread.sleep(i1);

            influxDB.write(Point.measurement(measure).time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)

                    .tag("ip", "192.168.1." + i2)

                    .tag("account_id", "zhangjunheng" + i)

                    .tag("path", "/item/search")

                    .addField("user_id", 1)

                    .addField("access_count", i+i1)
                    .build());

        }



        Query query = new Query("SELECT * FROM user_access_list", dbName);

        influxDB.query(query);

        System.out.println("done");

    }
}
