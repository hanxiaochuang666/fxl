package com.by.blcu.core.filter;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.AbstractMatcherFilter;
import ch.qos.logback.core.spi.FilterReply;
import com.by.blcu.core.utils.DateUtils;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

@Component
@Data
public class MyLogFilter extends AbstractMatcherFilter<ILoggingEvent> {
    /**
     * 过滤包路径
     */
    private String packageLogerPath;
    /**
     * 日志基础路径
     */
    private String filterPackagePath;

    @Override
    public FilterReply decide(ILoggingEvent iLoggingEvent) {
        String loggerPath = iLoggingEvent.getLoggerName();
        if(loggerPath.contains(filterPackagePath)){
            //拆解日志并输出
            String levelStr=iLoggingEvent.getLevel().toString();
            long timeStamp = iLoggingEvent.getTimeStamp();
            Date date = new Date(timeStamp);
            String message = iLoggingEvent.getMessage().toString();
            int lastIndexOf = loggerPath.lastIndexOf(".");
            String dirBefore = loggerPath.substring(lastIndexOf+1);
            File dir = new File(packageLogerPath+"/"+dirBefore);
            //如果对应的类路径不存在，则创建路径
            if(!dir.exists()){
                dir.mkdir();
            }
            String dirEnd=null;
            try {
                dirEnd=DateUtils.date2String(new Date(),"yyy-MM-dd");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            dir=new File(packageLogerPath+"/"+dirBefore+"/"+dirEnd+".txt");
            BufferedWriter output=null;
            if(!dir.exists()){
                try {
                    dir.createNewFile();
                    output = new BufferedWriter(new FileWriter(dir,false));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else{
                try {
                    output = new BufferedWriter(new FileWriter(dir,true));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            String date2String="";
            try {
                date2String = DateUtils.date2String(date, "yyyy-MM-dd HH:mm:ss");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String res=date2String+"["+dirBefore+"]"+" "+levelStr+" : "+message+"\r\n";
            try {
                output.write(res);
                output.flush();
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return FilterReply.NEUTRAL;
    }
}
