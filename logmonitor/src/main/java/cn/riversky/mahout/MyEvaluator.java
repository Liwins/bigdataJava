package cn.riversky.mahout;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.File;
import java.io.IOException;

/**
 * 获取推荐模型的评分
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/20.
 */
public class MyEvaluator {
    public static void main(String[] args) throws IOException, TasteException {
        //准备数据 这里是电影评分数据
        File file=new File("F:\\大数据\\传智播客\\学习资料架\\day24\\ml-10M100K\\ratings.dat");
        //将数据加载到内存中，GroupLensDataModel是针对开放电影评论数据的
        DataModel dataModel = getDataModel2();
        //推荐评估，使用均方根
        //RecommenderEvaluator evaluator = new RMSRecommenderEvaluator();
        //推荐评估，使用平均差值
        RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
        RecommenderBuilder builder = new RecommenderBuilder() {
            @Override
            public Recommender buildRecommender(DataModel dataModel) throws TasteException {
//                UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
//                UserSimilarity similarity = new EuclideanDistanceSimilarity(dataModel);
                UserSimilarity similarity = new TanimotoCoefficientSimilarity(dataModel);
                UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, dataModel);
                return new GenericUserBasedRecommender(dataModel, neighborhood, similarity);
            }
        };
        // 用70%的数据用作训练，剩下的30%用来测试
        double score = evaluator.evaluate(builder, null, dataModel, 0.7, 1.0);
        //最后得出的评估值越小，说明推荐结果越好
        System.out.println(score);
    }
    private static DataModel getDataModel2() throws IOException {
        //准备数据 这里是电影评分数据
//        File file=new File("F:\\大数据\\传智播客\\学习资料架\\day24\\ml-20m\\ratings.csv");
        File file=new File("F:\\大数据\\传智播客\\学习资料架\\day24\\ml-20m\\genome-scores.csv");
        //将数据加载到内存中，GroupLensDataModel是针对开放电影评论数据的
        return new FileDataModel(file);
    }
}
