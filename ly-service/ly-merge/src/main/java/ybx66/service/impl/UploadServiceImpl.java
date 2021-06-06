package ybx66.service.impl;

import cn.ybx66.conmmon.pojo.PageDTO;
import cn.ybx66.conmmon.pojo.PageResDTO;
import cn.ybx66.conmmon.pojo.PageResult;
import cn.ybx66.conmmon.utils.BeanUtil;
import cn.ybx66.conmmon.vo.ResultDescDTO;
import cn.ybx66.conmmon.vo.ResultMessageDTO;
import cn.ybx66.data_merge.pojo.Upload;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import ybx66.mapper.UploadMapper;
import ybx66.service.UploadService;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author Fox
 * @version 1.0
 * @date 2021/5/1 13:04
 * @description 上传下载业务层
 */
@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    public UploadMapper uploadMapper;

    @Override
    public ResultMessageDTO add(Upload upload) throws InvocationTargetException, IllegalAccessException {

        BeanUtil.autoSetAttrOnInsert(upload);
        int insert = uploadMapper.insert(upload);
        if (insert==0){
            return new ResultMessageDTO(400, ResultDescDTO.FAIL,"文件上传失败");
        }
        return new ResultMessageDTO(200,ResultDescDTO.OK,"文件上传成功");
    }

    @Override
    public ResultMessageDTO delete(String id) {
        //先找 再删 非暴力删除
        Upload del = uploadMapper.selectByPrimaryKey(id);
        if (del==null || del.getFlag()==0){
            return new ResultMessageDTO(400,ResultDescDTO.FAIL,"文件不存在");
        }
        del.setFlag(0);
        int key = uploadMapper.updateByPrimaryKey(del);

        return key==1 ? new ResultMessageDTO(200,ResultDescDTO.OK,"文件删除成功") : new ResultMessageDTO(400,ResultDescDTO.FAIL,"文件删除失败");
    }

    /**
     * 用户查询自己的
     * @param pageDTO
     * @return
     */
    @Override
    public ResultMessageDTO get(PageDTO pageDTO) {
        List<Upload> uploads =null;
        int sum =0;
        try{
            Example example = new Example(Upload.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("userId",pageDTO.getUserId());
            criteria.andEqualTo("flag",1);
            //分页
            Page<Upload> uploadPage = PageHelper.startPage(pageDTO.getPage(), pageDTO.getLimit());
            uploads = uploadMapper.selectByExample(example);
            //分页解析
            PageInfo<Upload> pageInfo = new PageInfo<Upload>(uploadPage.getResult());
            sum=(int) pageInfo.getTotal();
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResultMessageDTO(200,ResultDescDTO.OK,new PageResDTO(sum,uploads));
    }

    /**
     * 管理员的方法 shiro鉴别权限
     * @return
     */
    @Override
    public ResultMessageDTO getAll() {
        Upload upload = new Upload();
        upload.setFlag(1);
        List<Upload> select = uploadMapper.select(upload);
        return new ResultMessageDTO(200,ResultDescDTO.OK,select);
    }
}
