package cn.riversky.mahout;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.File;
import java.io.IOException;

/**
 * 获取推荐结果的查准率和召回率
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/20.
 */
public class MyIRStatistics {
    public static void main(String[] args) throws IOException, TasteException {
        //准备数据 这里是电影评分数据
        File file=new File("F:\\大数据\\传智播客\\学习资料架\\day24\\ml-10M100K\\ratings.dat");
        //将数据加载到内存中，GroupLensDataModel是针对开放电影评论数据的
//        DataModel dataModel=new GroupLensDataModel(file);
        DataModel dataModel=getDataModel2();
        RecommenderIRStatsEvaluator statsEvaluator=new GenericRecommenderIRStatsEvaluator();
        RecommenderBuilder recommenderBuilder=new RecommenderBuilder() {
            @Override
            public Recommender buildRecommender(DataModel dataModel) throws TasteException {
                UserSimilarity similarity=new PearsonCorrelationSimilarity(dataModel);
                UserNeighborhood neighborhood=new NearestNUserNeighborhood(4,similarity,dataModel);
                return new GenericUserBasedRecommender(dataModel,neighborhood,similarity);
            }
        };
        //计算推荐4个结果时的查准率和召回率
        //使用评估器，并设定评估期的参数
        //表示"precision and recall at 4"即相当于推荐top4，然后在top-4的推荐上计算准确率和召回率
        IRStatistics stats=statsEvaluator.evaluate(recommenderBuilder,null,dataModel,null,4,GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD,1.0);
        System.out.println(stats.getPrecision());
        System.out.println(stats.getRecall());
    }
    private static DataModel getDataModel2() throws IOException {
        //准备数据 这里是电影评分数据
        File file=new File("F:\\大数据\\传智播客\\学习资料架\\day24\\ml-20m\\ratings.csv");
        //将数据加载到内存中，GroupLensDataModel是针对开放电影评论数据的
        return new FileDataModel(file);
    }
}
