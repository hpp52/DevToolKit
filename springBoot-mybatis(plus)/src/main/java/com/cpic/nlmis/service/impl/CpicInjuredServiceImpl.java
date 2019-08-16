package com.cpic.nlmis.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cpic.nlmis.common.utils.SnowflakeIdWorker;
import com.cpic.nlmis.dao.FeeDetailEntityMapper;
import com.cpic.nlmis.dao.InjuredEntityMapper;
import com.cpic.nlmis.dto.InjuredInfoDto;
import com.cpic.nlmis.model.FeeDetailEntity;
import com.cpic.nlmis.model.InjuredEntity;
import com.cpic.nlmis.service.CpicInjuredService;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.cpic.nlmis.common.utils.SymbolUtil.Separator;

/**
 * @Author: hk
 * @Description:
 * @Date: 2019/8/14 16:22
 */
@Service("cpicInjuredService")
public class CpicInjuredServiceImpl implements CpicInjuredService {
    private final static Logger logger = LoggerFactory.getLogger(CpicInjuredServiceImpl.class);
    @Autowired
    private   InjuredEntityMapper  injuredEntityMapper;
    @Autowired
    private   FeeDetailEntityMapper   feeDetailMapper;
    @Override
    public int deleteByPrimaryKey(String id) {
        return injuredEntityMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(InjuredEntity record) {
        return injuredEntityMapper.insert(record);
    }

    @Override
    public InjuredEntity selectByPrimaryKey(String id) {
        return injuredEntityMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<InjuredEntity> selectAll() {
        return injuredEntityMapper.selectAll();
    }

    @Override
    public int updateByPrimaryKey(InjuredEntity record) {
        return injuredEntityMapper.updateByPrimaryKey(record);
    }

    @Override
    public int submit(InjuredInfoDto dto) throws Exception {
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0, 0);
        String feeDetail= dto.getFeeDetail();

        String signature=dto.getHospitalCode()+Separator+dto.getInjuryName()+Separator+
                dateFormat.format(dto.getActDate())+Separator+43201;
        logger.info("拿到加盐未加密之前的签名"+signature);
        String signatureMD5= DigestUtils.md5Hex(signature);
        logger.info("加密后的签名"+signatureMD5);
        InjuredEntity result=new InjuredEntity();
        BeanUtils.copyProperties(dto,result);
        result.setSignature(signatureMD5);

        /**
         * json转换 此处容易出错
         */
        logger.info("拿到了未转换之前的费用明细====>"+feeDetail);
        String feeJosn= JSONObject.toJSONString(
                feeDetail, SerializerFeature.WriteClassName);
        logger.info("费用明细json数组转换为的String====>"+feeJosn);
        List<FeeDetailEntity> feeDetailList = JSONObject.parseArray(
                feeJosn, FeeDetailEntity.class);
        logger.info("最终拿到的费用list"+feeDetailList.toString());
        if (feeDetailList==null||feeDetailList.size()==0){
            throw  new Exception("费用明细转换错误请核对费用json数组串");
        }
        feeDetailList.forEach(t->{
            Long id2 = idWorker.nextId();
            t.setId(id2.toString());
            t.setStatus("1");
            t.setInjuredId(dto.getId());
            t.setInjuryName(dto.getInjuryName());
            t.setActDate(new Date());
            feeDetailMapper.insert(t);
        });
        return this.insert(result);
    }
}
