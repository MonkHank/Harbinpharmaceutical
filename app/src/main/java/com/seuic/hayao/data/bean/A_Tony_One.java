package com.seuic.hayao.data.bean;

import java.util.List;

/**
 * Created by Kevin on 2017/7/20.
 */

public class A_Tony_One {

    /**
     * date : 20170720
     * stories : [{"images":["https://pic4.zhimg.com/v2-fee0d12f291138ea3dded3fce5bacaa7.jpg"],"type":0,"id":9533295,"ga_prefix":"072012","title":"大误 · 「扎心了老铁」用英语怎么说？"},{"images":["https://pic1.zhimg.com/v2-9c51fe92bc65715c8da0893eda372278.jpg"],"type":0,"id":9530188,"ga_prefix":"072011","title":"就算基金公司集体倒闭了，你的钱还是你的钱"},{"images":["https://pic3.zhimg.com/v2-129d8c3b26325741404653ad98a50216.jpg"],"type":0,"id":9530854,"ga_prefix":"072010","title":"高铁、动车、G 字头、D 字头，这么说你就知道区别了"},{"images":["https://pic4.zhimg.com/v2-7b4a6a1eaedfab3687528051d24303bf.jpg"],"type":0,"id":9530262,"ga_prefix":"072009","title":"原本是电器零售巨头，却被后起的京东反超，苏宁输在哪？"},{"images":["https://pic2.zhimg.com/v2-d133b7624480cad0887b804bcf4550b1.jpg"],"type":0,"id":9532589,"ga_prefix":"072008","title":"腌一份牛肉，涮火锅、做麻辣烫、炒着吃都可以"},{"images":["https://pic1.zhimg.com/v2-54e77b8c5e0d436ffb191f437ab26054.jpg"],"type":0,"id":9531489,"ga_prefix":"072007","title":"今年 NBA 休赛季的最大新闻，是火箭队要被卖掉"},{"title":"DOTA 历史上的经典战术和套路，让人不得不服","ga_prefix":"072007","images":["https://pic2.zhimg.com/v2-5448c332fec18ce9e774454aeceb9729.jpg"],"multipic":true,"type":0,"id":9529909},{"images":["https://pic2.zhimg.com/v2-d249313ec12ecd8a1fe6c7c2a4af4885.jpg"],"type":0,"id":9532125,"ga_prefix":"072007","title":"我们用数据挖掘，看看孙悟空和二郎神谁更厉害"},{"images":["https://pic3.zhimg.com/v2-e05a012c40ed3ad5e6b63b7d95af90ce.jpg"],"type":0,"id":9532426,"ga_prefix":"072006","title":"瞎扯 · 如何正确地吐槽"}]
     * top_stories : [{"image":"https://pic1.zhimg.com/v2-4564795139659aac4fbd0228d38e97c0.jpg","type":0,"id":9530188,"ga_prefix":"072011","title":"就算基金公司集体倒闭了，你的钱还是你的钱"},{"image":"https://pic1.zhimg.com/v2-5ea5f63775c6eb979cc6f4f437fb8930.jpg","type":0,"id":9530854,"ga_prefix":"072010","title":"高铁、动车、G 字头、D 字头，这么说你就知道区别了"},{"image":"https://pic2.zhimg.com/v2-d962643dbf4fe345957d54515a43aed1.jpg","type":0,"id":9532125,"ga_prefix":"072007","title":"我们用数据挖掘，看看孙悟空和二郎神谁更厉害"},{"image":"https://pic4.zhimg.com/v2-73ea24c0a241afba741dd846afdbbfc3.jpg","type":0,"id":9531489,"ga_prefix":"072007","title":"今年 NBA 休赛季的最大新闻，是火箭队要被卖掉"},{"image":"https://pic3.zhimg.com/v2-f00332ceefec3fa652f4d89c19cc042a.jpg","type":0,"id":9532539,"ga_prefix":"071920","title":"买二手房漏了这一步，连户口都没有了"}]
     */

    private String date;
    private List<StoriesBean> stories;
    private List<TopStoriesBean> top_stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<StoriesBean> getStories() {
        return stories;
    }

    public void setStories(List<StoriesBean> stories) {
        this.stories = stories;
    }

    public List<TopStoriesBean> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<TopStoriesBean> top_stories) {
        this.top_stories = top_stories;
    }

    public static class StoriesBean {
        /**
         * images : ["https://pic4.zhimg.com/v2-fee0d12f291138ea3dded3fce5bacaa7.jpg"]
         * type : 0
         * id : 9533295
         * ga_prefix : 072012
         * title : 大误 · 「扎心了老铁」用英语怎么说？
         * multipic : true
         */

        private int type;
        private int id;
        private String ga_prefix;
        private String title;
        private boolean multipic;
        private List<String> images;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isMultipic() {
            return multipic;
        }

        public void setMultipic(boolean multipic) {
            this.multipic = multipic;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }

    public static class TopStoriesBean {
        /**
         * image : https://pic1.zhimg.com/v2-4564795139659aac4fbd0228d38e97c0.jpg
         * type : 0
         * id : 9530188
         * ga_prefix : 072011
         * title : 就算基金公司集体倒闭了，你的钱还是你的钱
         */

        private String image;
        private int type;
        private int id;
        private String ga_prefix;
        private String title;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
