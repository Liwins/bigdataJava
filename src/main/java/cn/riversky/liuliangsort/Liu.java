package cn.riversky.liuliangsort;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by admin on 2017/11/25.
 */
public class Liu implements WritableComparable<Liu> {
private long upFlow;
private long downFlow;
private long sumFlow;
public Liu() {
}
public Liu(long upFlow, long downFlow) {
this.upFlow = upFlow;
this.downFlow = downFlow;
sumFlow = upFlow + downFlow;
}
public long getUpFlow() {
return upFlow;
}

public void setUpFlow(long upFlow) {
this.upFlow = upFlow;
}
public long getDownFlow() {
return downFlow;
}
public void setDownFlow(long downFlow) {
this.downFlow = downFlow;
}
public long getSumFlow() {
return sumFlow;
}
public void setSumFlow(long sumFlow) {
this.sumFlow = sumFlow;
}

public void setUpAndDown(long up,long down){
upFlow=up;
downFlow=down;
sumFlow=up+down;
}
@Override
public void write(DataOutput dataOutput) throws IOException {
dataOutput.writeLong(upFlow);
dataOutput.writeLong(downFlow);
dataOutput.writeLong(sumFlow);
}
@Override
public void readFields(DataInput dataInput) throws IOException {
upFlow = dataInput.readLong();
downFlow = dataInput.readLong();
sumFlow = dataInput.readLong();
}
@Override
public String toString() {
return upFlow + "\t" + downFlow + "\t" + sumFlow;
}

@Override
public int compareTo(Liu o) {
return this.sumFlow>o.sumFlow?1:-1;
}
}
