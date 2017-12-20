package cn.riversky.recommender.dto;

/**
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/20.
 */
public class Item implements Comparable{
    private String id;
    private double weight;

    public Item() {
    }

    public Item(String id, double weight) {
        this.id = id;
        this.weight = weight;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", weight=" + weight +
                '}';
    }

    @Override
    public int compareTo(Object o) {

        Item item= (Item) o;
        if(weight>item.weight){
            return 1;
        }
        if(weight<item.weight){
            return -1;
        }
        return 0;
    }
}
