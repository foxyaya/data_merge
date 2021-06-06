package ybx66.service.impl;

import cn.ybx66.conmmon.vo.ResultDescDTO;
import cn.ybx66.conmmon.vo.ResultMessageDTO;
import cn.ybx66.data_merge.pojo.Goods;
import cn.ybx66.data_merge.pojo.GoodsMerge;
import cn.ybx66.data_merge.pojo.Shop;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import tk.mybatis.mapper.entity.Example;
import ybx66.configure.DemoTaskScheduler;
import ybx66.configure.JdbcConnectConfig;
import ybx66.dto.*;
import ybx66.mapper.DataMergeMapper;
import ybx66.mapper.GoodsMergeMapper;
import ybx66.service.DataMergeService;
import ybx66.service.ShopService;
import ybx66.utils.JDBCConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/4/28 15:59
 * @description
 */
@Service
public class DataMergeServiceImpl implements DataMergeService, Job {

    @Autowired
    public DataMergeMapper dataMergeMapper;
    @Autowired
    public JdbcConnectConfig config;
    @Autowired
    public DemoTaskScheduler demoTaskScheduler;
    @Autowired
    private Scheduler scheduler;
    @Autowired
    public SchedulerFactoryBean schedulerFactory;
    @Autowired
    public GoodsMergeMapper goodsMergeMapper;
    @Autowired
    public ShopService shopService;
    //数据聚合

    /**请求json
     * {
     *     "dataA":"data_merge",
     *     "dataB":"data_merge",
     *     "tableA":"tb_goods",
     *     "tableB":"tb_goods_temp",
     *     "columnA":["id","goods_price","-1"],
     *     "columnE":["id","price","-1","sign"]
     * }
     * @param mergeDTO
     * @return
     * @throws SQLException
     */
    @Override
    public ResultMessageDTO getMerge(MergeDTO mergeDTO) throws SQLException {
        //获取数据 //非相似列以-1分隔
        String[] columnA = mergeDTO.getColumnA();
        String[] columnE = mergeDTO.getColumnE();
        if(columnA==null || columnA.length==0 || columnE==null || columnE.length==0) {
            return new ResultMessageDTO(400,ResultDescDTO.FAIL,"传输数据有误");
        }
        //将两表字段整合
        //最长长度
        int temp = columnA.length > columnE.length ? columnA.length : columnE.length;
        int i =0;
        while (i<temp){
            if (columnA[i].equals("-1")){
                i++;
                break;
            }
            i++;
        }
        //需要补齐的长度
        int size = columnA.length+columnE.length-2*i;
        //1 第一个表的列剩余
        int tempA = columnA.length -i;
        //2 第二个表的列剩余
        int tempB = columnE.length -i;
        //两个新的
        String[] Temp = new String[i+size];
        String[] columnF = new String[i+size];
        String[] columnQ = new String[i+size];
        for (int j =0; j<i-1;j++){
            columnF[j]=columnA[j];
            columnQ[j]=columnE[j];
            Temp[j] = columnA[j];
        }
        int tt =i-1;
        int k=i;
        while (tempA!=0){
            columnF[tt]=columnA[k];
            columnQ[tt]=null;
            Temp[tt] = columnA[k];
            k++;
            tt++;
            tempA--;
        }
        while (tempB!=0){
            columnF[tt]=null;
            columnQ[tt]=columnE[i];
            Temp[tt] = columnE[i];
            i++;
            tt++;
            tempB--;
        }

        // 生成表
        JDBCConnection jdbcConnection = new JDBCConnection();
        Connection connection = jdbcConnection.getConnection(config.getDriverClassName(),config.getUsername(),config.getPassword(),config.getUrl());
        //如果生成的表存在 则对原有的进行删除
        List<String> data_merge = dataMergeMapper.getDataTableName("data_merge");
        //固定表
        if (data_merge.contains("tb_goods_merge") ){
            String sql = "drop table "+ "tb_goods_merge";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("CREATE table "+" tb_goods_merge "+" (");
        int j = 0;
        for (; j <Temp.length-2 ; j++) {
            stringBuffer.append(Temp[j]+"  VARCHAR(255),");
        }
        stringBuffer.append(Temp[j]+"  VARCHAR(255)");
        stringBuffer.append(");");
        PreparedStatement statement = connection.prepareStatement(stringBuffer.toString());
        statement.executeUpdate();
        // 先获取数据  新旧数据   获取可能不同库的数据
        //目前整合都是这个连接的
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT ");
        int a = 0;
        for (; a < columnF.length-2; a++) {
            if (columnF[a] ==null){
                sb.append(columnF[a]+" as " + Temp[a]+", ");
            }else {
                sb.append(columnF[a]+", ");
            }
        }
        if (columnF[a] ==null){
            sb.append(columnF[a]+" as " + Temp[a]+" ");
        }else {
            sb.append(columnF[a]);
        }
        sb.append(" FROM "+mergeDTO.getDataA()+"."+mergeDTO.getTableA());
        sb.append(" UNION ALL ");
        sb.append(" SELECT ");
        int q = 0;
        for (; q < columnQ.length-2; q++) {
            if (columnQ[q]==null){
                sb.append(columnQ[q]+" as "+ Temp[q]+", ");
            }else {
                sb.append(columnQ[q]+", ");
            }
        }
        if (columnQ[q]==null){
            sb.append(columnQ[q]+" as "+ Temp[q]);
        }else {
            sb.append(columnQ[q]);
        }
        sb.append(" FROM "+mergeDTO.getDataB()+"."+mergeDTO.getTableB());
        PreparedStatement ps = connection.prepareStatement(sb.toString());
        ResultSet rs = ps.executeQuery();
        ArrayList<ArrayList<String>> arrayLists = new ArrayList<ArrayList<String>>();
        while (rs.next()){
            ArrayList<String> arrayList = new ArrayList<>();
            for (int p = 0; p <Temp.length-1 ; p++) {
                arrayList.add(rs.getString(Temp[p]));
            }
            arrayLists.add(arrayList);
        }

        //新数据直接注入
        StringBuffer stringBufferTemp = new StringBuffer();
        stringBufferTemp.append("INSERT INTO "+"tb_goods_merge"+" VALUES ");
        int i1 = 0;
        for (; i1 < arrayLists.size()-1; i1++) {
            stringBufferTemp.append("(");
            int i2 = 0;
            for (; i2 < arrayLists.get(i1).size()-1; i2++) {
                stringBufferTemp.append("'"+arrayLists.get(i1).get(i2)+"',");
            }
            stringBufferTemp.append("'"+arrayLists.get(i1).get(i2)+"'),");
        }
        stringBufferTemp.append("(");
        int i2 = 0;
        for (; i2 < arrayLists.get(i1).size()-1; i2++) {
            stringBufferTemp.append("'"+arrayLists.get(i1).get(i2)+"',");
        }
        stringBufferTemp.append("'"+arrayLists.get(i1).get(i2)+"');");
        PreparedStatement preparedStatement = connection.prepareStatement(stringBufferTemp.toString());
        preparedStatement.executeUpdate();
        connection.close();
        return new ResultMessageDTO(200, ResultDescDTO.OK,"success");
    }
    @Override
    public ResultMessageDTO getOtherMerge(MergeDTO mergeDTO) throws SQLException {
        //获取数据 //非相似列以-1分隔
        String[] columnA = mergeDTO.getColumnA();
        String[] columnE = mergeDTO.getColumnE();
        if(columnA==null || columnA.length==0 || columnE==null || columnE.length==0) {
            return new ResultMessageDTO(400,ResultDescDTO.FAIL,"传输数据有误");
        }
        //将两表字段整合
        //最长长度
        int temp = columnA.length > columnE.length ? columnA.length : columnE.length;
        int i =0;
        while (i<temp){
            if (columnA[i].equals("-1")){
                i++;
                break;
            }
            i++;
        }
        //需要补齐的长度
        int size = columnA.length+columnE.length-2*i;
        //1 第一个表的列剩余
        int tempA = columnA.length -i;
        //2 第二个表的列剩余
        int tempB = columnE.length -i;
        //两个新的
        String[] Temp = new String[i+size];
        String[] columnF = new String[i+size];
        String[] columnQ = new String[i+size];
        for (int j =0; j<i-1;j++){
            columnF[j]=columnA[j];
            columnQ[j]=columnE[j];
            Temp[j] = columnA[j];
        }
        int tt =i-1;
        int k=i;
        while (tempA!=0){
            columnF[tt]=columnA[k];
            columnQ[tt]=null;
            Temp[tt] = columnA[k];
            k++;
            tt++;
            tempA--;
        }
        while (tempB!=0){
            columnF[tt]=null;
            columnQ[tt]=columnE[i];
            Temp[tt] = columnE[i];
            i++;
            tt++;
            tempB--;
        }

        // 生成表
        JDBCConnection jdbcConnection = new JDBCConnection();
        Connection connection = jdbcConnection.getConnection(config.getDriverClassName(),config.getUsername(),config.getPassword(),config.getUrl());
        //如果生成的表存在 则对原有的进行删除
        List<String> data_merge = dataMergeMapper.getDataTableName("data_merge");
        //固定表
        if (data_merge.contains(mergeDTO.getTableA()+mergeDTO.getDataB()) ){
            String sql = "drop table "+ mergeDTO.getTableA()+mergeDTO.getDataB();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("CREATE table "+mergeDTO.getTableA()+mergeDTO.getDataB()+" (");
        int j = 0;
        for (; j <Temp.length-2 ; j++) {
            stringBuffer.append(Temp[j]+"  VARCHAR(255),");
        }
        stringBuffer.append(Temp[j]+"  VARCHAR(255)");
        stringBuffer.append(");");
        PreparedStatement statement = connection.prepareStatement(stringBuffer.toString());
        statement.executeUpdate();
        // 先获取数据  新旧数据   获取可能不同库的数据
        //目前整合都是这个连接的
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT ");
        int a = 0;
        for (; a < columnF.length-2; a++) {
            if (columnF[a] ==null){
                sb.append(columnF[a]+" as " + Temp[a]+", ");
            }else {
                sb.append(columnF[a]+", ");
            }
        }
        if (columnF[a] ==null){
            sb.append(columnF[a]+" as " + Temp[a]+" ");
        }else {
            sb.append(columnF[a]);
        }
        sb.append(" FROM "+mergeDTO.getDataA()+"."+mergeDTO.getTableA());
        sb.append(" UNION ALL ");
        sb.append(" SELECT ");
        int q = 0;
        for (; q < columnQ.length-2; q++) {
            if (columnQ[q]==null){
                sb.append(columnQ[q]+" as "+ Temp[q]+", ");
            }else {
                sb.append(columnQ[q]+", ");
            }
        }
        if (columnQ[q]==null){
            sb.append(columnQ[q]+" as "+ Temp[q]);
        }else {
            sb.append(columnQ[q]);
        }
        sb.append(" FROM "+mergeDTO.getDataB()+"."+mergeDTO.getTableB());
        PreparedStatement ps = connection.prepareStatement(sb.toString());
        ResultSet rs = ps.executeQuery();
        ArrayList<ArrayList<String>> arrayLists = new ArrayList<ArrayList<String>>();
        while (rs.next()){
            ArrayList<String> arrayList = new ArrayList<>();
            for (int p = 0; p <Temp.length-1 ; p++) {
                arrayList.add(rs.getString(Temp[p]));
            }
            arrayLists.add(arrayList);
        }

        //新数据直接注入
        StringBuffer stringBufferTemp = new StringBuffer();
        stringBufferTemp.append("INSERT INTO "+mergeDTO.getTableA()+mergeDTO.getDataB()+" VALUES ");
        int i1 = 0;
        for (; i1 < arrayLists.size()-1; i1++) {
            stringBufferTemp.append("(");
            int i2 = 0;
            for (; i2 < arrayLists.get(i1).size()-1; i2++) {
                stringBufferTemp.append("'"+arrayLists.get(i1).get(i2)+"',");
            }
            stringBufferTemp.append("'"+arrayLists.get(i1).get(i2)+"'),");
        }
        stringBufferTemp.append("(");
        int i2 = 0;
        for (; i2 < arrayLists.get(i1).size()-1; i2++) {
            stringBufferTemp.append("'"+arrayLists.get(i1).get(i2)+"',");
        }
        stringBufferTemp.append("'"+arrayLists.get(i1).get(i2)+"');");
        PreparedStatement preparedStatement = connection.prepareStatement(stringBufferTemp.toString());
        preparedStatement.executeUpdate();
        connection.close();
        return new ResultMessageDTO(200, ResultDescDTO.OK,"success");
    }

    /**
     * 获取指定数据库列名
     * @param columnDTO
     * @return
     */
    @Override
    public ResultMessageDTO getField(ColumnDTO columnDTO) {
        List<String> columnName = dataMergeMapper.getColumnName(columnDTO.getDataTable(), columnDTO.getTableName());
        if (columnName==null || columnName.size()==0){
            return new ResultMessageDTO(400,ResultDescDTO.FAIL,"无数据，请检查表名是否出错");
        }
        return new ResultMessageDTO(200,ResultDescDTO.OK,columnName.toArray());
    }

    /**
     * 获取全部数据库名
     * @return
     */
    @Override
    public ResultMessageDTO getDatabase() {
        List<String> database = dataMergeMapper.getDatabase();
        if (database==null || database.size()==0){
            return new ResultMessageDTO(400,ResultDescDTO.FAIL,"错误");
        }
        return new ResultMessageDTO(200,ResultDescDTO.OK,database.toArray());
    }

    /**
     * 获取全表名
     * @param databaseName
     * @return
     */
    @Override
    public ResultMessageDTO getColumn(String databaseName) {
        List<String> dataTableName = dataMergeMapper.getDataTableName(databaseName);
        if (dataTableName==null || dataTableName.size()==0){
            return new ResultMessageDTO(400,ResultDescDTO.FAIL,"错误");
        }
        return new ResultMessageDTO(200,ResultDescDTO.OK,dataTableName.toArray());
    }
    //弃用
    @Override
    public ResultMessageDTO getTwoData(JDBCDTO jdbcdto) {
        return null;
    }

    @Override
    public ResultMessageDTO getBestGoods(BestGoodsDTO bestGoodsDTO) {
        List<GoodsResDTO> goodsResDTOS=null;
        List<GoodsMerge> goodsMerges = null;
        try {
            Example example = new Example(GoodsMerge.class);
            Example.Criteria criteria = example.createCriteria();
            if (bestGoodsDTO.getGoodsName()!=null && !bestGoodsDTO.getGoodsName().equals("")){
                criteria.andLike("goodsName",'%'+bestGoodsDTO.getGoodsName()+'%');
            }
            if (bestGoodsDTO.getType()!=null && !bestGoodsDTO.getType().equals("")){
                criteria.andEqualTo("typeId",bestGoodsDTO.getType());
            }
            criteria.andEqualTo("flag",1);
            example.setOrderByClause("goods_price ASC");
            PageHelper.startPage(0, bestGoodsDTO.getLimit()==null ? 1:bestGoodsDTO.getLimit());
            goodsMerges = goodsMergeMapper.selectByExample(example);
            if (goodsMerges!=null && goodsMerges.size()!=0){
                goodsResDTOS =new ArrayList<>();
                for (GoodsMerge goods : goodsMerges) {
                    GoodsResDTO goodsResDTO = new GoodsResDTO();
                    BeanUtils.copyProperties(goods,goodsResDTO);
                    ResultMessageDTO byId = shopService.findById(goods.getShopId());
                    //存在的获取店铺异常的情况
                    try{
                        Shop shop = JSON.parseObject(JSON.toJSONString(byId.getMessage()), Shop.class);
                        goodsResDTO.setShopName(shop.getShopName());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    goodsResDTOS.add(goodsResDTO);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResultMessageDTO(200,ResultDescDTO.OK,goodsResDTOS);
    }

    @Override
    public ResultMessageDTO startTask() { //开启每日定时整合任务 每天两点的定时任务
        String msg =demoTaskScheduler.addJob("job","test","trigger","t1",this.getClass(),"0 0 2-2 * * ? *");//0 0/1 * * * ? *    0 0 2-2 * * ? *
        return new ResultMessageDTO(200,ResultDescDTO.OK,msg);
    }

    @Override
    public ResultMessageDTO endTask() {//关闭每日定时整合任务
        demoTaskScheduler.removeJob("job","test","trigger","t1");
        return new ResultMessageDTO(200,ResultDescDTO.OK,"关闭每日两点定时整合任务");
    }

    @Override
    public ResultMessageDTO  getTask() {
        try {
            scheduler = schedulerFactory.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey("trigger", "t1");
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                 return new ResultMessageDTO(200,ResultDescDTO.OK,false);
            }
            return new ResultMessageDTO(200,ResultDescDTO.OK,true);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return new ResultMessageDTO(200,ResultDescDTO.OK,true);
    }
/**请求json
 {
 *     "dataA":"data_merge",
 *     "dataB":"data_merge",
 *     "tableA":"tb_goods",
 *     "tableB":"tb_goods_temp",
 *     "columnA":["id","goods_price","-1"],
 *     "columnE":["id","price","-1","sign"]
 * }
 * **/
    /**
     * 初始化任务  固定
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        MergeDTO mergeDTO =JSONObject.parseObject("{  \"dataA\":\"data_merge\",\"dataB\":\"data_merge\", \"tableA\":\"tb_goods\",\"tableB\":\"tb_goods_temp\", \"columnA\":[\"id\",\"type_id\",\"shop_id\",\"goods_price\",\"goods_name\",\"flag\",\"-1\",\"goods_image\"],\"columnE\":[\"id\",\"ids\",\"shop_id\",\"price\",\"name\",\"flag\",\"-1\",\"sign\"]}",MergeDTO.class);
        try{
            getMerge(mergeDTO);
            System.out.println(" 任务执行成功" );
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * 非同源数据的处理过程 思考过程：手写sql 查找两不同源数据库字段 以及保留的字段  然后是数据查询与剔除的过程 最后是整合生成表
     */

}
