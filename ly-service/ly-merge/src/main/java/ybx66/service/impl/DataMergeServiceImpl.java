package ybx66.service.impl;

import cn.ybx66.conmmon.vo.ResultDescDTO;
import cn.ybx66.conmmon.vo.ResultMessageDTO;
import ybx66.dto.ColumnDTO;
import ybx66.dto.MergeDTO;
import ybx66.mapper.DataMergeMapper;
import ybx66.service.DataMergeService;
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
public class DataMergeServiceImpl implements DataMergeService {

    @Autowired
    public DataMergeMapper dataMergeMapper;

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
        Connection connection = jdbcConnection.getConnection();
        //如果生成的表存在 则对原有的进行删除
        List<String> data_merge = dataMergeMapper.getDataTableName("data_merge");
        if (data_merge.contains(mergeDTO.getTableA()+"_"+mergeDTO.getTableB()) || data_merge.contains(mergeDTO.getTableB()+"_"+mergeDTO.getTableA())){
            String sql = "drop table "+ mergeDTO.getTableA()+"_"+mergeDTO.getTableB();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("CREATE table "+mergeDTO.getTableA()+"_"+mergeDTO.getTableB()+" (");
        int j = 0;
        for (; j <Temp.length-2 ; j++) {
            stringBuffer.append(Temp[j]+"  VARCHAR(255),");
        }
        stringBuffer.append(Temp[j]+"  VARCHAR(255)");
        stringBuffer.append(");");
        PreparedStatement statement = connection.prepareStatement(stringBuffer.toString());
        statement.executeUpdate();
        // 先获取数据  新旧数据   获取可能不同库的数据
        //todo 这里的数据库应该是源数据库  不是目标数据库 由于tk.mybatis的局限性 尝试采用其他的手写sql 或者重新创建个源数据库的连接工具
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
        stringBufferTemp.append("INSERT INTO "+mergeDTO.getTableA()+"_"+mergeDTO.getTableB()+" VALUES ");
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
        return new ResultMessageDTO(200,ResultDescDTO.OK,(String[])columnName.toArray());
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
        return new ResultMessageDTO(200,ResultDescDTO.OK,(String[])database.toArray());
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
        return new ResultMessageDTO(200,ResultDescDTO.OK,(String[])dataTableName.toArray());
    }

    /**
     * 非同源数据的处理过程 思考过程：手写sql 查找两不同源数据库字段 以及保留的字段  然后是数据查询与剔除的过程 最后是整合生成表
     */

}
