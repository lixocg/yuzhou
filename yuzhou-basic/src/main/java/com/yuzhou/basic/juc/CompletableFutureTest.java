package com.yuzhou.basic.juc;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class CompletableFutureTest {

    static class Shop{
        private Random random = new Random();

        private String name;

        public Shop(String name){
            this.name = name;
        }
        /**
         * 根据产品名查找价格
         * */
        public double getPrice(String product) {
            return calculatePrice(product);
        }

        /**
         * 计算价格
         *
         * @param product
         * @return
         * */
        private double calculatePrice(String product) {
            delay();
            //random.nextDouble()随机返回折扣
            return random.nextDouble() * product.charAt(0) + product.charAt(1);
        }

        /**
         * 通过睡眠模拟其他耗时操作
         * */
        private void delay() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private static List<Shop> shopList = Arrays.asList(
            new Shop("BestPrice"),
            new Shop("LetsSaveBig"),
            new Shop("MyFavoriteShop"),
            new Shop("BuyItAll")
    );


    /**
     * 并行计算，结果无依赖
     */
    @Test
    public void findPriceAsync(){
        List<CompletableFuture<String>> completableFutureList = shopList.stream()
                //转异步执行
                .map(shop -> CompletableFuture.supplyAsync(
                        () -> String.format("%s price is %.2f",
                                shop.getName(), shop.getPrice(shop.getName()))))  //格式转换
                .collect(Collectors.toList());

        List<String> collect = completableFutureList.stream()
                .map(CompletableFuture::join)  //获取结果不会抛出异常
                .collect(Collectors.toList());

        System.out.println(collect);
    }
}
