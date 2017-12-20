package cn.riversky.mahout;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.precompute.example.GroupLensDataModel;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 与基于用户的技术不同的是，这种方法比较的是内容项与内容项之间的相似度
 * Item-based方法同样需要进行三个步骤获得推荐
 * 1得到内容项的历史评分数据
 * 2针对内容项进行内容之间的相似度计算，找到目标内容项的最近邻居
 * 3产生推荐。这里内容项之间的相似度是通过比较两个内容项上的用户行为选择矢量得到的。
 * 第二代协同过滤算法。
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/20.
 */
public class BaseItemRecommender {
    public static void main(String[] args) throws IOException, TasteException {
        DataModel dataModel=getDataModel2();
        //计算相似度，相似度有很多种，欧几里德，皮尔逊等等
//        ItemSimilarity itemSimilarity=new PearsonCorrelationSimilarity(dataModel);
        ItemSimilarity itemSimilarity=new EuclideanDistanceSimilarity(dataModel);
        //构建推荐器,协同过滤推荐中的基于物品的协同过滤推荐
        GenericItemBasedRecommender recommender=new GenericItemBasedRecommender(dataModel,itemSimilarity);
        //给用户等于5的用户推荐10个与2398相似的产品
        List<RecommendedItem> recommendedItems=recommender.recommendedBecause(5,1298,10);
        //打印推荐的结果
        System.out.println("使用基于物品的协同过滤算法");
        System.out.println("根据用户5当前浏览的商品2398，推荐10个相似的商品");

        for (RecommendedItem recommendedItem : recommendedItems) {
            System.out.println(recommendedItem);
        }
        long start = System.currentTimeMillis();
        //给用户等于5的用户推荐10个与34相似的产品
        System.out.println("根据用户5当前浏览的商品34，推荐10个相似的商品");
        recommendedItems = recommender.recommendedBecause(5, 34, 10);
        for (RecommendedItem recommendedItem : recommendedItems) {
            System.out.println(recommendedItem);
        }
        System.out.println(System.currentTimeMillis() -start);
    }
    private static DataModel getDataModel1() throws IOException {
        //准备数据 这里是电影评分数据
        File file=new File("F:\\大数据\\传智播客\\学习资料架\\day24\\ml-10M100K\\ratings.dat");
        //将数据加载到内存中，GroupLensDataModel是针对开放电影评论数据的
        return new GroupLensDataModel(file);
    }
    private static DataModel getDataModel2() throws IOException {
        //准备数据 这里是电影评分数据
//        File file=new File("F:\\大数据\\传智播客\\学习资料架\\day24\\ml-20m\\ratings.csv");
        File file=new File("F:\\大数据\\传智播客\\学习资料架\\day24\\ml-20m\\genome-scores.csv");
        //将数据加载到内存中，GroupLensDataModel是针对开放电影评论数据的
        return new FileDataModel(file);
    }
}
